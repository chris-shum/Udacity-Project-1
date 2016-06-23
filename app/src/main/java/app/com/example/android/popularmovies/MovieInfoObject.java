package app.com.example.android.popularmovies;

/**
 * Created by ShowMe on 6/23/16.
 */
public class MovieInfoObject {

    String originalTitle;
    String posterPath;
    String overview;
    String voteAverage;
    String releaseDate;

    public MovieInfoObject(String originalTitle, String posterPath, String overview, String voteAverage, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
