package methode.methodeelectronics_newdb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class Imageviewplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewplay);
        ImageView imageView = (ImageView)findViewById(R.id.imageView2);
        Intent intent = getIntent();
        String path = intent.getStringExtra("image");
        Uri uri=Uri.parse(path);
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(uri));
        imageView.setImageBitmap(bitmap);
    }
}
