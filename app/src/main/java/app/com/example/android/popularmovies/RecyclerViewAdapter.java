package app.com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ShowMe on 6/23/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    ArrayList<MovieInfoObject> movieInfoObjectArrayList;

    public RecyclerViewAdapter(ArrayList<MovieInfoObject> movieInfoObjectArrayList) {
        this.movieInfoObjectArrayList = movieInfoObjectArrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        RecyclerViewHolder rcv = new RecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.movieTitle.setText(movieInfoObjectArrayList.get(position).getOriginalTitle());
        Context context = holder.moviePoster.getContext();
        Picasso.with(context).load(movieInfoObjectArrayList.get(position).getPosterPath()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (movieInfoObjectArrayList == null) {
            return 0;
        }
        return movieInfoObjectArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView movieTitle;
        public ImageView moviePoster;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_name);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);

        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("Position", getAdapterPosition());
            context.startActivity(intent);
        }
    }
}
