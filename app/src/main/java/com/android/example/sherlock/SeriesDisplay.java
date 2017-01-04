package com.android.example.sherlock;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class SeriesDisplay extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExpandableSeriesAdapter adapter;
    private List<Series> seriesList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    public SeriesDisplay() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recList = (RecyclerView) findViewById(R.id.sd_recycler_view);

        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        adapter = new ExpandableSeriesAdapter(seriesList, this);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sd_about_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent newIntent = new Intent(SeriesDisplay.this, AboutActivity.class);
                startActivity(newIntent);
            }
        });

        populateList();

        adapter.notifyDataSetChanged();
    }
    private void populateList(){
        dbHelper = new DatabaseHelper(this);

        try{
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        }
        catch(SQLException e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        try{
            Cursor cursor = dbHelper.QueryData("SELECT * FROM series");
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        int seriesNumber = cursor.getInt(0);
                        String ratings = cursor.getString(1) ;
                        if (!ratings.equals("NA")) {
                            ratings += " \u2605";
                        }
                        String airDate = cursor.getString(2);
                        String imdbLink = cursor.getString(3);
                        String bbcLink = cursor.getString(4);
                        Series item = new Series(seriesNumber, ratings, airDate, imdbLink, bbcLink);
                        seriesList.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e){
            Log.e("LOG","Error");
            Log.e("LOG", e.getMessage());
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
