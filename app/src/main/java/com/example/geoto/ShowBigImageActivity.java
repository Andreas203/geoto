package com.example.geoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.geoto.database.PhotoData;
import java.util.List;

public class ShowBigImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
