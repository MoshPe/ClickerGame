package com.example.clickergame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class HomePageClicker extends Fragment implements View.OnClickListener, PauseDialogFrag.resetHomeBtns {
    private FragHomePageListener listener;
    private EditText playerNameET;
    private TextView setName;
    private Button joinGame;


    public HomePageClicker() {
        // Required empty public constructor
    }


    public static HomePageClicker newInstance(String param1, String param2) {
        HomePageClicker fragment = new HomePageClicker();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // check that we create instance of the listener
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (FragHomePageListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragHomePageListener'");
        }
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_home_page_clicker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context =  getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.playerNameET = view.findViewById(R.id.editTextTextPersonName);
        String playerKey = sharedPreferences.getString("playerKey", null);
        setName = view.findViewById(R.id.textView3);
        joinGame = view.findViewById(R.id.join_btn);
        if (playerKey != null){
            listener.OnClickJoinGame(playerKey);
            playerNameET.setVisibility(View.GONE);
            setName.setVisibility(View.GONE);
            joinGame.setText("Resume Game");
        }


        ((Button) view.findViewById(R.id.join_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClickJoinGame(playerNameET.getText().toString());
            }
        });
        ((Button) view.findViewById(R.id.exit_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PlayersModel viewModel = new ViewModelProvider(this).get(PlayersModel.class);
        Player player = viewModel.getMyPlayer();
        if (player != null) {
            player.setMyState(Finals.State.NOT_ACTIVE);
            viewModel.onPauseUpdatePlayer();
        }
        getActivity().finish();
        System.exit(0);
    }

    @Override
    public void onResetHomeBtns() {
        playerNameET.setVisibility(View.VISIBLE);
        setName.setVisibility(View.VISIBLE);
        joinGame.setText("Join game");
    }


    public interface FragHomePageListener{
        void OnClickJoinGame(String playerName);
    }



    /*

    Having a continue button or create a new player

        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
     */
}