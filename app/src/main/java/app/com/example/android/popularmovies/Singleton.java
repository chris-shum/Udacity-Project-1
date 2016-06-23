package app.com.example.android.popularmovies;

import java.util.ArrayList;

/**
 * Created by ShowMe on 6/23/16.
 */
public class Singleton {
    ArrayList<MovieInfoObject> movieInfoObjectsArrayList;

    public Singleton() {
        if (movieInfoObjectsArrayList == null) {
            movieInfoObjectsArrayList = new ArrayList<>();
        }
    }

    public static Singleton singleton;

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public ArrayList<MovieInfoObject> getMovieInfoObjectsArrayList() {
        return movieInfoObjectsArrayList;
    }
}
