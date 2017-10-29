package com.example.rlopatka.kotlinsamples;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

/**
 * Created by rlopatka on 29.10.2017.
 */

public class JavaDialog {
    public void show(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Dialog messagae").setTitle("Dialog title");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
