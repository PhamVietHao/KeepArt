package lets.example.keepart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtViewHolder> {

    private List<Art> artList;
    private Context context;
    private OnArtClickListener listener;

    public ArtAdapter(List<Art> artList, Context context, OnArtClickListener listener) {
        this.artList = artList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_art, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        Art art = artList.get(position);
        holder.artImage.setImageResource(art.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onArtClick(art);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public static class ArtViewHolder extends RecyclerView.ViewHolder {
        ImageView artImage;

        public ArtViewHolder(@NonNull View itemView) {
            super(itemView);
            artImage = itemView.findViewById(R.id.artImage);
        }
    }

    public interface OnArtClickListener {
        void onArtClick(Art art);
    }
}
