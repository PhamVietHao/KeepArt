package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;

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

        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFeatured.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        view.findViewById(R.id.filter_button).setOnClickListener(v -> openFilterDialog());

        // Set up onClickListener for artist_image_1
        ImageView artistImage1 = view.findViewById(R.id.artist_image_1);
        artistImage1.setOnClickListener(v -> openKhoaProfile());
        ImageView artistImage2 = view.findViewById(R.id.artist_image_2);
        artistImage2.setOnClickListener(v -> openNamProfile());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when HomeFragment becomes visible
        populateArtLists();
        setupAdapters();
    }

    private void populateArtLists() {
        List<Art> allArtList = ArtDataLoader.loadArtData(getContext());

        popularArtList = new ArrayList<>();
        featuredArtList = new ArrayList<>();
        newArtList = new ArrayList<>();

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
        intent.putExtra("lastBidDate", art.getLastBidDate());

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
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog, null);

        CheckBox checkboxPopular = dialogView.findViewById(R.id.checkbox_popular);
        CheckBox checkboxFeatured = dialogView.findViewById(R.id.checkbox_featured);
        CheckBox checkboxNew = dialogView.findViewById(R.id.checkbox_new);

        checkboxPopular.setChecked(showPopular);
        checkboxFeatured.setChecked(showFeatured);
        checkboxNew.setChecked(showNew);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm_filter);
        confirmButton.setOnClickListener(v -> {
            showPopular = checkboxPopular.isChecked();
            showFeatured = checkboxFeatured.isChecked();
            showNew = checkboxNew.isChecked();
            applyFilter();
            dialog.dismiss();
        });

        ImageButton closeButton = dialogView.findViewById(R.id.btn_close_filter);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setWindowAnimations(R.style.DialogSlideInFromBottom);
        dialog.show();
    }

    private void applyFilter() {
        recyclerViewPopular.setVisibility(showPopular ? View.VISIBLE : View.GONE);
        recyclerViewFeatured.setVisibility(showFeatured ? View.VISIBLE : View.GONE);
        recyclerViewNew.setVisibility(showNew ? View.VISIBLE : View.GONE);
    }

    private void openKhoaProfile() {
        // Create an Intent to open the KhoaProfile fragment
        Intent intent = new Intent(getContext(), KhoaProfile.class);
        // Passing artist's name
        intent.putExtra("artist_name", "Do Dang Khoa");  // You can dynamically set this
        startActivity(intent);
    }

    private void openNamProfile() {
        // Create an Intent to open the KhoaProfile fragment
        Intent intent = new Intent(getContext(), NamProfile.class);
        // Passing artist's name
        intent.putExtra("artist_name", "Nguyen Dong Nam");  // You can dynamically set this
        startActivity(intent);
    }
}
