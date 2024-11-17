package lets.example.keepart;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUtils.copyDataJsonIfNeeded(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavbar = findViewById(R.id.bottomNav);
        bottomNavbar.setSelectedItemId(R.id.home);
        bottomNavbar.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectedFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                Fragment selectedFragment;

                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.favorite) {
                    selectedFragment = new FavoriteFragment();
                } else if (itemId == R.id.profile) {
                    selectedFragment = new ProfileFragment();
                } else {
                    selectedFragment = new HomeFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectedFragment).commit();
                return true;
            };

    @Override
    public void onBackPressed() {
        // Show a confirmation dialog when back is pressed
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to leave the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Exit the app
                    finishAffinity(); // Close all activities and exit
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog and stay in the app
                    dialog.dismiss();
                })
                .setOnCancelListener(dialog -> super.onBackPressed()) // Handle "back" on dialog
                .show();
    }

}
