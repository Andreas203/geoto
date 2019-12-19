package com.example.geoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Window;
import android.widget.ImageView;

import com.example.geoto.database.PhotoData;
import java.util.List;

/**
 * The activity to display an image full screen
 */
public class ShowBigImageActivity extends AppCompatActivity {
    /**
     * Called when the show big image activity is first created
     * Displays an image full screen
     * @param savedInstanceState the current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_big_image);

        Bundle b = getIntent().getExtras();
        String path=null;
        if(b != null) {
            path = b.getString("path");
            if (path!=null) {
                ImageView imageView = (ImageView) findViewById(R.id.big_image);
                Bitmap myBitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(myBitmap);
            }
        }


    }

}
