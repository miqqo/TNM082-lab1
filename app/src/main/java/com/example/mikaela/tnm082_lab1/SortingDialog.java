package com.example.mikaela.tnm082_lab1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by mikaela on 16-02-19.
 */
public class SortingDialog extends DialogFragment {
    final CharSequence[] items = {"Id","Title","Rating"};
    private int currentItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sorting");

        SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
        currentItem = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt("sort", 1);

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                currentItem = item;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);

                prefs.edit().putInt("sort", currentItem).commit();

                ((ItemListActivity)getActivity()).itemSort(currentItem);
            }
        });


        return builder.create();
    }


}