package lets.example.keepart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Art> favoriteArtList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.favoriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteArtList = new ArrayList<>();

        // Load the art data and filter for favorited items
        loadArtData();

        return view;
    }

    private void loadArtData() {
        // Load art data using the ArtDataLoader
        List<Art> allArtList = ArtDataLoader.loadArtData(getContext());

        // Filter out the art pieces that are marked as 'favorited'
        for (Art art : allArtList) {
            if (art.isFavorited()) {
                favoriteArtList.add(art);
            }
        }

        // Update the RecyclerView with the filtered favorite art list
        favoriteAdapter = new FavoriteAdapter(favoriteArtList);
        recyclerView.setAdapter(favoriteAdapter);
    }
}
