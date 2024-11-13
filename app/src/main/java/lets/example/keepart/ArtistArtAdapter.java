package lets.example.keepart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArtistArtAdapter extends RecyclerView.Adapter<ArtistArtAdapter.ArtistArtViewHolder> {

    private List<Art> artistArtList;
    private Context context;

    public ArtistArtAdapter(List<Art> artistArtList, Context context) {
        this.artistArtList = artistArtList;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_art, parent, false);
        return new ArtistArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistArtViewHolder holder, int position) {
        Art art = artistArtList.get(position);
        holder.artImage.setImageResource(art.getImageResId());
    }

    @Override
    public int getItemCount() {
        return artistArtList.size();
    }

    public static class ArtistArtViewHolder extends RecyclerView.ViewHolder {
        ImageView artImage;

        public ArtistArtViewHolder(@NonNull View itemView) {
            super(itemView);
            artImage = itemView.findViewById(R.id.artImage);
        }
    }

    public void updateArtistArtList(List<Art> newArtList) {
        artistArtList = newArtList;
        notifyDataSetChanged();
    }
}
