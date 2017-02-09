package com.eg.addtendencebackup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity
{
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.img);

    }

    private MainActivity getActivity()
    {
        return this;
    }

    public void click(View view)
    {
        View view1 = this.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        takeSnapShots();
    }

    /** picture call back */
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera)
        {
            FileOutputStream outStream = null;
            try {
                //String dir_path = "";// set your directory path here
                //outStream = new FileOutputStream(dir_path+File.separator+image_name+no_pics+".jpg");
                //outStream.write(data);
                //outStream.close();
                //Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                //Canvas canvas = new Canvas(mutableBitmap); // now it should work ok
                mImageView.setImageBitmap(bmp);
                Toast.makeText(getApplicationContext(), "Image snapshot Done",Toast.LENGTH_LONG).show();
                PHPConnectorWorker connectorWorker = new PHPConnectorWorker(getActivity(), data);
                connectorWorker.execute(((EditText) findViewById(R.id.fname)).getText().toString(), ((EditText) findViewById(R.id.lname)).getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally
            {
                camera.stopPreview();
                camera.release();
                camera = null;
                Toast.makeText(getApplicationContext(), "Image snapshot Done",Toast.LENGTH_LONG).show();


            }
            //Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    private void takeSnapShots()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            getCameraPermission();
            return;
        }

        Toast.makeText(getApplicationContext(), "Image snapshot   Started",Toast.LENGTH_SHORT).show();
        // here below "this" is activity context.
        SurfaceView surface = new SurfaceView(this);
        Camera camera = Camera.open(1);
        try {
            camera.setPreviewDisplay(surface.getHolder());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
        camera.takePicture(null,null,jpegCallback);
    }
    
    private void getCameraPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Here, thisActivity is the current activity
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }

    public String getIP()
    {
        return ((EditText) findViewById(R.id.ip)).getText().toString();
    }
}
