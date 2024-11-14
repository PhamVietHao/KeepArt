package lets.example.keepart;
import android.widget.ImageButton;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder; // Import for Material dialog

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPopular, recyclerViewFeatured, recyclerViewNew;
    private List<Art> popularArtList, featuredArtList, newArtList;
    private boolean showPopular = true;
    private boolean showFeatured = true;
    private boolean showNew = true;
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

        // Initialize the filter button
        view.findViewById(R.id.filter_button).setOnClickListener(v -> openFilterDialog());

        return view;
    }

    private void populateArtLists() {
        List<Art> allArtList = ArtDataLoader.loadArtData(getContext());

        popularArtList = new ArrayList<>();
        featuredArtList = new ArrayList<>();
        newArtList = new ArrayList<>();

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
                }
            }
        }
    }

    private void setupAdapters() {
        ArtAdapter popularAdapter = new ArtAdapter(popularArtList, getContext(), this::openArtDetailActivity);
        ArtAdapter featuredAdapter = new ArtAdapter(featuredArtList, getContext(), this::openArtDetailActivity);
        ArtAdapter newAdapter = new ArtAdapter(newArtList, getContext(), this::openArtDetailActivity);

        recyclerViewPopular.setAdapter(popularAdapter);
        recyclerViewFeatured.setAdapter(featuredAdapter);
        recyclerViewNew.setAdapter(newAdapter);

        // Apply the initial filter settings
        applyFilter();
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
        intent.putExtra("artist", art.getArtist());
        startActivityForResult(intent, ART_DETAIL_REQUEST_CODE);
    }

    private void updateArtFavoritedStatus(int artId, boolean isFavorited) {
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

        recyclerViewPopular.getAdapter().notifyDataSetChanged();
        recyclerViewFeatured.getAdapter().notifyDataSetChanged();
        recyclerViewNew.getAdapter().notifyDataSetChanged();
    }

    private void openFilterDialog() {
        // Inflate the filter dialog view
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog, null);

        // Find the CheckBoxes in the dialog layout
        CheckBox checkboxPopular = dialogView.findViewById(R.id.checkbox_popular);
        CheckBox checkboxFeatured = dialogView.findViewById(R.id.checkbox_featured);
        CheckBox checkboxNew = dialogView.findViewById(R.id.checkbox_new);

        // Set the current filter settings to the CheckBoxes
        checkboxPopular.setChecked(showPopular);
        checkboxFeatured.setChecked(showFeatured);
        checkboxNew.setChecked(showNew);

        // Create and show the custom dialog without a Cancel button
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setView(dialogView);  // No negative button

        // Create the dialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        // Find the custom "Confirm Filter" button
        Button confirmButton = dialogView.findViewById(R.id.btn_confirm_filter);

        // Set the listener for the "Confirm Filter" button
        confirmButton.setOnClickListener(v -> {
            // Get the current state of the checkboxes and update the filter settings
            showPopular = checkboxPopular.isChecked();
            showFeatured = checkboxFeatured.isChecked();
            showNew = checkboxNew.isChecked();

            // Apply the filter settings
            applyFilter();

            // Close the dialog after applying the filter
            dialog.dismiss();
        });

        // Find the close button (X icon)
        ImageButton closeButton = dialogView.findViewById(R.id.btn_close_filter);

        // Set the listener for the close button
        closeButton.setOnClickListener(v -> {
            // Dismiss the dialog when the close button is clicked
            dialog.dismiss();
        });

        // Set the sliding animation for the dialog
        dialog.getWindow().setWindowAnimations(R.style.DialogSlideInFromBottom);
        dialog.show();
    }




    private void applyFilter() {
        recyclerViewPopular.setVisibility(showPopular ? View.VISIBLE : View.GONE);
        recyclerViewFeatured.setVisibility(showFeatured ? View.VISIBLE : View.GONE);
        recyclerViewNew.setVisibility(showNew ? View.VISIBLE : View.GONE);
    }
}
