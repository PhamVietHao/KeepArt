package lets.example.keepart;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NamProfile extends AppCompatActivity {

    private List<Art> artList = new ArrayList<>();
    private ArtistArtAdapter artistArtAdapter;
    private RecyclerView artRecyclerViewNam;
    private String artistName; // Store the artist's name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nam_profile); // Set the layout for the activity

        // Retrieve the artist's name passed through the Intent
        if (getIntent() != null) {
            artistName = getIntent().getStringExtra("artist_name");
        }

        // Initialize the RecyclerView
        artRecyclerViewNam = findViewById(R.id.artRecyclerViewNam);

        // Set the layout manager to GridLayoutManager for grid-style layout with 2 columns per row
        artRecyclerViewNam.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns per row

        // Initialize the adapter for the artist's art
        artistArtAdapter = new ArtistArtAdapter(new ArrayList<>(), this);
        artRecyclerViewNam.setAdapter(artistArtAdapter);

        // Load all art data (Make sure this loads data correctly from your source)
        artList = ArtDataLoader.loadArtData(this); // Replace with your data source or method

        // Debug: Log all the art data with artist's name
        for (Art art : artList) {
            Log.d("ProfileActivity", "Art Title: " + art.getTitle() + ", Artist: " + art.getArtist());
        }

        // Filter the art data by artist name dynamically
        List<Art> artistArtList = filterByArtist(artList, artistName);

        // Update the adapter with the filtered list
        artistArtAdapter.updateArtistArtList(artistArtList);
    }

    // Filter method to get arts by artist name
    private List<Art> filterByArtist(List<Art> artList, String artistName) {
        List<Art> filteredList = new ArrayList<>();
        for (Art art : artList) {
            if (art.getArtist() != null && art.getArtist().equals(artistName)) {
                filteredList.add(art);
            }
        }
        return filteredList;
    }
}
