package methode.methodeelectronics_newdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHandler extends SQLiteOpenHelper {
    private static final int DV = 2;
    private static final String Dnamefile = "headunitnew";
    private static final String DtableNamemp3 = "table_mp3"; // table name for mp3
    private static final String DtableNamevideo = "table_video";
    private static final String DtableNamephoto = "table_photo";
    private static final String mp3_id= "id";// column name inside mp3 table
    private static final String video_id= "id2";
    private static final String photo_id= "id3";
    private static final String mp3_filename = "mp3filename"; //column name inside mp3 table
    private static final String video_filename = "videofilename";
    private static final String photo_filename = "photofilename";
    public boolean empty=true;

    public int scrollcount =0;

    public MyDatabaseHandler(Context context) {
        super(context, Dnamefile, null, DV);
    }

    public boolean getstatus()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM table_mp3",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            empty=(cursor.getInt(0)==0);
        }
        cursor.close();
        return empty;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MP3_TABLE = "CREATE TABLE " + DtableNamemp3 + "(" + mp3_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + mp3_filename + " TEXT" + ")";
        String CREATE_VIDEO_TABLE = "CREATE TABLE " + DtableNamevideo + "(" + video_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + video_filename + " TEXT" + ")";
        String CREATE_PHOTO_TABLE = "CREATE TABLE " + DtableNamephoto + "(" + photo_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + photo_filename + " TEXT" + ")";
        db.execSQL(CREATE_MP3_TABLE);
        db.execSQL(CREATE_VIDEO_TABLE);
        db.execSQL(CREATE_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DtableNamemp3);
        db.execSQL("DROP TABLE IF EXISTS " + DtableNamevideo);
        db.execSQL("DROP TABLE IF EXISTS " + DtableNamephoto);
        onCreate(db);
    }
    public void addmp3file(MP3 mp3)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mp3_filename, mp3.getname());
        db.insert(DtableNamemp3, null, values);
        scrollcount++;
        setScrollcount(scrollcount);
        Log.d("value mp3 id"," "+mp3_id);
        db.close();
    }

    public void addvideofile(VIDEO video)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(video_filename, video.getname());
        db.insert(DtableNamevideo, null, values);
        db.close();
    }

    public void addphotofile(PHOTO photo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(photo_filename, photo.getname());
        db.insert(DtableNamephoto, null, values);
        db.close();
    }

    public List<MP3> getallmp3files()
    {
        List<MP3> mp3List = new ArrayList<MP3>();
        String selectquery = "SELECT * FROM "+DtableNamemp3;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectquery,null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                MP3 mp3 = new MP3();
                mp3.setFilename(cursor.getString(1));
                mp3List.add(mp3);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return mp3List;
    }

    public int start;
    public int stop;

    public void setStart(int start)
    {
        this.start = start;
    }

    public void setStop(int stop)
    {
        this.stop = stop;
    }

    public void setScrollcount(int scrollcount)
    {
        this.scrollcount = scrollcount;
    }
    public int getScrollcount()
    {
        return scrollcount;
    }

    public ArrayList<String> getfixedmp3files (int start,int stop)
    {
        ArrayList<String> mp3fixedlist = new ArrayList<String>();
        String selectquery = "SELECT * FROM "+DtableNamemp3;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectquery,null);
        if(cursor != null) {
            cursor.moveToPosition(start);
            do
            {
                MP3 mp3 = new MP3();
                mp3.setFilename(cursor.getString(1));
                mp3fixedlist.add(mp3.getname());
                cursor.moveToNext();
                Log.d("file2", "" + mp3.getname());
            }while(cursor.getPosition() <= stop);
            cursor.close();
        }
        return mp3fixedlist;
    }

    public ArrayList<String> getfixedvideofiles (int start,int stop) {
        ArrayList<String> videofixedlist = new ArrayList<String>();
        String selectquery = "SELECT * FROM " + DtableNamevideo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor != null) {
            cursor.moveToPosition(start);
            do {
                VIDEO video = new VIDEO();
                video.setFilename(cursor.getString(1));
                videofixedlist.add(video.getname());
                Log.d("video file added","added in video db ");
                cursor.moveToNext();

            } while (cursor.getPosition() <= stop);
            cursor.close();
        }
        return videofixedlist;
    }

    public ArrayList<String> getfixedimagefiles (int start,int stop)
    {
        ArrayList<String> imagefixedlist = new ArrayList<String>();
        String selectquery = "SELECT * FROM "+DtableNamephoto;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        if(cursor!=null) {
            for (cursor.moveToPosition(start); cursor.getPosition() <= stop; cursor.moveToNext()) {
                PHOTO photo = new PHOTO();
                photo.setFilename(cursor.getString(1));
                imagefixedlist.add(photo.getname());
            }
            cursor.close();
        }
        return imagefixedlist;
    }

    public String getSinglemp3file(int id) {
        Log.d("came","into single file");
        SQLiteDatabase db = this.getReadableDatabase();
        MP3 mp3 = new MP3();

        Cursor cursor = db.query(DtableNamemp3, new String[]{mp3_id,
                        mp3_filename}, mp3_id + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mp3.setFilename(cursor.getString(1));
        String song = mp3.getname();
        Log.d("file1","file sent : "+scrollcount);
        cursor.close();
        Log.d("getscroll"," "+getScrollcount());
        // return Contacts
        if(id > 349) {
            Log.d("Count","" + scrollcount);
            return null;
        }
        else
            return song;
    }

    public String getSinglevideofile(int id) {
        Log.d("came","into single file");
        SQLiteDatabase db = this.getReadableDatabase();
        VIDEO video = new VIDEO();

        Cursor cursor = db.query(DtableNamevideo, new String[]{video_id,
                        video_filename}, video_id + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        video.setFilename(cursor.getString(1));
        String song = video.getname();
        Log.d("file1","file sent");
        cursor.close();
        // return Contacts
        if(id > 12)
            return null;
        else
            return song;
    }

    public String getSingleimagefile(int id) {
        Log.d("came","into single file");
        SQLiteDatabase db = this.getReadableDatabase();
        PHOTO photo = new PHOTO();

        Cursor cursor = db.query(DtableNamephoto, new String[]{photo_id,
                        photo_filename}, photo_id + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        photo.setFilename(cursor.getString(1));
        String song = photo.getname();
        Log.d("file1","file sent");
        cursor.close();
        // return Contacts
        if(id >= 50)
            return null;
        else
            return song;
    }
    
//    public List<VIDEO> getallvideofiles()
//    {
//        List<VIDEO> videoList = new ArrayList<VIDEO>();
//        String selectquery = "SELECT * FROM "+DtableNamevideo;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if(cursor!=null && cursor.moveToFirst())
//        {
//            do {
//                VIDEO video = new VIDEO();
//                video.setFilename(cursor.getString(1));
//                videoList.add(video);
//            }while(cursor.moveToNext());
//            cursor.close();
//        }
//        return videoList;
//    }
//    public List<PHOTO> getallphotofiles()
//    {
//        List<PHOTO> photoList = new ArrayList<PHOTO>();
//        String selectquery = "SELECT * FROM "+DtableNamephoto;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectquery,null);
//        if(cursor != null && cursor.moveToFirst())
//        {
//            do {
//                PHOTO photo = new PHOTO();
//                photo.setFilename(cursor.getString(1));
//                photoList.add(photo);
//            }while(cursor.moveToNext());
//            cursor.close();
//        }
//        return photoList;
//    }
//
//    public String searchmp3(String s)
//    {
//        List<MP3> mp3List = new ArrayList<MP3>();
//        for(MP3 m:mp3List)
//        {
//            if(s==m.getname())
//                return s;
//        }
//        List<VIDEO> videoList = new ArrayList<VIDEO>();
//        for(VIDEO v:videoList)
//        {
//            if(s==v.getname())
//                return s;
//        }
//        List<PHOTO> photoList = new ArrayList<PHOTO>();
//        for(PHOTO p:photoList)
//        {
//            if(s==p.getname())
//                return s;
//        }
//        return null;
//    }
}