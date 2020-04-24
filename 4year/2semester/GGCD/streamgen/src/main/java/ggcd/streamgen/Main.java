package ggcd.streamgen;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.AhrensDieterExponentialSampler;
import org.apache.commons.rng.sampling.distribution.ContinuousSampler;
import org.apache.commons.rng.simple.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static class Entry {
        public String id;
        public float rating;

        public Entry(String id, float rating) {
            this.id = id;
            this.rating = rating;
        }
    }

    public static void main(String[] args) throws Exception {

        String name = args.length<1 ? "title.ratings.tsv.gz" : args[0];

        double rate = args.length<2 ? 120 : Double.parseDouble(args[1]);

        log.info("reading IMDb ratings from {}, generating {} events/min", name, rate);

        InputStream is = new CompressorStreamFactory()
                .createCompressorInputStream(
                        new BufferedInputStream(
                                new FileInputStream(name)));

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<Entry> titles = new ArrayList<>();

        br.readLine(); // discard header
        String line = br.readLine();
        while(line != null) {
            String[] row = line.split("\\t");
            titles.add(new Entry(row[0], Float.parseFloat(row[1])));
            line = br.readLine();
        }

        br.close();

        log.info("read {} lines", titles.size());

        UniformRandomProvider rng = RandomSource.create(RandomSource.MT_64);
        ContinuousSampler iat = new AhrensDieterExponentialSampler(rng, 1/rate);

        ServerSocket ss = new ServerSocket(12345);
        while(true) {
            log.info("waiting for connection on {}", ss.getLocalSocketAddress());
            try (Socket s = ss.accept()) {
                log.info("new connection from {}", s.getRemoteSocketAddress());

                // kill the conneciton if something is received
                new Thread(()->{
                    try {
                        s.getInputStream().read();
                    } catch(Exception e) {
                        // don't care
                    }
                    try {
                        s.close();
                    } catch (IOException e) {
                        // don't care
                    }
                }).start();

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

                while(!s.isClosed()) {
                    Thread.sleep((long) (iat.sample()*1000*60));

                    Entry entry = titles.get(rng.nextInt(titles.size()));
                    int rating = (int) (Math.floor(Math.min(10.0, rng.nextFloat()*entry.rating*2.0)));
                    pw.println(entry.id+"\t"+rating);
                    pw.flush();
                }
            } catch(Exception e) {
                // don't care
            }
            log.info("connection closed");
        }
    }
}
