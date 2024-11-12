package lets.example.keepart;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    // Method to copy the data from assets if the file does not exist
    public static void copyDataJsonIfNeeded(Context context) {
        try {
            // Check if the file already exists in internal storage
            FileInputStream fis = context.openFileInput("data_internal.json");
            fis.close();
            Log.d("FileUtils", "data_internal.json already exists.");
        } catch (FileNotFoundException e) {
            // File does not exist, so copy it from assets
            try {
                InputStream inputStream = context.getAssets().open("data_internal.json");
                FileOutputStream fos = context.openFileOutput("data_internal.json", Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                inputStream.close();
                Log.d("FileUtils", "data_internal.json copied from assets.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Static method to update the favorite status
    public static void updateFavoriteStatus(Context context, int artId, boolean newFavoritedStatus) {
        try {
            JSONArray artData = loadArtDataFromFile(context);
            boolean updated = false;

            for (int i = 0; i < artData.length(); i++) {
                JSONObject art = artData.getJSONObject(i);
                if (art.getInt("id") == artId) {
                    art.put("favorited", newFavoritedStatus);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                saveArtDataToFile(context, artData);
                Log.d("FileUtils", "Favorite status updated for artId: " + artId);
            } else {
                Log.d("FileUtils", "Art with ID: " + artId + " not found.");
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray loadArtDataFromFile(Context context) throws IOException, JSONException {
        FileInputStream fis = context.openFileInput("data_internal.json");
        int character;
        StringBuilder stringBuilder = new StringBuilder();
        while ((character = fis.read()) != -1) {
            stringBuilder.append((char) character);
        }
        fis.close();
        Log.d("FileUtils", "Loaded JSON data: " + stringBuilder.toString());
        return new JSONArray(stringBuilder.toString());
    }

    private static void saveArtDataToFile(Context context, JSONArray jsonArray) throws IOException {
        FileOutputStream fos = context.openFileOutput("data_internal.json", Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
        Log.d("FileUtils", "Data saved to data_internal.json: " + jsonArray.toString());
    }


}
