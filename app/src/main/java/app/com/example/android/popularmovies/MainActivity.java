package app.com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String pageNumber;
    String popularity;
    String rated;
    String year;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageNumber = "1";
        popularity = getString(R.string.sort_by_query1);
        rated = getString(R.string.sort_by_query2);
        choice = popularity;
        year = "2016";

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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
