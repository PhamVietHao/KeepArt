package lets.example.keepart;

import android.os.Bundle;
import android.util.Log;
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

public class ProfileFragment extends Fragment {

    private List<Art> artList = new ArrayList<>();
    private ArtistArtAdapter artistArtAdapter;
    private RecyclerView artRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize the RecyclerView
        artRecyclerView = view.findViewById(R.id.artRecyclerView);

        // Set the layout manager
        artRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter for the artist's art
        artistArtAdapter = new ArtistArtAdapter(new ArrayList<>(), getContext());
        artRecyclerView.setAdapter(artistArtAdapter);

        // Load all art data
        artList = ArtDataLoader.loadArtData(getContext()); // Replace with your data source or method

        // Debug: Log all the art data with artist's name
        for (Art art : artList) {
            Log.d("ProfileFragment", "Art Title: " + art.getTitle() + ", Artist: " + art.getArtist());
        }

        // Filter the art data by artist name (Pham Viet Hao)
        List<Art> artistArtList = filterByArtist(artList, "Pham Viet Hao");

        // Update the adapter with the filtered list
        artistArtAdapter.updateArtistArtList(artistArtList);

        return view;
    }

    // Filter method to get arts by artist name
    private List<Art> filterByArtist(List<Art> artList, String artistName) {
        List<Art> filteredList = new ArrayList<>();
        for (Art art : artList) {
            if (art.getArtist().equals(artistName)) {
                filteredList.add(art);
            }
        }
        return filteredList;
    }
}
