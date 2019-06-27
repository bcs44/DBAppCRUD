package mestrado.ipg.condomastercrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NovoAssembleia extends Activity {

    Button saveDateBTN;
    private DBAdapter datasource;
    Spinner spinner;
    String placeDesc, placeId;
    EditText etInitialDate, etInitialTime, etDesc, etTitle;
    Calendar myCalendarInitial = Calendar.getInstance();
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_assembleia);

        saveDateBTN = findViewById(R.id.saveDateBTN);
        spinner = findViewById(R.id.spinner1);
        etInitialDate = findViewById(R.id.initialDate);
        etInitialTime = findViewById(R.id.initialTime);
        etDesc = findViewById(R.id.desc);
        etTitle = findViewById(R.id.title);

        datasource = new DBAdapter(this);

        final DatePickerDialog.OnDateSetListener initialDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarInitial.set(Calendar.YEAR, year);
                myCalendarInitial.set(Calendar.MONTH, monthOfYear);
                myCalendarInitial.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        etInitialDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NovoAssembleia.this, initialDate, myCalendarInitial
                        .get(Calendar.YEAR), myCalendarInitial.get(Calendar.MONTH),
                        myCalendarInitial.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etInitialTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NovoAssembleia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String toET = selectedHour + ":" + selectedMinute;
                        etInitialTime.setText(toET);
                        myCalendarInitial.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendarInitial.set(Calendar.MINUTE, selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        saveDateBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datasource.open();

                Date date = new Date(myCalendarInitial.getTimeInMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                String user_id = user.getUser_id();
                String meeting_date = format.format(date);
                String place_id = placeId;
                String description = String.valueOf(etDesc.getText());
                String title = String.valueOf(etTitle.getText());

                Assembleia assembleia = datasource.createAssembleia(user_id, meeting_date, place_id, description, title);

                datasource.close();
                AlertDialog.Builder dialogo = new
                        AlertDialog.Builder(NovoAssembleia.this);
                dialogo.setTitle("Aviso");
                dialogo.setMessage("Assembleia Criada: " + assembleia.getTITLE());
                dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                dialogo.show();
            }
        });

        registerReceiver();
        getPlaces();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        etInitialDate.setText(sdf.format(myCalendarInitial.getTime()));
    }

    private void registerReceiver() {

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String data = intent.getStringExtra("data");
                String wherefrom = intent.getStringExtra("wherefrom");

                switch (wherefrom) {
                    case "getPlaces":
                        context.stopService(new Intent(context, BackgroundGetServiceAuth.class));
                        dealWithSpinners(data);
                        break;
                }
                intent.getBundleExtra("Location");
            }
        };

        LocalBroadcastManager.getInstance(NovoAssembleia.this).registerReceiver(
                mMessageReceiver, new IntentFilter("ServiceMain"));
    }

    private void getPlaces() {

        Intent intent = new Intent(NovoAssembleia.this, BackgroundGetServiceAuth.class);
        intent.putExtra("urlStrg", "https://bd.ipg.pt:5500/ords/bda_1701887/place/all");
        intent.putExtra("_uri", "/place/all");
        intent.putExtra("wherefrom", "getPlaces");
        startService(intent);
    }

    public void dealWithSpinners(String data) {

        final SpinAdapter adapter;

        JSONObject json;
        JSONArray array;
        Place[] places = new Place[0];

        try {
            json = new JSONObject(data);
            array = json.getJSONArray("response");
            places = new Place[array.length()];

            for (int i = 0; i < array.length(); ++i) {
                json = array.getJSONObject(i);
                if (json != null) {
                    places[i] = new Place();
                    places[i].setId(json.getString("place_id"));
                    places[i].setDesc(json.getString("description"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SpinAdapter(NovoAssembleia.this,
                android.R.layout.simple_spinner_item,
                places);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int checkTemp = 0;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (++checkTemp > 1) {
                    Place place = adapter.getItem(position);
                    if (place != null) {
                        placeDesc = place.getDesc();
                        placeId = place.getId();
                        Toast.makeText(NovoAssembleia.this, "ID: " + place.getId() + "\nDesc: " + place.getDesc(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }
}