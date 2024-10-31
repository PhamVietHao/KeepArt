package lets.example.keepart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ArtActivity extends AppCompatActivity {

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

        // Find views
        ImageView artImageView = findViewById(R.id.artImageView);
        TextView artTitleTextView = findViewById(R.id.artTitleTextView);
        TextView artDescriptionTextView = findViewById(R.id.artDescriptionTextView);
        TextView artPriceTextView = findViewById(R.id.artPriceTextView);
        TextView artLikesTextView = findViewById(R.id.artLikesTextView);

        // Set data to views
        artImageView.setImageResource(imageResId);
        artTitleTextView.setText(title);
        artDescriptionTextView.setText(description);
        artPriceTextView.setText("Price: $" + price);
        artLikesTextView.setText("Likes: " + like);
    }
}
