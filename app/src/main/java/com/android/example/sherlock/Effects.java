package com.android.example.sherlock;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Suyash on 30-12-2016.
 */

public class Effects {
    public static void dsc_expand(Context context, View view){
    Animation a = AnimationUtils.loadAnimation(context, R.anim.dsc_show_anim);
        if (a!= null){
            a.reset();
            if (view != null){
                view.clearAnimation();
                view.startAnimation(a);
            }
        }

    }
    public static void dsc_collapse(Context context, View view){
        Animation a = AnimationUtils.loadAnimation(context, R.anim.dsc_hide_anim);
        if (a!= null){
            a.reset();
            if (view != null){
                view.clearAnimation();
                view.startAnimation(a);
            }
        }

    }
}
