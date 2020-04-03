FROM bde2020/spark-base
COPY target/Guiao6-1.0-SNAPSHOT.jar /
ENTRYPOINT ["/spark/bin/spark-submit", "--class", "Ex1", \
            "--master", "spark://spark-master:7077","/Guiao6-1.0-SNAPSHOT.jar"]