package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;  // Import the Log class

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
    private static final int ART_DETAIL_REQUEST_CODE = 100;

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
        // Load art data from JSON file
        List<Art> allArtList = ArtDataLoader.loadArtData(getContext());

        // Initialize the lists
        popularArtList = new ArrayList<>();
        featuredArtList = new ArrayList<>();
        newArtList = new ArrayList<>();

        // Log the size of the loaded art list to debug
        Log.d("HomeFragment", "Total art items loaded: " + allArtList.size());

        // Filter the art items into appropriate lists
        for (Art art : allArtList) {
            String category = art.getCategory();

            if (category != null) {
                switch (category) {
                    case "popular":
                        popularArtList.add(art);
                        break;
                    case "featured":
                        featuredArtList.add(art);
                        break;
                    case "new":
                        newArtList.add(art);
                        break;
                    default:
                        Log.d("HomeFragment", "Unknown category: " + category);
                        break;
                }
            } else {
                Log.d("HomeFragment", "Art category is null for art: " + art);
            }
        }

        // Log the sizes of the filtered lists to check if they are being populated
        Log.d("HomeFragment", "Popular art items: " + popularArtList.size());
        Log.d("HomeFragment", "Featured art items: " + featuredArtList.size());
        Log.d("HomeFragment", "New art items: " + newArtList.size());
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
        intent.putExtra("favorited", art.isFavorited());
        intent.putExtra("id", art.getId());
        startActivityForResult(intent, ART_DETAIL_REQUEST_CODE);  // Start activity for result
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ART_DETAIL_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                // Retrieve the updated favorited status
                int updatedArtId = data.getIntExtra("id", -1);
                boolean updatedFavoritedStatus = data.getBooleanExtra("favorited", false);

                // Update the art list with the new favorited status
                updateArtFavoritedStatus(updatedArtId, updatedFavoritedStatus);
            }
        }
    }

    private void updateArtFavoritedStatus(int artId, boolean isFavorited) {
        // Update the favorited status for the art in the list
        for (Art art : popularArtList) {
            if (art.getId() == artId) {
                art.setFavorited(isFavorited);
                break;
            }
        }

        for (Art art : featuredArtList) {
            if (art.getId() == artId) {
                art.setFavorited(isFavorited);
                break;
            }
        }

        for (Art art : newArtList) {
            if (art.getId() == artId) {
                art.setFavorited(isFavorited);
                break;
            }
        }

        // Notify the adapters that the data has changed
        recyclerViewPopular.getAdapter().notifyDataSetChanged();
        recyclerViewFeatured.getAdapter().notifyDataSetChanged();
        recyclerViewNew.getAdapter().notifyDataSetChanged();
    }
}
