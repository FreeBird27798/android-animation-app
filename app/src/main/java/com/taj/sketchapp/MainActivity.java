package com.taj.sketchapp;

import android.Manifest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvasView;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvas_view);

        // Check if WRITE_EXTERNAL_STORAGE permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_clear:
                // Clear the canvas view's canvas
                canvasView.clear();
                return true;
            case R.id.action_color:
                // Show a color picker dialog and update the canvas view's color based on the selected color
                ColorPickerDialog dialog = new ColorPickerDialog(MainActivity.this);
                dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        canvasView.setPaintColor(color);
                    }
                });
                dialog.show();
                return true;
            case R.id.action_thickness:                // Create a new SeekBarDialog
                SeekBarDialog thicknessDialog = new SeekBarDialog(MainActivity.this, "Select signature width", 1, 50, (int) canvasView.getStrokeWidth());
                // Set a listener to update the stroke width when the user changes the value on the SeekBar
                thicknessDialog.setOnSeekBarChangeListener(new SeekBarDialog.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update the stroke width of the canvas view
                        canvasView.setStrokeWidth(progress);
                    }
                });
                // Set a listener to handle the "OK" button
                thicknessDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        canvasView.setStrokeWidth(which);
                    }
                });
                // Set a listener to handle the "Cancel" button
                thicknessDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restore the previous stroke width of the canvas view
                        canvasView.setStrokeWidth(canvasView.getPreviousStrokeWidth());
                    }
                });
                // Show the SeekBarDialog
                thicknessDialog.show();// Handle thickness action
                return true;
            case R.id.action_save:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Save Signature");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Enter filename");
                builder.setView(input);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save the signature as a PNG file
                        String filename = input.getText().toString();
                        if (!TextUtils.isEmpty(filename)) {
                            Bitmap bitmap = canvasView.getBitmap();

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            OutputStream fos = null;
                            try {
                                fos = getContentResolver().openOutputStream(uri);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                Toast.makeText(MainActivity.this, "Signature saved to gallery", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error saving signature", Toast.LENGTH_LONG).show();
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter a filename", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
