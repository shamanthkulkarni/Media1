package methode.methodeelectronics_newdb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class methodelogo extends AppCompatActivity {
    private ProgressBar spinner;
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("handler", "coming inside handler");
            spinner.setVisibility(View.INVISIBLE);
            Intent intentactivity = new Intent(methodelogo.this, DisplayActivity.class);
            startActivity(intentactivity);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_methodelogo);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        Display();
    }
    public void Display() {
        Log.d("Thread", "came into display method");
        Runnable r=new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("thread","came to wait");
                    Thread.sleep(37000);
                    Log.d("thread", "waited for 10 sec");
                    //  Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    Log.d("thread", "exception occured");
                    e.printStackTrace();

                }
                h.sendEmptyMessage(0);
            }
        };
        Thread mydisplaythread = new Thread(r);
        mydisplaythread.start();
    }
}
