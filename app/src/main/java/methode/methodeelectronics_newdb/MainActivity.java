package methode.methodeelectronics_newdb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("handler", "coming inside handler");

            Intent intentactivity = new Intent(MainActivity.this, methodelogo.class);
            startActivity(intentactivity);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView methodelogo = (ImageView) findViewById(R.id.methodebluelogo);
        methodelogo.setImageResource(R.drawable.methodebluelogo);

        Intent gotoservice = new Intent(MainActivity.this, MyBackgroundService.class);
        startService(gotoservice);
        Display();
    }
    public void Display() {
        Log.d("Thread", "came into display method");
        Runnable r=new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("thread","came to wait");
                    Thread.sleep(4000);
                    Log.d("thread", "waited for 10 sec");
                    Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_SHORT).show();
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