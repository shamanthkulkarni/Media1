package methode.methodeelectronics_newdb;


import android.util.Log;

public class MP3 {
    String filename;
    static int mp3_count=0;
    public MP3(String filename)
    {
        this.filename = filename;
    }
    public MP3()
    {

    }
    public String getname()
    {
        return this.filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
        mp3_count++;
        Log.d("mp3fileset : "," "+mp3_count);
    }
    public int gettotalmp3count()
    {
        return mp3_count;
    }

}