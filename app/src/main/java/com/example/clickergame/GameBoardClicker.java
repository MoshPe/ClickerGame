package com.example.clickergame;

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


public class GameBoardClicker extends Fragment {
    private PlayersModel viewModel;

    public GameBoardClicker() {
        // Required empty public constructor
    }

    public static GameBoardClicker newInstance(String param1, String param2) {
        GameBoardClicker fragment = new GameBoardClicker();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.layout_game_board, container, false);
    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showInstructionsDialog();
        RecyclerView rvCountries = (RecyclerView) view.findViewById(R.id.countryRec);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayersModel.class);
        PlayerAdapter adapter = new PlayerAdapter(view.getContext(), getActivity(), viewModel);
        rvCountries.setAdapter(adapter);
        rvCountries.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
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
}