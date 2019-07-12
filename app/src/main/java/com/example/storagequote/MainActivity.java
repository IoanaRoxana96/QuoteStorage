package com.example.storagequote;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.print.PrinterId;
import android.provider.BaseColumns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    DBHelper mydb;

    EditText quote;
    TextView result;
    Button addQuote, deleteQuote, viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllQuotes();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        obj = (ListView) findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent (getApplicationContext(), DisplayQuote.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        quote = (EditText) findViewById(R.id.quote);
        result = (TextView) findViewById(R.id.result);
        addQuote = (Button) findViewById(R.id.addQuote);
        deleteQuote = (Button) findViewById(R.id.deleteQuote);
        viewAll = (Button) findViewById(R.id.viewAll);

        addQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quotes = quote.getText().toString();
                result.setText("Quote: " + quotes);
            }
        });

        deleteQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quote.setText("");
                result.setText("");
                quote.requestFocus();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item1: Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", 0);

            Intent intent = new Intent (getApplicationContext(), DisplayQuote.class);
            intent.putExtras(dataBundle);

            startActivity(intent);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown (int keycode, KeyEvent event) {
        if(keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }


}
