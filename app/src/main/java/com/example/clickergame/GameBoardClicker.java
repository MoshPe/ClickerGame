package com.example.clickergame;

import static com.example.clickergame.Finals.IS_NEW_PLAYER;
import static com.example.clickergame.Finals.PLAYER_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class GameBoardClicker extends Fragment implements EndGameDialog.EditSeekBarProgressListener {
    private PlayersModel viewModel;
    private String playerName = "";
    private boolean isNewPlayer;

    public GameBoardClicker() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pause_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pause:
                Player player = viewModel.getMyPlayer();
                player.setMyState(Finals.State.SUSPEND);
                viewModel.onPauseUpdatePlayer();
                showPauseDialog(player);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.playerName = getArguments().getString(PLAYER_NAME);
        return inflater.inflate(R.layout.layout_game_board, container, false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Finals.isDialogShown = false;
        showInstructionsDialog();
        Player player = new Player(this.playerName, Finals.PLAYER_INIT_SCORE, 0);
        RecyclerView rvCountries = (RecyclerView) view.findViewById(R.id.playersRec);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayersModel.class);
        viewModel.initPlayersList();
        viewModel.setPlayer(player);
        PlayerAdapter adapter = new PlayerAdapter(view.getContext(), getActivity(), viewModel);
        rvCountries.setAdapter(adapter);
        rvCountries.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
    }

    private void showPauseDialog(Player player) {
        FragmentManager fm = getParentFragmentManager();
        //TODO send player instance to pause frag for changing the background
        PauseDialogFrag editNameDialogFragment = PauseDialogFrag.newInstance(player);
        editNameDialogFragment.setTargetFragment(this, 300);
        editNameDialogFragment.show(fm, "Pause dialog");
    }

    private void showInstructionsDialog() {
        FragmentManager fm = getParentFragmentManager();
        //TODO send player instance to pause frag for changing the background
        GameInstructionsDialog editNameDialogFragment = GameInstructionsDialog.newInstance();
        editNameDialogFragment.setTargetFragment(this, 300);
        editNameDialogFragment.show(fm, "Instructions dialog");
    }

    @Override
    public void onFinishSeekBarDialog() {
        getParentFragmentManager().popBackStackImmediate("BBB", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}