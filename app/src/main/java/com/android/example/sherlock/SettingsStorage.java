package com.android.example.sherlock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Suyash on 05-01-2017.
 */

public class SettingsStorage  extends Activity {
    public static boolean isCurrentPageBookmarked(String sourceString, int seriesNumber, int episodeNumber){
        String[] split = sourceString.split(";");

        String stringToFind = "(" + seriesNumber + "," + episodeNumber + ")";
        for (int i = 0; i < split.length; i++){
            if (split[i].equals(stringToFind)){
                return true;
            }
        }

        return false;
    }

    public static String AddString(String existingString, int seriesNumber, int episodeNumber){
        String[] split = existingString.split(";");

        //ArrayList list = (ArrayList) Arrays.asList(split);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));

        String stringToAdd = "(" + seriesNumber + "," + episodeNumber + ")";

        if (!list.contains(stringToAdd)){
            list.add(stringToAdd);
        }

        String outputString = "";
        for (int i = 0; i < list.size(); i++){
            outputString += list.get(i) + ";";
        }
        return outputString;
    }
    public static String deleteFromString(String existingString, int seriesNumber, int episodeNumber){
        String[] split = existingString.split(";");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        String stringToRemove = "(" + seriesNumber + "," + episodeNumber + ")";

        try{
            list.remove(stringToRemove);
        } catch (Exception e){
            Log.i("SettingsStorage","Unable to delete string");
        }

        String outputString = "";
        for (int i = 0; i < list.size(); i++){
            outputString += list.get(i) + ";";
        }
        return outputString;
    }
}
