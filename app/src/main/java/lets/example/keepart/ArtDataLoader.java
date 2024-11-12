package lets.example.keepart;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ArtDataLoader {

    public static List<Art> loadArtData(Context context) {
        List<Art> artList = new ArrayList<>();
        try {
            // Load `data_internal.json` from internal storage
            FileInputStream inputStream = context.openFileInput("data_internal.json");

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON data
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                int imageResId = context.getResources().getIdentifier(
                        jsonObject.getString("imageResId"), "drawable", context.getPackageName());
                String description = jsonObject.getString("description");
                int price = jsonObject.getInt("price");
                int like = jsonObject.getInt("like");
                boolean favorited = jsonObject.getBoolean("favorited");
                String category = jsonObject.getString("category");

                Art art = new Art(id, title, imageResId, like, description, price, favorited, category);
                artList.add(art);
            }
        } catch (Exception e) {
            Log.e("ArtDataLoader", "Error reading JSON data", e);
        }
        return artList;
    }
}
