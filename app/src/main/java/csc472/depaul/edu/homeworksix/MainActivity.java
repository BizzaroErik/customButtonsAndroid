package csc472.depaul.edu.homeworksix;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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


        if(checkSavedPassword()){
            Log.v("PASSWORD", "found");
            enterYourPin();
        }
        else{
            Log.v("PASSWORD", "no file found");
            createNewPin();
        }

    }

    public boolean checkSavedPassword(){
        requestReadFromExternalStoragePermission();
        File sdCard = Environment.getExternalStorageDirectory();
        Log.v("PASSWORD", sdCard.toString());
        File dir = new File(sdCard.getAbsolutePath() + "/csc472");
        File file = new File(dir + "/passwords.txt");
        if(file.exists()) return true;
        else return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        RemoteDisc rd = findViewById(R.id.remote_disc);
        rd.setRemoteDiscObserver(null);

    }


    private void enterYourPin()
    {
        final EditText pin = new EditText(this);
        pin.setHint("****");
        pin.setTextSize(24);
        pin.setTypeface(null, Typeface.BOLD);
        pin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pin.setFilters(new InputFilter[] {
                // Maximum 2 characters.
                new InputFilter.LengthFilter(4),
        });
        // Digits only & use numeric soft-keyboard.
        pin.setKeyListener(DigitsKeyListener.getInstance(Locale.getDefault()));    //getInstance(Locale.getDefault()));

        final TextView pinTitle = new TextView(this);
        pinTitle.setText(getResources().getString(R.string.enter_existing_pin));
        pinTitle.setTextSize(24);
        pinTitle.setTypeface(null, Typeface.BOLD);
        pinTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        final AlertDialog createPin = new AlertDialog.Builder(this)
                .setCustomTitle(pinTitle)
                .setView(pin)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener()
                {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                    {
                        if (keyCode== KeyEvent.KEYCODE_ENTER)
                        {
                            if (pin.getText().length() == 4)
                            {
                                dialog.dismiss();
                                String encryptedPassword = getFileContents();
                                Cryption c = Cryption.getCryption();


                                String decrypted = null;
                                try {
                                    decrypted = c.aes256Decrypt(pin.getText().toString(), encryptedPassword );

                                }catch(GeneralSecurityException g){
                                    Log.v("SECURITY", g.getLocalizedMessage());
                                }
                                if(decrypted.equals(pin.getText().toString())) {
                                    getChannel();
                                }
                            }
                        }

                        return false;
                    }
                }).create();
        createPin.show();

        createPin.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (pin.getText().length() == 4)
                {
                    createPin.dismiss();
                    String encryptedPassword = getFileContents();
                    Cryption c = Cryption.getCryption();


                    String decrypted = null;
                    try {
                        decrypted = c.aes256Decrypt(pin.getText().toString(), encryptedPassword );

                    }catch(GeneralSecurityException g){
                        Log.v("SECURITY", g.getLocalizedMessage());
                    }
                    if(decrypted.equals(pin.getText().toString())) {
                        getChannel();
                    }
                }
            }
        });

        createPin.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        createPin.show();
    }

    public void setChannel(){
        SharedPreferences sp = getSharedPreferences("REMOTE_SETTINGS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Television tv = findViewById(R.id.tv);

        editor.putString("Channel", String.valueOf(tv.getCurrentResource()));
        editor.commit();
    }
    public void getChannel(){
        SharedPreferences sp = getSharedPreferences("REMOTE_SETTINGS", Activity.MODE_PRIVATE);
        String channel = sp.getString("Channel", "");
        Television tv = findViewById(R.id.tv);
        tv.setProgram(Integer.valueOf(channel));
    }


    public String getFileContents(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/csc472");
        File file = new File(dir + "/passwords.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                StringBuilder sb = new StringBuilder();
                String st;
                while ((st = br.readLine()) != null) {
                    Log.v("PASSWORD", st);
                    sb.append(st);
                }
                return sb.toString();
            }catch (IOException ioe){
                Log.v("IOE", ioe.getLocalizedMessage());
            }
        }catch(FileNotFoundException f){
            Log.v("FILENF", f.getLocalizedMessage());
        }
        return null;
    }







    private void createNewPin()
    {
        final EditText pin = new EditText(this);
        pin.setHint("****");
        pin.setTextSize(24);
        pin.setTypeface(null, Typeface.BOLD);
        pin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        pin.setFilters(new InputFilter[] {
                // Maximum 2 characters.
                new InputFilter.LengthFilter(4),
        });
        // Digits only & use numeric soft-keyboard.
        pin.setKeyListener(DigitsKeyListener.getInstance(Locale.getDefault()));    //getInstance(Locale.getDefault()));

        final TextView pinTitle = new TextView(this);
        pinTitle.setText(getResources().getString(R.string.create_new_pin));
        pinTitle.setTextSize(24);
        pinTitle.setTypeface(null, Typeface.BOLD);
        pinTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        final AlertDialog createPin = new AlertDialog.Builder(this)
                .setCustomTitle(pinTitle)
                .setView(pin)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener()
                {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                    {
                        if (keyCode== KeyEvent.KEYCODE_ENTER)
                        {
                            if (pin.getText().length() == 4)
                            {
                                dialog.dismiss();
                                saveNewPin(pin.getText().toString());
                            }
                        }

                        return false;
                    }
                }).create();
        createPin.show();

        createPin.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (pin.getText().length() == 4)
                {
                    createPin.dismiss();

                    saveNewPin(pin.getText().toString());
                }
            }
        });

        createPin.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        createPin.show();
    }

    private void saveNewPin(final String sText) {
        Cryption c = Cryption.getCryption();
        String encrypted = null;
        try {
            encrypted = c.aes256Encrypt(sText, sText);

        }catch(GeneralSecurityException g){
            Log.v("SECURITY", g.getLocalizedMessage());
        }

        /*SharedPreferences sp = getSharedPreferences("REMOTE_SETTINGS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PASSWORD", encrypted);
        editor.commit();*/
        saveText(encrypted);
    }

    private void saveText(String data){
        try
        {
            requestWriteToExternalStoragePermission();

            //create sdcard directory and file
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/csc472");
            dir.mkdirs();

            File file = new File(dir + "/passwords.txt");
            Log.v("PASSWORD", file.toString());
            file.createNewFile();

            FileOutputStream outputFile = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputFile);
            outputStreamWriter.append(data);
            outputStreamWriter.close();
            outputFile.close();
        } catch (Exception e)
        {
            //Did we forget to ask permission or make an assumption about sdcard ?
            Toast toast = Toast.makeText(getMainActivity(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void requestWriteToExternalStoragePermission()
    {
        // int readPermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED)
        {
            int REQUEST_EXTERNAL_STORAGE = 1;

            String[] PERMISSIONS_STORAGE = {
                    //Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            ActivityCompat.requestPermissions(
                    getMainActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void requestReadFromExternalStoragePermission()
    {
        int readPermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //int writePermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (readPermission != PackageManager.PERMISSION_GRANTED)
        {
            int REQUEST_EXTERNAL_STORAGE = 1;

            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            ActivityCompat.requestPermissions(
                    getMainActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private MainActivity getMainActivity()
    {
        return this;
    }


    @Override
    protected void onPause(){
        super.onPause();
        setChannel();
    }
}
