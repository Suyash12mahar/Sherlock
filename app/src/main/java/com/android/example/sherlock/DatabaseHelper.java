package com.android.example.sherlock;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Suyash on 26-11-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private String db_path = "/data/data/com.android.example.sherlock/";
    private static String db_name = "SherlockDatabase.sqlite";
    private SQLiteDatabase myDatabase;
    public final Context myContext;
    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String my_path = db_path + db_name;
            checkDB = SQLiteDatabase.openDatabase(my_path, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            
        }
        if (checkDB != null) checkDB.close();
        return ((checkDB != null));
    }

    private void copyDatabase() throws IOException{
        InputStream myInput=myContext.getAssets().open(db_name);
        String outFileName = db_path + db_name;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[102400];
        int length;
        while ((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);

        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void openDatabase() throws SQLException {
        String my_path = db_path + db_name;
        myDatabase = SQLiteDatabase.openDatabase(my_path,null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void ExeSQLData(String sql) throws SQLiteException{
        myDatabase.execSQL(sql);
    }

    public Cursor QueryData(String query) throws SQLException{
        return myDatabase.rawQuery(query, null);
    }

    @Override
    public synchronized void close() {
        if (myDatabase !=null)
            myDatabase.close();
        super.close();
    }
    public void checkAndCopyDatabase(){
        boolean dbExists = checkDatabase();
        if (dbExists){
            Log.d("TAG", "Database already exists");
        }
        else{
            this.getReadableDatabase();
            try{
                copyDatabase();
            }catch(IOException e){
                Log.d("TAG", "IO Error");
            }
        }
    }
}
