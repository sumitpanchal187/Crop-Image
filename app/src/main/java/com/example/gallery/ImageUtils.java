package com.example.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    public static void saveBitmapToGallery(Context context, Bitmap bitmap, String title, String description) {
        String galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = title + "_" + timeStamp + ".jpg";

        // Create a file with the unique filename
        File imageFile = new File(galleryPath, fileName);

        try {
            OutputStream fos = new FileOutputStream(imageFile);
            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            // Add the image to the gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());

            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Notify the gallery about the new image
            MediaScannerConnection.scanFile(context,
                    new String[]{imageFile.toString()},
                    null,
                    (path, uri) -> {
                        // Image added to the gallery
                        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
}
