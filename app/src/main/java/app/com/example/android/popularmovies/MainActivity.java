package app.com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String pageNumber;
    String popularity;
    String rated;
    String year;
    String choice;
    Singleton mSingleton;
    TextView textView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageNumber = "1";
        popularity = getString(R.string.sort_by_query1);
        rated = getString(R.string.sort_by_query2);
        choice = popularity;
        year = "2016";
        mSingleton = Singleton.getInstance();

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(getString(R.string.base_url))
                .appendPath(getString(R.string.path1))
                .appendPath(getString(R.string.path2))
                .appendPath(getString(R.string.path3))
                .appendQueryParameter(getString(R.string.API_query), getString(R.string.API_KEY))
                .appendQueryParameter(getString(R.string.sort_by_query), choice)
                .appendQueryParameter(getString(R.string.sort_by_year_query), year)
                .appendQueryParameter(getString(R.string.page_query), pageNumber);

        String myUrl = builder.build().toString();
        getMoviesAsyncTask getMoviesAsyncTask = new getMoviesAsyncTask();
        getMoviesAsyncTask.execute(myUrl);

    }

    public class getMoviesAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String data = "";
            mSingleton.movieInfoObjectsArrayList.clear();
            try {
                URL url = new URL(params[0]);
                Log.d("hmm",params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                data = getInputData(inputStream);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonResults = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject movieData = jsonResults.getJSONObject(i);
                    String originalTitle = movieData.optString("original_title");
                    String posterPath = movieData.optString("poster_path");
                    String fullPosterPath = getString(R.string.poster_url) + posterPath;
                    if (posterPath == null) {
                        fullPosterPath = getString(R.string.no_poster_url);
                    }
                    String overview = movieData.optString("overview");
                    String voteAverage = movieData.optString("vote_average");
                    String releaseDate = movieData.optString("release_date");
                    mSingleton.movieInfoObjectsArrayList.add(new MovieInfoObject(originalTitle, fullPosterPath, overview, voteAverage, releaseDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText(mSingleton.movieInfoObjectsArrayList.get(1).originalTitle);
            Picasso.with(getBaseContext()).load(mSingleton.movieInfoObjectsArrayList.get(1).posterPath).fit().into(imageView);
        }
    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String data;
        while ((data = reader.readLine()) != null) {
            builder.append(data);
        }
        reader.close();
        return builder.toString();
    }
}
