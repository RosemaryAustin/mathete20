package com.example.mathete20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import android.graphics.Matrix;

public class math extends AppCompatActivity {

    private static final String TAG = "here";
    private ImageView imageView;
    private Button call_camera;
    private Button keep;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        getSupportActionBar().hide();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        imageView = findViewById(R.id.imageView);
        call_camera = findViewById(R.id.call_camera);
        keep = findViewById(R.id.keep);

        call_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
                //makes the keep button visible which covers the call_camera
                keep.setClickable(true);
                keep.setVisibility(View.VISIBLE);
            }
        });
    }

    //runs the pic just taken to the model and then sends the result in a string to equ activity
    public void sendPic(View view){
        String problem = "2+2";//tenser flow lit model goes here
        Intent intent = new Intent(this, equ.class);
        intent.putExtra("problem", problem);
        startActivity(intent);
    };

    static final int REQUEST_TAKE_PHOTO = 1;

    //This opens the camera app to take a pic
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "Error could not create photofile ");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //used to take an image
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //This allows you to view the image as a thumbnail on the screen with next
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setPic();
        }
    }

    //This is the current photos location
    String currentPhotoPath;

    //This saves the full size photo by creating a name for the file and date stamping it
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    static Bitmap blurfast(Bitmap bmp, int radius) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pix = new int[w * h];
        bmp.getPixels(pix, 0, w, 0, 0, w, h);

        for(int r = radius; r >= 1; r /= 2) {
            for(int i = r; i < h - r; i++) {
                for(int j = r; j < w - r; j++) {
                    int tl = pix[(i - r) * w + j - r];
                    int tr = pix[(i - r) * w + j + r];
                    int tc = pix[(i - r) * w + j];
                    int bl = pix[(i + r) * w + j - r];
                    int br = pix[(i + r) * w + j + r];
                    int bc = pix[(i + r) * w + j];
                    int cl = pix[i * w + j - r];
                    int cr = pix[i * w + j + r];

                    pix[(i * w) + j] = 0xFF000000 |
                            (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) + (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
                            (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00) + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
                            (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) + (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3) & 0xFF0000;
                }
            }
        }
        bmp.setPixels(pix, 0, w, 0, 0, w, h);
        return bmp;
    }

//    public int[] pixels(Bitmap bitmap) {
//        int[] pixels_array = new int[bitmap.getHeight() * bitmap.getWidth()];
//        bitmap.getPixels(pixels_array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        System.out.println(Arrays.toString(pixels_array));
//        return pixels_array;
//    };

    public static int[] mode(int[][] arr) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            // tiny change 1: proper dimensions
            for (int j = 0; j < arr[i].length; j++) {
                // tiny change 2: actually store the values
                list.add(arr[i][j]);
            }
        }

        // now you need to find a mode in the list.

        // tiny change 3, if you definitely need an array
        int[] vector = new int[list.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = list.get(i);
        }
        return vector;
    }

//    public static ColorMatrix createThresholdMatrix(int threshold) {
//        ColorMatrix matrix = new ColorMatrix(new float[] {
//                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
//                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
//                85.f, 85.f, 85.f, 0.f, -255.f * threshold,
//                0f, 0f, 0f, 1f, 0f
//        });
//        return matrix;
//    }

    static Bitmap threshy(Bitmap bitmap, int threshold) {
        for (int y = 0; y < bitmap.getHeight(); y++){
            for (int x = 0; x < bitmap.getWidth(); x++){
                int c = bitmap.getPixel(x, y);
                byte r, g, b;
                r = (byte)Color.red(c);
                g = (byte)Color.green(c);
                b = (byte)Color.blue(c);
                if (r > threshold){
                    bitmap.setPixel(x, y, Color.WHITE);
                }
                else{
                    bitmap.setPixel(x, y, Color.BLACK);
                }
            }
        }
        return bitmap;
    }

    //This resizes the pic for memory and does the image preprocessing
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

//        Bitmap rotatedBitmap = null;
//        rotatedBitmap = rotateImage(bitmap, 90);
        imageView.setRotation(90);

        Bitmap graybitmap = toGrayscale(bitmap);

        Bitmap blurbitmap = blurfast(graybitmap, 5);

        int lenarr = blurbitmap.getHeight() * blurbitmap.getWidth();
        int[][] pixels_array = new int[blurbitmap.getHeight()][ blurbitmap.getWidth()];
//        blurbitmap.getPixels(pixels_array, 0, blurbitmap.getWidth(), 0, 0, blurbitmap.getWidth(), blurbitmap.getHeight());
//
        for (int y = 0; y < blurbitmap.getHeight(); y++){
            for (int x = 0; x < blurbitmap.getWidth(); x++){
                int c = blurbitmap.getPixel(x, y);
                pixels_array[y][x] = c;
            }
        }

        int[] vector = mode(pixels_array);

        Arrays.sort(vector);
        double median;
        if (vector.length % 2 == 0)
            median = ((double)vector[vector.length/2] + (double)vector[vector.length/2 - 1])/2;
        else
            median = (double) vector[vector.length/2];

        int new_m = (int)median;
        Log.d("MYINT", "value: " + new_m);
        byte r, g, b;
        r = (byte)Color.red(new_m);
        g = (byte)Color.green(new_m);
        b = (byte)Color.blue(new_m);
        Log.d("COLOR","\n"+"Red : "+r+"\n"+"Green : "+g+"\n"+"Blue : "+b);

        double median_double = (double) r * 0.66;

        int median_third = (int) median_double;

        Bitmap thresh_bitmap = threshy(blurbitmap, median_third);


//        ColorMatrix t_matrix = createThresholdMatrix(median_third);
//
//        Bitmap thresh_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paintt = new Paint();
//        //cm.setSaturation(0);
//        ColorMatrixColorFilter f2 = new ColorMatrixColorFilter(t_matrix);
//
//        paintt.setColorFilter(f2);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;

//        for (int i =0 ;i<blurbitmap.getHeight();i++) {
//            for (int j =0 ;j<blurbitmap.getWidth();j++){
//                Log.v("Array Value","Array Value"+pixels_array[i][j]);
//            }
//        }

//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));

        imageView.setImageBitmap(thresh_bitmap);
    }
}
