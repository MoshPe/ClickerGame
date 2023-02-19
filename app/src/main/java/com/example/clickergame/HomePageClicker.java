package com.example.clickergame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class HomePageClicker extends Fragment implements View.OnClickListener  {
    private FragHomePageListener listener;


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
        EditText playerNameET = view.findViewById(R.id.editTextTextPersonName);

        ((Button) view.findViewById(R.id.join_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClickJoinGame(playerNameET.getText().toString());
            }
        });
        ((Button) view.findViewById(R.id.exit_btn)).setOnClickListener(this);
        Context context =  getContext();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean savedCountries = sharedPreferences.getBoolean("removed_countries", false);
////        if (savedCountries){
////            this.countryList = parseCountries(ALL_COUNTRIES, context);
////            Set<String> countries = sharedPreferences.getStringSet("savedCountries", new HashSet<String>());
////            this.countryList.removeIf(country -> !countries.contains(country.getName()));
////        } else
////            this.countryList = parseCountries(ALL_COUNTRIES, context);
//        if (savedCountries){
//            this.countryList = importCountries(context, SAVED_COUNTRIES);
//        }
//        else
//            this.countryList = parseCountries(ALL_COUNTRIES, context);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
        System.exit(0);
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