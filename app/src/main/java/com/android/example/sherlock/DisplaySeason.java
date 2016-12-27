package com.android.example.sherlock;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplaySeason extends AppCompatActivity {
    ListView listView; // TODO remove objext reference to avoid runtime error
    Button newButton;
    ArrayList<SeasonItem> arrayList= new ArrayList<SeasonItem>();
    SeasonAdapter adapter;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_season);
        listView = (ListView) findViewById(R.id.displayListView);
        dbHelper = new DatabaseHelper(this);

        try{
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        }
        catch(SQLException e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        try{
            Cursor cursor = dbHelper.QueryData("SELECT * FROM seasons");
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        SeasonItem item = new SeasonItem();
                        item.setSeasonNumber(cursor.getString(0));
                        item.setYear(cursor.getString(1));
                        item.setRatings(cursor.getString(2));
                        arrayList.add(item);
                    }while (cursor.moveToNext());

                }
            }
        } catch (SQLiteException e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        adapter = new SeasonAdapter(this,R.layout.display_season_row, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(onItemClickListener);
        /*().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // This comment is of no use

            Intent newIntent = new Intent(DisplaySeason.this, Season1Activity.class);
            newIntent.putExtra("season_number", String.valueOf(id + 1));
            startActivity(newIntent);
        }
    };


}
