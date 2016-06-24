package app.com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    private GridLayoutManager gridLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Spinner spinner;
    Button leftButton;
    Button rightButton;
    EditText pageNumberEditText;
//    EditText yearEditText;


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
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        pageNumberEditText = (EditText) findViewById(R.id.pageNumberEditText);
//        yearEditText = (EditText) findViewById(R.id.yearEditText);

        spinner = (Spinner) findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (selection.equals("Most Popular")) {
                    choice = getString(R.string.sort_by_query1);
                } else {
                    choice = getString(R.string.sort_by_query2);
                }
                urlBuilderAndCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageNumberTemp = pageNumberEditText.getText().toString();
                int pageNumberInt = Integer.valueOf(pageNumberTemp);
                if (pageNumberInt < 2) {
                    pageNumber = "1";
                    pageNumberEditText.setText(pageNumber);
                } else {
                    pageNumber = String.valueOf((pageNumberInt - 1));
                    pageNumberEditText.setText(pageNumber);
                }

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageNumberTemp = pageNumberEditText.getText().toString();
                int pageNumberInt = Integer.valueOf(pageNumberTemp);
                pageNumber = String.valueOf((pageNumberInt + 1));
                pageNumberEditText.setText(pageNumber);
            }
        });

//        yearEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (yearEditText.getText().toString().equals("")) {
//                    year = "2016";}
//                else if (Integer.valueOf(yearEditText.getText().toString()) >= 1887 && Integer.valueOf(yearEditText.getText().toString()) <= 2016) {
//                    year = yearEditText.getText().toString();
//                    urlBuilderAndCall();
//                } else {
//                    Toast.makeText(MainActivity.this, "Please enter a year between 1887 and 2016.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        pageNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pageNumberEditText.getText().toString().equals("")) {
                    pageNumber = "1";
                }else if (Integer.valueOf(pageNumberEditText.getText().toString()) >= 1) {
                    pageNumber = pageNumberEditText.getText().toString();
                }
                urlBuilderAndCall();
            }
        });


        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(mSingleton.getMovieInfoObjectsArrayList());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public class getMoviesAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String data = "";
            mSingleton.movieInfoObjectsArrayList.clear();
            try {
                URL url = new URL(params[0]);
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
                    if (posterPath.equals("null")) {
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
            recyclerViewAdapter.notifyDataSetChanged();
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

    public void urlBuilderAndCall() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(getString(R.string.base_url))
                .appendPath(getString(R.string.path1))
//                .appendPath(getString(R.string.path2))
                .appendPath(getString(R.string.path3))
                .appendPath(choice)
                .appendQueryParameter(getString(R.string.API_query), getString(R.string.API_KEY))
//                .appendQueryParameter(getString(R.string.sort_by_query), choice)
//                .appendQueryParameter(getString(R.string.sort_by_year_query), year)
                .appendQueryParameter(getString(R.string.page_query), pageNumber);

        String myUrl = builder.build().toString();
        getMoviesAsyncTask getMoviesAsyncTask = new getMoviesAsyncTask();
        getMoviesAsyncTask.execute(myUrl);
    }


}
