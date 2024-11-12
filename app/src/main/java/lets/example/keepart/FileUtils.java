package lets.example.keepart;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";

    // Method to copy the data.json from assets to the app's internal storage if it doesn't already exist
// Method to copy data_internal.json if it doesn’t exist in internal storage
    public static void copyDataJsonIfNeeded(Context context) {
        String filePath = context.getFilesDir() + "/data_internal.json";
        try {
            FileInputStream fis = context.openFileInput("data_internal.json");
            fis.close(); // If file exists, we don’t need to copy it
        } catch (IOException e) {
            // Copy from assets if it doesn't exist
            copyDataJsonFromAssets(context);
        }
    }

    private static void copyDataJsonFromAssets(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("data.json");
            FileOutputStream outputStream = context.openFileOutput("data_internal.json", Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

