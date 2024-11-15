package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class ArtActivity extends AppCompatActivity {

    private boolean isFavorite; // Track if the art is marked as favorite
    private ImageButton favoriteButton;
    private int artId; // To identify the art for updating its status
    private int currentPrice; // Track the current price for bidding
    private TextView lastBidAmountTextView;
    private TextView lastBidDateTextView; // Added TextView for displaying the last bid date
    private EditText bidEditText; // Added EditText to enter new bid
    private Button bidButton; // The bid button for submitting the new bid
    private TextView biddingClosedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);

        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");


        artId = intent.getIntExtra("id", -1);

        int imageResId = intent.getIntExtra("imageResId", 0);
        String description = intent.getStringExtra("description");
        currentPrice = intent.getIntExtra("price", 0); // This is the price (last bid amount)
        String artist = intent.getStringExtra("artist");
        String lastBidDate = intent.getStringExtra("lastBidDate"); // Extract the last bid date
        isFavorite = intent.getBooleanExtra("favorited", false);
        // Find views
        ImageView artImageView = findViewById(R.id.artImageView);
        TextView artTitleTextView = findViewById(R.id.artTitleTextView);
        TextView artDescriptionTextView = findViewById(R.id.artDescriptionTextView);
        TextView artLikesTextView = findViewById(R.id.artLikesTextView);
        TextView artArtistTextView = findViewById(R.id.artArtistTextView);
        lastBidAmountTextView = findViewById(R.id.lastBidAmount); // This shows the last bid amount
        lastBidDateTextView = findViewById(R.id.lastBidDateTextView); // Initialize the TextView for last bid date
        favoriteButton = findViewById(R.id.favoriteButton);
        ImageButton backButton = findViewById(R.id.backButton);
        bidEditText = findViewById(R.id.bidEditText); // The EditText for user input
        ImageButton bidButton = findViewById(R.id.bidButton); // The Button for placing the bid
        biddingClosedMessage = findViewById(R.id.biddingClosedMessage); // The message for bidding closure

        // Set data to views
        artImageView.setImageResource(imageResId);
        artTitleTextView.setText(title);
        artDescriptionTextView.setText(description);
        artLikesTextView.setText(String.valueOf(intent.getIntExtra("like", 0)));
        artArtistTextView.setText("Artist: " + artist);

        // Set the last bid amount (price)
        lastBidAmountTextView.setText("$" + currentPrice); // Set price as last bid amount

        updateFavoriteIcon();
        // Set the last bid date (with proper formatting)
        setLastBidDate(lastBidDate);

        // Set the back button to finish the activity and send the result
        backButton.setOnClickListener(v -> sendResultAndFinish());

        // Set the favorite button to toggle favorite status
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            updateFavoriteIcon();
            saveFavoriteStatusToFile(); // Save the updated favorite status to data_internal.json
        });

        // Set up bidding logic
        bidButton.setOnClickListener(v -> handleBid());

        // Check if bidding is over
        if (isBiddingOver(lastBidDate)) { // Now passing lastBidDate as an argument
            biddingClosedMessage.setVisibility(View.VISIBLE); // Show the message if bidding is closed
            bidButton.setVisibility(View.GONE); // Hide the bid button when bidding is closed
            bidEditText.setVisibility(View.GONE); // Hide the bid input field when bidding is closed
        } else {
            biddingClosedMessage.setVisibility(View.INVISIBLE); // Hide the message if bidding is still open
            bidButton.setVisibility(View.VISIBLE); // Show the bid button
            bidEditText.setVisibility(View.VISIBLE); // Show the bid input field
        }
    }



    private boolean isBiddingOver(String lastBidDate) {
        Date currentDate = new Date(); // Get the current date and time

        try {
            // Assuming the lastBidDate is in the format 'yyyy-MM-dd'
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedLastBidDate = inputFormat.parse(lastBidDate); // Parse the string into a Date object

            // Compare current date with the parsed last bid date
            return currentDate.after(parsedLastBidDate); // Return true if current date is after the last bid date
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if there is an error (e.g., parsing issue)
        }
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
        FileOutputStream fos = openFileOutput("data_internal.json", MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }

    private void setLastBidDate(String lastBidDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(lastBidDate);
            String formattedDate = outputFormat.format(date);

            // Construct the full text to be displayed
            String fullText = "Last Bid Date: " + formattedDate;

            // Create a SpannableString
            SpannableString spannableString = new SpannableString(fullText);

            // Apply bold style only to the date portion
            int start = fullText.indexOf(formattedDate);
            int end = start + formattedDate.length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the text to the TextView
            lastBidDateTextView.setText(spannableString);

        } catch (Exception e) {
            e.printStackTrace();
            lastBidDateTextView.setText("Invalid Date");
        }
    }

    private void handleBid() {
        // Get the last bid date from the Intent (or any other data source)
        String lastBidDate = getIntent().getStringExtra("lastBidDate");

        // Check if bidding is over
        if (isBiddingOver(lastBidDate)) { // Pass lastBidDate to isBiddingOver()
            Toast.makeText(this, "Bidding has closed", Toast.LENGTH_SHORT).show();
            return; // Prevent placing a bid if the bidding is over
        }

        String bidInput = bidEditText.getText().toString();
        if (bidInput.isEmpty()) {
            Toast.makeText(this, "Please enter a bid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int newBid = Integer.parseInt(bidInput);
        if (newBid <= currentPrice) {
            Toast.makeText(this, "Bid must be higher than the current price", Toast.LENGTH_SHORT).show();
        } else {
            currentPrice = newBid;
            lastBidAmountTextView.setText("$" + currentPrice); // Update the displayed last bid amount
            Toast.makeText(this, "Bid placed successfully", Toast.LENGTH_SHORT).show();
            saveBidToFile(newBid); // Optionally save the new bid to file or database
        }
    }


    private void saveBidToFile(int newBid) {
        try {
            JSONArray jsonArray = loadArtDataFromFile();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("id") == artId) {
                    jsonObject.put("price", newBid); // Update the price to the new bid
                    break;
                }
            }
            saveArtDataToFile(jsonArray);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

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
