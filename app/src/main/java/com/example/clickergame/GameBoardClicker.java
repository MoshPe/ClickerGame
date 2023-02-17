package com.example.clickergame;

import static com.example.clickergame.Finals.PLAYER_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class GameBoardClicker extends Fragment implements PlayerAdapter.EndGameListener {
    private PlayersModel viewModel;
    private String playerName = "";

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
                showPauseDialog();
                Player player = viewModel.getMyPlayer();
                player.setMyState(Finals.State.SUSPEND);
                viewModel.onPauseUpdatePlayer();
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
        Log.i("hello", String.valueOf(savedInstanceState));
        showInstructionsDialog();
        Player player = new Player(this.playerName, Finals.PLAYER_INIT_SCORE, 0);
        RecyclerView rvCountries = (RecyclerView) view.findViewById(R.id.playersRec);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayersModel.class);
        viewModel.initPlayersList(view.getContext());
        viewModel.setPlayer(player);
        PlayerAdapter adapter = new PlayerAdapter(this, view.getContext(), getActivity(), viewModel);
        rvCountries.setAdapter(adapter);
        rvCountries.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
    }

    public void openDialog(boolean isWin){
        showEndGameDialog(isWin);
    }

    private void showPauseDialog() {
        FragmentManager fm = getParentFragmentManager();
        //TODO send player instance to pause frag for changing the background
        PauseDialogFrag editNameDialogFragment = PauseDialogFrag.newInstance();
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
    public void showEndGameDialog(boolean isWin) {
        EndGameDialog endGameFrag = (EndGameDialog) getParentFragmentManager().findFragmentByTag("End Game dialog");
        if (endGameFrag != null)
            return;
        FragmentManager fm = getParentFragmentManager();
        //TODO send player instance to pause frag for changing the background
        EndGameDialog editNameDialogFragment = EndGameDialog.newInstance(isWin);
        editNameDialogFragment.setTargetFragment(this, 300);
        editNameDialogFragment.show(fm, "End Game dialog");
    }
}