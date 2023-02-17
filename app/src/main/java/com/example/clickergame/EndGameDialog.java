package com.example.clickergame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class EndGameDialog extends DialogFragment {
    private PlayersModel viewModel;
    private boolean isWin;

    public EndGameDialog() {
    }

    public static EndGameDialog newInstance(boolean isWin) {
        EndGameDialog fragment = new EndGameDialog();
        Bundle args = new Bundle();
        args.putBoolean("WIN", isWin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isWin = getArguments().getBoolean("WIN");
        } else {
            isWin = false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(PlayersModel.class);
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_end_game, null, false);
        if (this.isWin){
            dialogBuilder.setTitle(R.string.win);
            dialogBuilder.setIcon(R.drawable.party_emojii);
        }
        else{
            dialogBuilder.setTitle(R.string.lost);
            dialogBuilder.setIcon(R.drawable.sad_emojii);
            ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.try_again);
        }
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    //TODO change player cube to white
                    dialog.dismiss();
                }
                //delete board
            }
        });

        dialogBuilder.setNegativeButton(R.string.return_home, new DialogInterface.OnClickListener() {
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
