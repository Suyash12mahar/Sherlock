package com.android.example.sherlock;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Season1Activity extends AppCompatActivity {
    private int seasonNumber;

    ListView listView;
    ArrayList<EpisodeItem> arrayList= new ArrayList<EpisodeItem>();
    EpisodeAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-- following lines hides title bar
            //getWindow().getDecorView().setSystemUiVisibility(
            //        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            //                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            //                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            //                | View.SYSTEM_UI_FLAG_FULLSCREEN
            //                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //--

        //-- Standard code
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_season1);
        //--

        listView = (ListView) findViewById(R.id.episode_list_view);
        Bundle extras = getIntent().getExtras();

        seasonNumber = Integer.parseInt(extras.getString("season_number"));
        dbHelper = new DatabaseHelper(this);


        //-- updates season number in season_title_text_view
            TextView titleText = (TextView) findViewById(R.id.season_title_text_view);
            titleText.setText("Season " + seasonNumber);
        //--

        try{
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        }
        catch(SQLException e){
            Log.d("TAG", "Error accessing database");//MinGW
        }


        String queryString = String.format(
                "SELECT * FROM \"EpisodeDetails\" WHERE SeasonNumber=\"%d\"",seasonNumber);

        try{
            Cursor cursor = dbHelper.QueryData(queryString);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        EpisodeItem item = new EpisodeItem();
                        item.setEpTitle(cursor.getInt(2)  + ". " + cursor.getString(3));
                        item.setAirDate(cursor.getString(10));
                        item.setShortDescription(cursor.getString(11));

                        String imageName = String.format("img_%d_%d", seasonNumber, Integer.parseInt(item.getEpTitle().charAt((0)) + "") );
                        int resourceId = -1;
                        resourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
                        if (resourceId != 0) {
                            item.setImageId(this.getResources().getIdentifier(imageName, "drawable", this.getPackageName()));
                        } else {
                            item.setImageId(R.drawable.thumbnail_extra_wide_png);
                        }

                        item.setViews(cursor.getString(5));
                        arrayList.add(item);
                    }while (cursor.moveToNext());

                }
            }
        } catch (SQLiteException e){
            Log.d("TAG", "Error accessing database 2");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        adapter = new EpisodeAdapter(this,R.layout.activity_episode_item, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(onItemClickListenerHell);
        }

    private AdapterView.OnItemClickListener onItemClickListenerHell = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // This comment is of no use
            Intent newerIntent = new Intent(Season1Activity.this, EpisodesDetails.class);
            newerIntent.putExtra("season_number", String.valueOf(seasonNumber));
            newerIntent.putExtra("episode_number", String.valueOf(id+1));
            startActivity(newerIntent);
        }
    };
}
