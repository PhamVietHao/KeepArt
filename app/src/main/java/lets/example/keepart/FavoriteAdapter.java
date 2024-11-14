package lets.example.keepart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Art> favoriteArtList;

    // Constructor to pass the list of favorite art items
    public FavoriteAdapter(List<Art> favoriteArtList) {
        this.favoriteArtList = favoriteArtList;

        // Log the number of items in the list
        Log.d("FavoriteAdapter", "Adapter initialized with " + favoriteArtList.size() + " favorite items.");
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_art, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Art art = favoriteArtList.get(position);

        // Log the data for each art item as it is being bound to the view
        Log.d("FavoriteAdapter", "Binding data for art item at position " + position + ": " +
                "Title: " + art.getTitle() + ", Artist: " + art.getArtist() + ", Last Bid: $" + art.getPrice());

        // Set the art data to the views
        holder.artImage.setImageResource(art.getImageResId());
        holder.artTitle.setText(art.getTitle());
        holder.artArtist.setText(art.getArtist());
        holder.lastBid.setText("Last Bid: $" + art.getPrice());
    }

    @Override
    public int getItemCount() {
        // Log the number of items in the list each time the item count is fetched
        Log.d("FavoriteAdapter", "Item count: " + favoriteArtList.size());
        return favoriteArtList.size();
    }

    // ViewHolder class to hold references to the views in the item
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView artImage;
        TextView artTitle;
        TextView artArtist;
        TextView lastBid;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            artImage = itemView.findViewById(R.id.artImage);
            artTitle = itemView.findViewById(R.id.artTitle);
            artArtist = itemView.findViewById(R.id.artArtist);
            lastBid = itemView.findViewById(R.id.lastBid);
        }
    }
}
