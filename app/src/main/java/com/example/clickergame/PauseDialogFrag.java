package com.example.clickergame;

import static com.example.clickergame.Finals.PLAYER_NAME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class PauseDialogFrag extends DialogFragment  {
    private Player player;

    public PauseDialogFrag() {
    }

    public static PauseDialogFrag newInstance(Player player) {
        PauseDialogFrag fragment = new PauseDialogFrag();
        Bundle args = new Bundle();
        args.putSerializable(PLAYER_NAME, player);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.player = (Player) getArguments().getSerializable(PLAYER_NAME);
        } else {
            this.player = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.paused_game);
        dialogBuilder.setIcon(R.drawable.pause_icon);
        PlayersModel viewModel = new ViewModelProvider(getActivity()).get(PlayersModel.class);
        dialogBuilder.setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    //TODO change player cube to white
                    player.setMyState(Finals.State.ACTIVE);
                    viewModel.onPauseUpdatePlayer();
                    dialog.dismiss();
                }
            }
        });

        dialogBuilder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO change player cube to red and set player to inactive in DB
                viewModel.removePlayer(player);
//                getActivity().finish();
//                System.exit(0);
                getFragmentManager().popBackStackImmediate("BBB", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return dialogBuilder.create();
    }
}