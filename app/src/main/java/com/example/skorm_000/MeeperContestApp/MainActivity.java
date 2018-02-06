package com.example.skorm_000.MeeperContestApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    EditText firstNameInput;
    EditText lastNameInput;
    EditText phoneInput;
    EditText emailInput;
    //EditText addressInput;

    String tag1 = ""; //TODO set up prefs on what the previous event was
    String tag2 = "";
    String tag3 = "";
    String tempTag1;
    String tempTag2;
    String tempTag3;
    String tempTag1Results;
    String tempTag2Results;
    String tempTag3Results;
    String password = "0204";

    LinearLayout mainScreen;
    RelativeLayout loginScreen;
    RelativeLayout adminScreen;
    LinearLayout termsAndConditionsLayout;

    Context context;

    com.example.skorm_000.MeeperContestApp.NoDefaultSpinner tag1Spinner;
    com.example.skorm_000.MeeperContestApp.NoDefaultSpinner tag2Spinner;
    com.example.skorm_000.MeeperContestApp.NoDefaultSpinner tag3Spinner;
    Spinner tag1SpinnerResults;
    Spinner tag2SpinnerResults;
    Spinner tag3SpinnerResults;
    EditText tag1Input;
    EditText tag2Input;
    EditText tag3Input;

    ArrayList<String> tag1List = new ArrayList<>();
    ArrayList<String> tag2List = new ArrayList<>();
    ArrayList<String> tag3List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences tagPrefs = getSharedPreferences("tagPrefs", 0);

        tag1 = tagPrefs.getString("tag1Pref", null);
        tag2 = tagPrefs.getString("tag2Pref", null);
        tag3 = tagPrefs.getString("tag3Pref", null);

        context = this;

        mainScreen = (LinearLayout) findViewById(R.id.mainScreen);
        loginScreen = (RelativeLayout) findViewById(R.id.loginLayout);
        adminScreen = (RelativeLayout) findViewById(R.id.adminSetupLayout);
        termsAndConditionsLayout = (LinearLayout) findViewById(R.id.termsAndConditionLayout);

        firstNameInput = (EditText) findViewById(R.id.nameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        //addressInput = (EditText) findViewById(R.id.addressInput);

        ImageButton submitButton = (ImageButton) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!firstNameInput.getText().toString().equals("") && !firstNameInput.getText().toString().equals(null)
                        &&!lastNameInput.getText().toString().equals("") && !lastNameInput.getText().toString().equals(null)
                        &&!emailInput.getText().toString().equals("") && !emailInput.getText().toString().equals(null))
                        /*&&!phoneInput.getText().toString().equals("") && !phoneInput.getText().toString().equals(null)
                        &&!addressInput.getText().toString().equals("") && !addressInput.getText().toString().equals(null))*/{
                    if((phoneInput.getText().toString().equals("") || phoneInput.getText().toString().equals(null)) || phoneInput.getText().length() < 10 || phoneInput.getText().length() > 10){
                        phoneInput.setText(" ");
                    }
                    /*if(addressInput.getText().toString().equals("") || addressInput.getText().toString().equals(null)){
                        addressInput.setText(" ");
                    }*/

                    String firstName = firstNameInput.getText().toString().replace(",", "");
                    String lastName = lastNameInput.getText().toString().replace(",", "");
                    String email = emailInput.getText().toString().replace(",", "");
                    String phone = phoneInput.getText().toString().replace(",", "");
                    //String address = addressInput.getText().toString().replace(",", "");

                    String uniqueID = UUID.randomUUID().toString();
                    String csvString = uniqueID + "," + firstName + " " + lastName + "," + email + "," + phone + ","/* + address + ","*/ + Calendar.getInstance().getTime() + "," + tag1 + "," + tag2 + "," + tag3 + ";\n";
                    showPopUp(csvString);
                }
            }
        });

        FrameLayout optionButton = (FrameLayout) findViewById(R.id.optionButton);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen.setVisibility(View.GONE);

                TextView tag1Text = (TextView) findViewById(R.id.Tag1Text);
                tag1Text.setText(tag1 + " - " + tag2);
                TextView tag3Text = (TextView) findViewById(R.id.Tag3Text);
                tag3Text.setText(tag3);

                loginScreen.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout closeOptionButton = (LinearLayout) findViewById(R.id.closeOptionButton);
        closeOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen.setVisibility(View.VISIBLE);
                loginScreen.setVisibility(View.GONE);
            }
        });

        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        tag1Input = (EditText) findViewById(R.id.newEventInput);
        tag2Input = (EditText) findViewById(R.id.newLocationInput);
        tag3Input = (EditText) findViewById(R.id.newDayInput);

        ImageButton loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordInput.getText().toString().equals(password)){
                    loginScreen.setVisibility(View.GONE);


                    TextView tag1AdminText = (TextView) findViewById(R.id.tag1AdminText);
                    tag1AdminText.setText(tag1 + " - " + tag2);
                    TextView tag3AdminText = (TextView) findViewById(R.id.tag3AdminText);
                    tag3AdminText.setText(tag3);

                    tempTag1 = tag1;
                    tempTag2 = tag2;
                    tempTag3 = tag3;

                    setSpinners(1);
                    setSpinners(2);
                    setSpinners(3);
                    setSpinners(4);
                    setSpinners(5);
                    setSpinners(6);

                    adminScreen.setVisibility(View.VISIBLE);
                    passwordInput.getText().clear();
                }else{
                    Log.d("MYTAG", "PASSWORD IS INCORRECT");
                }
            }
        });

        ImageButton createButton = (ImageButton) findViewById(R.id.createEventButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmConfirmation = new AlertDialog.Builder(context);
                confirmConfirmation.setTitle("Confirmation");
                confirmConfirmation.setMessage("By selecting confirm you will be ending the current conteset.");
                confirmConfirmation.create();
                confirmConfirmation.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateTags();
                    }
                });
                confirmConfirmation.setNegativeButton("CANCEL", null);
                confirmConfirmation.show();
            }
        });

        LinearLayout closeAdminButton = (LinearLayout) findViewById(R.id.closeAdminButton);
        closeAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminScreen.setVisibility(View.GONE);
                mainScreen.setVisibility(View.VISIBLE);

                setLayout("Details");
            }
        });

        FrameLayout detailsButton = (FrameLayout) findViewById(R.id.detailsButton);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout("Details");
            }
        });
        FrameLayout resultsButton = (FrameLayout) findViewById(R.id.resultsButton);
        resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayout("Results");
            }
        });;

        FrameLayout termsAndConditionsButton = (FrameLayout) findViewById(R.id.termsAndConditionButton);
        termsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ScrollView termsScrollview = (ScrollView) findViewById(R.id.termsScrollview);

                termsScrollview.fullScroll(View.FOCUS_UP);//if you move at the end of the scroll
                termsScrollview.pageScroll(View.FOCUS_UP);//if you move at the middle of the scroll
                termsScrollview.smoothScrollTo(0,0);

                mainScreen.setVisibility(View.GONE);
                termsAndConditionsLayout.setVisibility(View.VISIBLE);
            }
        });

        FrameLayout privacyPolicyButton = (FrameLayout) findViewById(R.id.privacyPolicyButton);
        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://meeperbot.com/pages/privacy-policy");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        LinearLayout closeTermsAndConditionsButton = (LinearLayout) findViewById(R.id.closeTermsAndConditionsButton);
        closeTermsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsAndConditionsLayout.setVisibility(View.GONE);
                mainScreen.setVisibility(View.VISIBLE);
            }
        });

        checkPermission();

        ImageButton downloadButton = (ImageButton) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCSV();
            }
        });

    }

    //disable the back button or set it to do what we want
    @Override
    public void onBackPressed() {
    }

    void showPopUp(String csvString) {
        final LinearLayout popUp = (LinearLayout) findViewById(R.id.popUpLayout);
        popUp.setVisibility(View.VISIBLE);
        mainScreen.setVisibility(View.GONE);

        saveInfo(csvString);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        firstNameInput.getText().clear();
                        lastNameInput.getText().clear();
                        emailInput.getText().clear();
                        phoneInput.getText().clear();
                        //addressInput.getText().clear();
                        popUp.setVisibility(View.GONE);
                        mainScreen.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 5000);
    }

    String filename = "meeperContestList.txt";

    private String NameOfFolder = "/MeeperContest";
    private File file = null;

    void saveInfo(String csvString){


        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+NameOfFolder;
        File directory = new File(file_path);
        if(!directory.exists()){
            if(directory.mkdirs()){
                file = new File(directory, filename);
            }else{
                Log.d("MYTAG", "FOLDER CREATION FAILED");
                //Display error here
            }
        }else{
            file =  new File(directory, "/" + filename);
            if(!file.exists()){
                Log.d("MYTAG", "FILE DOES NOT EXIST");
                //Display error here
            }
        }

        try{
            FileOutputStream outputStream = new FileOutputStream(file.getPath(), true);

            outputStream.write(csvString.getBytes());
            outputStream.flush();
            outputStream.close();
            MakeSureFileWasCreatedanMadeAvailable(file);

            Log.d("MYTAG", "FILE SAVED");
        }catch(FileNotFoundException e){
            //Display error here
        }catch (IOException e){
            //Display error here
        }
    }
    private void MakeSureFileWasCreatedanMadeAvailable(File file){

        MediaScannerConnection.scanFile(this, new String[] {file.toString()},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage", "Scanned" + path + ":");
                    }
                });
    }

    void setLayout(String layout){
        LinearLayout detailsLayout = (LinearLayout) findViewById(R.id.detailsLayout);
        LinearLayout resultsLayout = (LinearLayout) findViewById(R.id.resultsLayout);

        LinearLayout adminBackground = (LinearLayout) findViewById(R.id.adminSetupBackground);

        if(layout == "Details" && detailsLayout.getVisibility() != View.VISIBLE){
            adminBackground.setBackgroundResource(R.drawable.background_admin);
            detailsLayout.setVisibility(View.VISIBLE);
            resultsLayout.setVisibility(View.GONE);
            tag1Input.getText().clear();
            tag1Input.setVisibility(View.GONE);
            tag2Input.getText().clear();
            tag2Input.setVisibility(View.GONE);
            tag3Input.getText().clear();
            tag3Input.setVisibility(View.GONE);

            tag1Spinner.setNoSelection();
            tag1Spinner.setPrompt("Event");
            tag2Spinner.setNoSelection();
            tag2Spinner.setPrompt("Location");
            tag3Spinner.setNoSelection();
            tag3Spinner.setPrompt("Day");


            createTagList();
            setSpinners(1);
            setSpinners(2);
            setSpinners(3);

        }else if(layout == "Results" && resultsLayout.getVisibility() != View.VISIBLE){
            adminBackground.setBackgroundResource(R.drawable.background_admin_2);
            detailsLayout.setVisibility(View.GONE);
            resultsLayout.setVisibility(View.VISIBLE);

            tag1SpinnerResults.setSelection(0);
            tag1SpinnerResults.setPrompt("Any Event");
            tag2SpinnerResults.setSelection(0);
            tag2SpinnerResults.setPrompt("Any Location");
            tag3SpinnerResults.setSelection(0);
            tag3SpinnerResults.setPrompt("Any Day");

            createTagList();
            setSpinners(4);
            setSpinners(5);
            setSpinners(6);
        }
    }

    void updateTags(){

        if(tag1Input.getVisibility() == View.VISIBLE){
            if(!tag1Input.getText().toString().equals("") && !tag1Input.getText().toString().equals(null)) {
                tempTag1 = tag1Input.getText().toString();
                saveTags(tempTag1 + ",", 1);
            }
        }
        if(tag2Input.getVisibility() == View.VISIBLE){
            if(!tag2Input.getText().toString().equals("") && !tag2Input.getText().toString().equals(null)) {
                tempTag2 = tag2Input.getText().toString();
                saveTags(tempTag2 + ",", 2);
            }
        }
        if(tag3Input.getVisibility() == View.VISIBLE){
            if(!tag3Input.getText().toString().equals("") && !tag3Input.getText().toString().equals(null)) {
                tempTag3 = tag3Input.getText().toString();
                saveTags(tempTag3 + ",", 3);
            }
        }

        SharedPreferences tagPrefs = getSharedPreferences("tagPrefs", 0);;

        tag1 = tempTag1;
        tagPrefs.edit().putString("tag1Pref", tag1).commit();
        tag2 = tempTag2;
        tagPrefs.edit().putString("tag2Pref", tag2).commit();
        tag3 = tempTag3;
        tagPrefs.edit().putString("tag3Pref", tag3).commit();



        createTagList();
        setSpinners(1);
        setSpinners(2);
        setSpinners(3);
        setSpinners(4);
        setSpinners(5);
        setSpinners(6);

        TextView tag1AdminText = (TextView) findViewById(R.id.tag1AdminText);
        tag1AdminText.setText(tag1 + " - " + tag2);
        TextView tag3AdminText = (TextView) findViewById(R.id.tag3AdminText);
        tag3AdminText.setText(tag3);
    }

    private void saveTags(String data, int tagNumber) {
        switch (tagNumber) {
            case 1:
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("tag1List.txt", MODE_APPEND));
                    outputStreamWriter.write(data);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File tag1List write failed: " + e.toString());
                }
                break;

            case 2:
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("tag2List.txt", MODE_APPEND));
                    outputStreamWriter.write(data);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File tag2List write failed: " + e.toString());
                }
                break;

            case 3:
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("tag3List.txt", MODE_APPEND));
                    outputStreamWriter.write(data);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File tag3List write failed: " + e.toString());
                }
                break;
        }
    }

    String createTagList() {
        String ret1 = "";
        String ret2 = "";
        String ret3 = "";

        try {
            InputStream inputStream = openFileInput("tag1List.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret1 = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {
            InputStream inputStream = openFileInput("tag2List.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret2 = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try {
            InputStream inputStream = openFileInput("tag3List.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret3 = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        tag1List.clear();
        tag1List.add("New Event");
        StringTokenizer token1 = new StringTokenizer(ret1, ",");
        while (token1.hasMoreTokens()) {
            tag1List.add(token1.nextToken());
        }
        tag2List.clear();
        tag2List.add("New Location");
        StringTokenizer token2 = new StringTokenizer(ret2, ",");
        while (token2.hasMoreTokens()) {
            tag2List.add(token2.nextToken());
        }
        tag3List.clear();
        tag3List.add("New Day");
        tag3List.add("Monday");
        tag3List.add("Tuesday");
        tag3List.add("Wednesday");
        tag3List.add("Thursday");
        tag3List.add("Friday");
        tag3List.add("Saturday");
        tag3List.add("Sunday");
        StringTokenizer token3 = new StringTokenizer(ret3, ",");
        while (token3.hasMoreTokens()) {
            tag3List.add(token3.nextToken());
        }

        return ret1 + " : " + ret2 + " : " + ret3;
    }

    void setSpinners(int spinnerNumber){

        switch (spinnerNumber){
            case 1:
                tag1Spinner = (com.example.skorm_000.MeeperContestApp.NoDefaultSpinner) findViewById(R.id.tag1DropDown);
                final ArrayList<String> tag1Items = tag1List;//new String[]{"New Event", "Brick Fest Live", "Minefaire", "Puma Palooza"};
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag1Items);
                tag1Spinner.setAdapter(adapter1);
                tag1Spinner.setNoSelection();
                tag1Spinner.setPrompt("Event");

                tag1Input.setVisibility(View.GONE);
                tag1Input.getText().clear();

                tag1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tag1Input.setVisibility(View.VISIBLE);
                                break;
                            default:
                                tempTag1 = tag1Items.get(i);
                                tag1Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 2:
                tag2Spinner = (com.example.skorm_000.MeeperContestApp.NoDefaultSpinner) findViewById(R.id.tag2DropDown);
                final ArrayList<String> tag2Items = tag2List;//new String[]{"New Location", "Philidalphia", "Madison"};
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag2Items);
                tag2Spinner.setAdapter(adapter2);
                tag2Spinner.setNoSelection();
                tag2Spinner.setPrompt("Location");

                tag2Input.setVisibility(View.GONE);
                tag2Input.getText().clear();

                tag2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tag2Input.setVisibility(View.VISIBLE);
                                break;
                            default:
                                tempTag2 = tag2Items.get(i);
                                tag2Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 3:
                tag3Spinner = (com.example.skorm_000.MeeperContestApp.NoDefaultSpinner) findViewById(R.id.tag3DropDown);
                final ArrayList<String> tag3Items = tag3List;//new String[]{"New Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag3Items);
                tag3Spinner.setAdapter(adapter3);
                tag3Spinner.setNoSelection();
                tag3Spinner.setPrompt("Day");

                tag3Input.setVisibility(View.GONE);
                tag3Input.getText().clear();

                tag3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tag3Input.setVisibility(View.VISIBLE);
                                break;
                            default:
                                tempTag3 = tag3Items.get(i);
                                tag3Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 4:
                tag1SpinnerResults = (Spinner) findViewById(R.id.tag1DropDownResults);
                final ArrayList<String> tag1ItemsResults = tag1List;
                tag1ItemsResults.set(0, "Any Event");
                ArrayAdapter<String> adapter1Results = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag1ItemsResults);
                tag1SpinnerResults.setAdapter(adapter1Results);
                tag1SpinnerResults.setSelection(0);
                tag1SpinnerResults.setPrompt("Any Event");

                //tag1Input.setVisibility(View.GONE);
                //tag1Input.getText().clear();

                tag1SpinnerResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tempTag1Results = null;
                                break;
                            default:
                                tempTag1Results = tag1ItemsResults.get(i);
                                //tag1Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 5:
                tag2SpinnerResults = (Spinner) findViewById(R.id.tag2DropDownResults);
                final ArrayList<String> tag2ItemsResults = tag2List;//new String[]{"New Location", "Philidalphia", "Madison"};
                tag2ItemsResults.set(0, "Any Location");
                ArrayAdapter<String> adapter2Results = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag2ItemsResults);
                tag2SpinnerResults.setAdapter(adapter2Results);
                tag2SpinnerResults.setSelection(0);
                tag2SpinnerResults.setPrompt("Any Location");

                //tag2Input.setVisibility(View.GONE);
                //tag2Input.getText().clear();

                tag2SpinnerResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tempTag2Results = null;
                                break;
                            default:
                                tempTag2Results = tag2ItemsResults.get(i);
                                //tag2Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case 6:
                tag3SpinnerResults = (Spinner) findViewById(R.id.tag3DropDownResults);
                final ArrayList<String> tag3ItemsResults = tag3List;//new String[]{"New Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                tag3ItemsResults.set(0, "Any Day");
                ArrayAdapter<String> adapter3Results = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tag3ItemsResults);
                tag3SpinnerResults.setAdapter(adapter3Results);
                tag3SpinnerResults.setSelection(0);
                tag3SpinnerResults.setPrompt("Any Day");

                //tag3Input.setVisibility(View.GONE);
                //tag3Input.getText().clear();

                tag3SpinnerResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:
                                tempTag3Results = null;
                                break;
                            default:
                                tempTag3Results = tag3ItemsResults.get(i);
                                //tag3Input.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
        }
    }

    void checkPermission(){
        final ArrayList<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add("WRITE_EXTERNAL_STORAGE");
            }

            if(permissionsList.size() != 0)
            {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 789);
            }
            else
            {
                createTagList();
            }
        }else{
            createTagList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 789: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createTagList();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage, please restart the app and accept permission to let the app work properly", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void downloadCSV(){

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+NameOfFolder+"/"+filename;
        String ret1 = "";

        try {
            FileInputStream inputStream = new FileInputStream(new File(file_path));//openFileInput(file_path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret1 = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        ArrayList<String> entryList = new ArrayList<>();
        StringTokenizer tokens = new StringTokenizer(ret1, ";");
        while (tokens.hasMoreTokens()) {
            entryList.add(tokens.nextToken());
        }

        ArrayList<String> queryList = new ArrayList<>();

        for (String info: entryList) {
            ArrayList<String> infoList = new ArrayList<>();
            StringTokenizer infotokens = new StringTokenizer(info, ",");
            while (infotokens.hasMoreTokens()) {
                String insert = infotokens.nextToken();
                if(insert == null || insert == ""){
                    insert = " ";
                }
                infoList.add(insert);
            }
            Log.d("MYTAG", infoList.toString());
            if((infoList.get(5).equals(tempTag1Results) || tempTag1Results == null || tempTag1Results == "")
                    && (infoList.get(6).equals(tempTag2Results) || tempTag2Results == null || tempTag2Results == "")
                    && (infoList.get(7).equals(tempTag3Results) || tempTag3Results == null || tempTag3Results == "")){
                queryList.add(info + "\n");
            }

        }

        if(queryList.size() > 0) {
            exportFile(queryList);
        }else{
            Toast.makeText(MainActivity.this, "No entries to export", Toast.LENGTH_LONG).show();
        }
    }

    private File exportFile = null;
    void exportFile(ArrayList<String> exportList){

        DateFormat df = new SimpleDateFormat("MM_dd_yy");
        String date = df.format(new Date());
        if(tempTag1Results == null){
            tempTag1Results = "";
        }
        if(tempTag2Results == null){
            tempTag2Results = "";
        }
        if(tempTag3Results == null){
            tempTag3Results = "";
        }
        String exportFileName = "meeperList_" + tempTag1Results + "_" + tempTag2Results + "_" + tempTag3Results + "_" + date + ".csv";

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+NameOfFolder;
        File directory = new File(file_path);

        if(!directory.exists()){
            if(directory.mkdirs()){
                exportFile = new File(directory, exportFileName);
            }else{
                Log.d("MYTAG", "FOLDER CREATION FAILED");
                //Display error here
            }
        }else{
            exportFile =  new File(directory, "/" + exportFileName);
            if(!exportFile.exists()){
                Log.d("MYTAG", "FILE DOES NOT EXIST");
                //Display error here
            }
        }

        try{
            for (String info : exportList) {
                FileOutputStream outputStream = new FileOutputStream(exportFile.getPath(), true);

                outputStream.write(info.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            MakeSureFileWasCreatedanMadeAvailable(exportFile);

            Toast.makeText(MainActivity.this, "File exported as: " + exportFileName, Toast.LENGTH_LONG).show();

        }catch(FileNotFoundException e){
            //Display error here
        }catch (IOException e){
            //Display error here
        }
    }
}
