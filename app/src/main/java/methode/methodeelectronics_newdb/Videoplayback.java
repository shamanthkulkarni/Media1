package methode.methodeelectronics_newdb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videoplayback extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayback);
        VideoView videoView=(VideoView) findViewById(R.id.videoView2);
        DisplayActivity da=new DisplayActivity();
        Intent intent = getIntent();
        String path = intent.getStringExtra("video");
        Log.d("video p",""+path);
        videoView.setMediaController(new MediaController(this));
        Uri uri=Uri.parse(path);
        videoView.setVideoPath(String.valueOf(uri));
        videoView.requestFocus();
        videoView.start();
    }
}
