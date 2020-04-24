import java.io.IOException;

public class JobStarter {

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        long start, end;

        System.out.println("Starting Loading movies data...");
        start = System.currentTimeMillis();
        LoadMovieData.load_movies_data();
        end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end-start));

        System.out.println("Starting Loading actor data...");
        start = System.currentTimeMillis();
        LoadActorData.load_actors_data();
        end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end-start));


        System.out.println("Starting Loading n movies for each actor...");
        start = System.currentTimeMillis();
        LoadMoviesActorData.load_actors_movies_data();
        end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end-start));

        System.out.println("Starting Loading top 3 movies for each actor...");
        start = System.currentTimeMillis();
        long[] times = LoadTop3MoviesActor.load_top_3_movies();
        end = System.currentTimeMillis();
        System.out.println("Time passed: " + (end-start));
        System.out.println("Job1 -> " + times[0]);
        System.out.println("Job2 -> " + times[1]);
    }
}
