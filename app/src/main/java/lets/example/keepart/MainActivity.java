package lets.example.keepart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import lets.example.keepart.HomeFragment;
import lets.example.keepart.FavoriteFragment;
import lets.example.keepart.ProfileFragment;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                selectedFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;

                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.favorite) {
                    selectedFragment = new FavoriteFragment();
                } else if (itemId == R.id.profile) {
                    // Handle the profile case
                    selectedFragment = new ProfileFragment();

                } else {
                    selectedFragment = new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, selectedFragment).commit();
                return true;
            };

}
