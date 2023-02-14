package com.example.clickergame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PauseDialogFrag extends DialogFragment  {

    public PauseDialogFrag() {
    }

    public static PauseDialogFrag newInstance() {
        PauseDialogFrag fragment = new PauseDialogFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            progress = getArguments().getInt(PROGRESS);
//        } else {
//            progress = 0;
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.paused_game);
        dialogBuilder.setIcon(R.drawable.pause_icon);
//        View view = getActivity().getLayoutInflater().inflate(R.layout.pause_dialog_layout, null, false);
//        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    //TODO change player cube to white
                    dialog.dismiss();
                }
            }
        });

        dialogBuilder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO change player cube to red and set player to inactive in DB
                getActivity().finish();
                System.exit(0);
            }
        });

        return dialogBuilder.create();
    }
}