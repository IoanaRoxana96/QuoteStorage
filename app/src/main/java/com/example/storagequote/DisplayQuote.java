package com.example.storagequote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayQuote extends Activity {

    private DBHelper mydb;

    TextView quote;
    int id_To_Update = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quote);
        quote = (TextView) findViewById(R.id.quote);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int Value = ((Bundle) extras).getInt("id");

            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String quot = rs.getString(rs.getColumnIndex(DBHelper.QUOTES_COLUMN_QUOTES));

                if(!rs.isClosed())
                {
                    rs.close();
                }

                Button addQuote = (Button) findViewById(R.id.save);
                addQuote.setVisibility(View.INVISIBLE);

                quote.setText((CharSequence)quot);
                quote.setFocusable(false);
                quote.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            int Value = extras.getInt("id");
            if(Value > 0) {
                getMenuInflater().inflate(R.menu.display_quote, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Edit_Quote:
                Button addQuote = (Button) findViewById(R.id.addQuote);
                addQuote.setVisibility(View.VISIBLE);
                quote.setEnabled(true);
                quote.setFocusableInTouchMode(true);
                quote.setClickable(true);

                return true;
            case R.id.Delete_Quote:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.Delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteQuote(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted successfully",
                                       Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("are you sure?");
                d.show();

                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateQuote(id_To_Update, quote.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Not updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(mydb.insertQuote(quote.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Done",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
