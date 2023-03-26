package edu.ewubd.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventInformationActivity extends AppCompatActivity {

    private EditText etName, etDateTime,place,capacity,budget,description,email,phone;
    private RadioButton rdIndoor,rdOutdoor,rdOnline;


    private String existingKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("@EventInformationActivity-onCreate()");
        setContentView(R.layout.event_information_layout);

        etName = findViewById(R.id.etName);
        etDateTime = findViewById(R.id.etDateTime);
        rdIndoor = findViewById(R.id.rIndoor);
        rdOutdoor = findViewById(R.id.rOutdoor);
        rdOnline = findViewById(R.id.rOnline);
        place = findViewById(R.id.place);
        capacity = findViewById(R.id.capacity);
        budget = findViewById(R.id.budget);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);

        findViewById(R.id.btnCancel).setOnClickListener(view -> finish());
        findViewById(R.id.btnSave).setOnClickListener(view -> save());


        Intent i = getIntent();

        existingKey = i.getStringExtra("EventKey");
        if(existingKey != null && !existingKey.isEmpty()) {
            initializeFormWithExistingData(existingKey);
        }
    }

    private void initializeFormWithExistingData(String eventKey){

        String value = Util.getInstance().getValueByKey(this, eventKey);
        System.out.println("Key: " + eventKey + "; Value: "+value);

        if(value != null) {
            String[] fieldValues = value.split("-::-");
            String name = fieldValues[0];
            String dateTime = fieldValues[1];
            String eventType = fieldValues[2];
            String p = fieldValues[3];
            String c = fieldValues[4];
            String b = fieldValues[5];
            String em = fieldValues[6];
            String ph = fieldValues[7];
            String des = fieldValues[8];


            etName.setText(name);
            etDateTime.setText(dateTime);
            place.setText(p);
            capacity.setText(c);
            budget.setText(b);
            email.setText(em);
            phone.setText(ph);
            description.setText(des);



            if(eventType.equals("INDOOR")){
                rdIndoor.setChecked(true);
            }else if(eventType.equals("OUTDOOR")){
                rdOutdoor.setChecked(true);
            }else if(eventType.equals("ONLINE")){
                rdOutdoor.setChecked(true);
            }

        }
    }
    @Override
    public void onStart(){
        super.onStart();
        System.out.println("@EventInformationActivity-onStart()");
    }
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("@EventInformationActivity-onResume()");
    }
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("@EventInformationActivity-onPause()");
    }
    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("@EventInformationActivity-onRestart()");
    }
    @Override
    public void onStop(){
        super.onStop();
        System.out.println("@EventInformationActivity-onStop()");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("@EventInformationActivity-onDestroy()");
    }

    private void save(){
        String name = etName.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();
        String p = place.getText().toString().trim();
        String c = capacity.getText().toString().trim();
        String b = budget.getText().toString().trim();
        String em = email.getText().toString().trim();
        String ph = phone.getText().toString().trim();
        String des = description.getText().toString().trim();



        System.out.println("Event Name: "+name);
        boolean isIndoorChecked = rdIndoor.isChecked();
        boolean isOutdoorChecked = rdOutdoor.isChecked();
        boolean isOnlineChecked = rdOnline.isChecked();
        System.out.println("Is indoor checked: "+isIndoorChecked);

        if(name.length() < 3 || dateTime.isEmpty()){
            System.out.println("Invalid Data");
            return;
        }
        //String eventType = isIndoorChecked ? "INDOOR" : "";


        String eventType;

        if(isIndoorChecked == true)
        {
             eventType = "INDOOR";
        }else if(isOutdoorChecked == true)
        {
            eventType = "OUTDOOR";
        }
        else{
            eventType = "ONLINE";
        }




        String key = name+"-::-"+dateTime;

        if(existingKey != null){
            key = existingKey;
        }

        String value = name+"-::-"+dateTime+"-::-"+eventType+"-::-"+p+"-::-"+c+"-::-"+b+"-::-"+em+"-::-"+ph+"-::-"+des;



        Util.getInstance().setKeyValue(this, key, value);
        System.out.println();


        Toast.makeText(this, "Event information has been saved successfully", Toast.LENGTH_LONG).show();

        finish();
    }
}