package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPopular, recyclerViewFeatured, recyclerViewNew;
    private List<Art> popularArtList, featuredArtList, newArtList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        recyclerViewFeatured = view.findViewById(R.id.recyclerViewFeatured);
        recyclerViewNew = view.findViewById(R.id.recyclerViewNew);

        // Set up horizontal layout managers
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFeatured.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize art lists and adapters
        populateArtLists();
        setupAdapters();

        return view;
    }

    private void populateArtLists() {
        // Sample data for each section
        popularArtList = new ArrayList<>();
        featuredArtList = new ArrayList<>();
        newArtList = new ArrayList<>();

        // Populate with sample data
        popularArtList.add(new Art("Starry Night", R.drawable.starry_night, 100, "A famous painting by Vincent van Gogh.", 200, false));
    }

    private void setupAdapters() {
        // Set adapters with data and click listener
        ArtAdapter popularAdapter = new ArtAdapter(popularArtList, getContext(), this::openArtDetailActivity);
        ArtAdapter featuredAdapter = new ArtAdapter(featuredArtList, getContext(), this::openArtDetailActivity);
        ArtAdapter newAdapter = new ArtAdapter(newArtList, getContext(), this::openArtDetailActivity);

        recyclerViewPopular.setAdapter(popularAdapter);
        recyclerViewFeatured.setAdapter(featuredAdapter);
        recyclerViewNew.setAdapter(newAdapter);
    }

    private void openArtDetailActivity(Art art) {
        Intent intent = new Intent(getContext(), ArtActivity.class);
        intent.putExtra("title", art.getTitle());
        intent.putExtra("imageResId", art.getImageResId());
        intent.putExtra("description", art.getDescription());
        intent.putExtra("price", art.getPrice());
        intent.putExtra("like", art.getLike());
        startActivity(intent);
    }
}
