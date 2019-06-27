package mestrado.ipg.condomastercrud;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends ListActivity {
    ListAdapter adapter;
    DBAdapter datasource;
    Button btNovoContacto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new DBAdapter(this);
        datasource.open();
        Cursor cursor = datasource.getAssembleias();

        String[] columns = new String[]{"_title", "_description"};
        int[] to = new int[]{R.id._title, R.id._description};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.assembleia_list_item,
                cursor,
                columns,
                to);
        this.setListAdapter(adapter);
        datasource.close();

        btNovoContacto = findViewById(R.id.btNovoContacto);


        btNovoContacto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent novo = new Intent(MainActivity.this, NovoAssembleia.class);
                startActivity(novo);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        datasource.open();
        Cursor cursor = datasource.getAssembleias();
        String[] columns = new String[]{"_title", "_description"};
        int[] to = new int[]{R.id._title, R.id._description};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.assembleia_list_item,
                cursor,
                columns,
                to);
        this.setListAdapter(adapter);
        datasource.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(MainActivity.this, AssembleiaDetalhes.class);
        Cursor cursor = (Cursor) adapter.getItem(position);
        intent.putExtra("idAssembleia", cursor.getInt(cursor.getColumnIndex("_id")));
        startActivity(intent);
    }
}
