package methode.methodeelectronics_newdb;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity{
    TabHost th;
    public int count=0;
    private MyDatabaseHandler db = new MyDatabaseHandler(this);
    public ArrayList<String> displaymp3 = new ArrayList<String>();
    public ArrayList<String> displayvideo = new ArrayList<String>();
    public ArrayList<String> displayimage = new ArrayList<String>();
    public int start;
    public int run=0;
    public int stopmp3;
    public int stopvideo;
    public int stopimage;
    public MediaPlayer mediaPlayer;
    
    public int i=0;
    public String song;
    public String videofile;
    public String imagefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        th = (TabHost) findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec ts1 = th.newTabSpec("tag1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("", getDrawable(R.drawable.audio_tab));
        th.addTab(ts1);
        TabHost.TabSpec ts2 = th.newTabSpec("tag2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("", getDrawable(R.drawable.video_tab));
        th.addTab(ts2);
        TabHost.TabSpec ts3 = th.newTabSpec("tag3");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("", getDrawable(R.drawable.image_tab));
        th.addTab(ts3);

        BaseAdapter mymp3adapter = null;
        BaseAdapter myvideoadapter = null;
        BaseAdapter myimageadapter = null;

        stopmp3=1;
        stopvideo=1;
        stopimage=1;
        final String[] path = {"storage/extSdCard/songs/english/"};
        final String[] pathvideo = {"storage/extSdCard/videos/"};
        final String[] pathimage = {"storage/extSdCard/photos/"};

        ArrayList<String> filesinfoldermp3 = getfiles(path[0]);
        final ListView listViewmp3 = (ListView) findViewById(R.id.listViewmp3);
        displaymp3=filesinfoldermp3;

       final MediaPlayer player=MediaPlayer.create(this,R.raw.intheend);
//        final MediaPlayer mediaplayer = MediaPlayer.create()

        listViewmp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                path[0]="storage/extSdCard/songs/english/";
                String mp3name=String.valueOf(parent.getItemAtPosition(position));
                path [0]+=mp3name;
                Uri uri = Uri.parse(path[0]);
                Log.d("path : ", "" + path[0]);
                //player.start();
                try {
                    player.reset();
                    player.setDataSource(String.valueOf(uri));
                    player.prepare();
                    player.start();
                    //player.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        displaymp3 = db.getfixedmp3files(0,stopmp3);  //displays initial 100 or x no of files before scrolling can begin
          mymp3adapter = new CustomAdapter(this, displaymp3);
        listViewmp3.setAdapter(mymp3adapter);
        final BaseAdapter finalMymp3adapter = mymp3adapter;

        final ListView listViewvideo = (ListView) findViewById(R.id.listViewvideo);
        //displayvideo = db.getfixedvideofiles(0, stopvideo);  //displays initial 100 or x no of files before scrolling can begin
        ArrayList<String> filesinfoldervideo = getfiles(pathvideo[0]);
        displayvideo=filesinfoldervideo;
       // final VideoView videoView = (VideoView) findViewById(R.id.videoView);

        //videoView.setMediaController(new MediaController(this));
        listViewvideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(player.isPlaying())
                    player.stop();
                pathvideo[0]="storage/extSdCard/videos/";
                String videoname=String.valueOf(parent.getItemAtPosition(position));
                pathvideo [0]+=videoname;
                setpath(pathvideo[0]);
                Intent videop = new Intent(DisplayActivity.this, Videoplayback.class);
                videop.putExtra("video",pathvideo[0]);
                startActivity(videop);

            }
        });
//        if(!videoView.isPlaying())
//            videoView.setVisibility(View.INVISIBLE);

        myvideoadapter = new CustomAdapterVideo(this, displayvideo);
        listViewvideo.setAdapter(myvideoadapter);
        final BaseAdapter finalMyvideoadapter = myvideoadapter;



        final ListView listViewphoto = (ListView) findViewById(R.id.listViewphoto);
        //displayimage = db.getfixedimagefiles(0,stopimage);  //displays initial 100 or x no of files before scrolling can begin

        ArrayList<String> filesinfolderimage = getfiles(pathimage[0]);
        displayimage=filesinfolderimage;
        listViewphoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pathimage[0]="storage/extSdCard/photos/";
                String imagename=String.valueOf(parent.getItemAtPosition(position));
                pathimage[0]+=imagename;
                setpath(pathimage[0]);
                Intent imagep = new Intent(DisplayActivity.this, Imageviewplay.class);
                imagep.putExtra("image", pathimage[0]);
                startActivity(imagep);
            }
        });

        myimageadapter = new CustomAdapterImage(this, displayimage);
        listViewphoto.setAdapter(myimageadapter);
        final BaseAdapter finalMyimageadapter = myimageadapter;

        //AUDIO FILES TAB
        listViewmp3.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int firstVisibleItem = listViewmp3.getFirstVisiblePosition();
                int lastvisibleItemCount = listViewmp3.getLastVisiblePosition();
                Log.d("last position visible", "" + lastvisibleItemCount);
                Log.d("first position visible", "" + firstVisibleItem);

                int totalItemCount = listViewmp3.getChildCount();
                Log.d("total", "" + totalItemCount);
                Log.d("inside", "123");
                for (i = 0; i <= 4; i++) {
//                    song = db.getSinglemp3file(i + 1 + stopmp3);

                    song = db.getSinglemp3file(i + 1+stopmp3);
                    Log.d("song","song received = "+song);
                    if(song!=null){
                        displaymp3.add(song);
                        count++;
                        Log.d("xyz", "12 : " + displaymp3);
                        finalMymp3adapter.notifyDataSetChanged();}
                    else
                        break;
                }
                stopmp3 = stopmp3 + i;

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int lastvisibleItemCount, int totalItemCount) {

            }
        });
        //VIDEO FILES TAB
        listViewvideo.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int firstVisibleItem = listViewvideo.getFirstVisiblePosition();
                int lastvisibleItemCount = listViewvideo.getLastVisiblePosition();
                Log.d("last position visible", "" + lastvisibleItemCount);
                Log.d("first position visible", "" + firstVisibleItem);

                int totalItemCount = listViewvideo.getChildCount();
                Log.d("total", "" + totalItemCount);
                Log.d("inside video", "1234");
                for (i = 0; i <= 4; i++) {
                    videofile = db.getSinglevideofile(i + 1 + stopvideo);
                    Log.d("video","video received = "+ videofile);
                    if(videofile!=null){
                        displayvideo.add(videofile);
                        Log.d("xyz", "12 : " + displayvideo);
                        finalMyvideoadapter.notifyDataSetChanged();}
                    else
                        break;
                }
                stopvideo = stopvideo + i;

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int lastvisibleItemCount, int totalItemCount) {
                
            }
        });
        //IMAGE FILES TAB
        listViewphoto.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int firstVisibleItem = listViewphoto.getFirstVisiblePosition();
                int lastvisibleItemCount = listViewphoto.getLastVisiblePosition();
                Log.d("last position visible", "" + lastvisibleItemCount);
                Log.d("first position visible", "" + firstVisibleItem);

                int totalItemCount = listViewphoto.getChildCount();
                Log.d("total", "" + totalItemCount);
                Log.d("inside image", "12345");
                for (i = 0; i <= 4; i++) {
                    imagefile = db.getSingleimagefile(i + 1 + stopimage);
                    Log.d("song","song received = "+song);
                    if(imagefile!=null){
                        displayimage.add(imagefile);
                        Log.d("xyz", "12 : " + displayimage);
                        finalMyimageadapter.notifyDataSetChanged();}
                    else
                        break;
                }
                stopimage = stopimage + i;

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int lastvisibleItemCount, int totalItemCount) {

            }
        });
    }
    public ArrayList<String> getfiles(String path)
    {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(path);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }
    public String setpath;
    public void setpath(String setpath)
    {
        this.setpath=setpath;
    }
    public String getpath()
    {
        return setpath;
    }
}
