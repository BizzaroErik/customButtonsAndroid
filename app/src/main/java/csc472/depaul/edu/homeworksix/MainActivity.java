package csc472.depaul.edu.homeworksix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
{
    //TODO: You should create a model object for your channels and some logic to loop them

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //TODO: You need to set your contentView to your layout
        setContentView(R.layout.activity_main);

        //TODO: Map the remote disc control reference and set whatever object you choose as an observer


        RemoteDisc rd = findViewById(R.id.remote_disc);
        Television tv = findViewById(R.id.tv);
        tv.setProgram(R.drawable.cartoons);
        rd.setRemoteDiscObserver(tv);

        ColorButton cb1 = findViewById(R.id.blue_button);
        cb1.setBackground(R.drawable.remote_blue_active, R.drawable.remote_blue_pressed);
        ColorButton cb2 = findViewById(R.id.yellow_button);
        cb2.setBackground(R.drawable.remote_yellow_active, R.drawable.remote_yellow_pressed);
        ColorButton cb3 = findViewById(R.id.green_button);
        cb3.setBackground(R.drawable.remote_green_active, R.drawable.remote_green_pressed);
        ColorButton cb4 = findViewById(R.id.red_button);
        cb4.setBackground(R.drawable.remote_red_active, R.drawable.remote_red_pressed);

        ColorButton centerbtn = findViewById(R.id.center_yellow_button);
        centerbtn.setBackground(R.drawable.remote_yellow_active, R.drawable.remote_yellow_pressed);




        //TODO: You can have the mainactiivty be the observer or even the television class
        //TODO: Once you decide where you want to handle the events you can catch the
        //TODO: up/down channel changes and have the television object respond accordingly
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        //TODO: dont' forget to stop observing (set it to null) if you setup an observer on the remote disc
    }
}
