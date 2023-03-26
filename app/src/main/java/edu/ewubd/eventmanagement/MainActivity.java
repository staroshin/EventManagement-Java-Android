package edu.ewubd.eventmanagement;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnCreate, btnExit,btnAttendance;

    // Reference objects for handling event lists
    private ListView lvEvents;
    private ArrayList<Event> events;
    private CustomEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("@MainActivity-onCreate()");

        btnCreate = findViewById(R.id.btnCreate);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnCreate.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EventInformationActivity.class);
            startActivity(intent);
        });

        btnAttendance.setOnClickListener(view -> {
            Intent intent2  = new Intent(MainActivity.this, MyAttendanceActivity.class);
            startActivity(intent2);
        });


        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view -> finish());
        findViewById(R.id.btnAttendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Invoke History Activity here");
            }
        });


        lvEvents = findViewById(R.id.listEvents);

        loadData();
    }

    private void loadData(){
        events = new ArrayList<>();
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0) {
            return;
        }

        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String eventData = rows.getString(1);
            String[] fieldValues = eventData.split("-::-");

            String name = fieldValues[0];
            String dateTime = fieldValues[1];
            String eventType = fieldValues[2];
            String p = fieldValues[3];


            Event e = new Event(key, name, p, dateTime, "", "", "", "", "", eventType);
            events.add(e);
        }
        db.close();
        adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);


        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);
                Intent i = new Intent(MainActivity.this, EventInformationActivity.class);
                i.putExtra("EventKey", events.get(position).key);
                startActivity(i);
            }
        });

        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String message = "Do you want to delete event - "+events[position].name +" ?";
                String message = "Do you want to delete event - "+events.get(position).name +" ?";
                System.out.println(message);
                showDialog(message, "Delete Event", events.get(position).key);
                return true;
            }
        });
    }

    private void showDialog(String message, String title, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Util.getInstance().deleteByKey(MainActivity.this, key);

                        dialog.cancel();
                        loadData();
                        adapter.notifyDataSetChanged();

                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Error Dialog");
        alert.show();
    }




    @Override
    public void onStart(){
        super.onStart();
        System.out.println("@MainActivity-onStart()");
    }
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("@MainActivity-onResume()");
    }
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("@MainActivity-onPause()");
    }
    @Override
    public void onStop(){
        super.onStop();
        System.out.println("@MainActivity-onStop()");

        events.clear();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("@MainActivity-onRestart()");

        loadData();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("@MainActivity-onDestroy()");
    }
}