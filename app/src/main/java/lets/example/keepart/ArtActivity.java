package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArtActivity extends AppCompatActivity {

    private boolean isFavorite; // Track if the art is marked as favorite
    private ImageButton favoriteButton;
    private int artId; // To identify the art for updating its status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);

        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int imageResId = intent.getIntExtra("imageResId", 0);
        String description = intent.getStringExtra("description");
        int price = intent.getIntExtra("price", 0);
        int like = intent.getIntExtra("like", 0);
        artId = intent.getIntExtra("id", -1);
        isFavorite = intent.getBooleanExtra("favorited", false);
        String artist = intent.getStringExtra("artist");
        Log.d("ArtActivity", "Received favorited status: " + isFavorite);

        // Find views
        ImageView artImageView = findViewById(R.id.artImageView);
        TextView artTitleTextView = findViewById(R.id.artTitleTextView);
        TextView artDescriptionTextView = findViewById(R.id.artDescriptionTextView);
        TextView artLikesTextView = findViewById(R.id.artLikesTextView);
        TextView artArtistTextView = findViewById(R.id.artArtistTextView);
        TextView lastBidAmount = findViewById(R.id.lastBidAmount);
        favoriteButton = findViewById(R.id.favoriteButton);
        ImageButton backButton = findViewById(R.id.backButton);

        // Set data to views
        artImageView.setImageResource(imageResId);
        artTitleTextView.setText(title);
        artDescriptionTextView.setText(description);
        artLikesTextView.setText(String.valueOf(like));
        artArtistTextView.setText("Artist: " + artist);
        lastBidAmount.setText("$500"); // Placeholder for last bid amount

        // Update the favorite icon based on the favorited status
        updateFavoriteIcon();

        // Set the back button to finish the activity and send the result
        backButton.setOnClickListener(v -> {
            // Before finishing, send the updated favorite status
            sendResultAndFinish();
        });

        // Set the favorite button to toggle favorite status
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            Log.d("ArtActivity", "Toggled favorite status: " + isFavorite);
            updateFavoriteIcon();
            saveFavoriteStatusToFile(); // Save the updated favorite status to data_internal.json
        });
    }

    private void updateFavoriteIcon() {
        // Update the favorite icon based on the favorited status
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_heart_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_heart_unfilled);
        }
    }

    private void saveFavoriteStatusToFile() {
        try {
            // Load the current art data from data_internal.json
            JSONArray jsonArray = loadArtDataFromFile();

            // Update the favorite status for the matching art item
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("id") == artId) {
                    jsonObject.put("favorited", isFavorite);
                    break;
                }
            }

            // Save the updated JSON array back to data_internal.json
            saveArtDataToFile(jsonArray);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadArtDataFromFile() throws IOException, JSONException {
        // Read the JSON file and return the art data as a JSONArray
        FileInputStream fis = openFileInput("data_internal.json");
        int character;
        StringBuilder stringBuilder = new StringBuilder();
        while ((character = fis.read()) != -1) {
            stringBuilder.append((char) character);
        }
        fis.close();
        return new JSONArray(stringBuilder.toString());
    }

    private void saveArtDataToFile(JSONArray jsonArray) throws IOException {
        // Save the updated art data to the JSON file
        FileOutputStream fos = openFileOutput("data_internal.json", MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }

    // This method is called when the back button is clicked to send the result
    private void sendResultAndFinish() {
        // Send the updated favorite status back to the parent activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", artId);
        resultIntent.putExtra("favorited", isFavorite);
        setResult(RESULT_OK, resultIntent);

        // Finish the activity and return to the parent
        finish();
    }
}
