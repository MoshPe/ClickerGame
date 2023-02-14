package com.example.clickergame;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PlayersModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Player>> playerLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    Integer itemSelected;
    ArrayList<Player> playersList;

    public PlayersModel(Application app) {
        super(app);
        this.playerLiveData = new MutableLiveData<>();
        this.itemSelectedLive = new MutableLiveData<>();
        initPlayersList(app);

        this.itemSelected = RecyclerView.NO_POSITION;
        this.itemSelectedLive.setValue(this.itemSelected);

    }

    public void initPlayersList(Application app){
        playersList = new ArrayList<Player>();
        playersList.add(new Player("player", 5, "id"));
//        Context context =  app.getApplicationContext();
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
        this.playerLiveData.setValue(this.playersList);
    }

    public void setSPPlayers(Context context){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean savedCountries = sharedPreferences.getBoolean("removed_countries", false);
////        if (savedCountries){
////            ArrayList<String> countryNames = (ArrayList<String>) this.countryList.stream()
////                    .map(object -> object.getName())
////                    .collect(Collectors.toList());
////            SharedPreferences.Editor editor = sharedPreferences.edit();
////            editor.putStringSet("savedCountries", new HashSet(countryNames));
////            editor.commit();
////        }
//        if (savedCountries){
//            exportCountries(context,this.countryList);
//        }
    }

    public MutableLiveData<ArrayList<Player>> getPlayerLiveData() {
        return this.playerLiveData;
    }

    public MutableLiveData<Integer> getItemSelected() {
        return this.itemSelectedLive;
    }

    public void setItemSelected(int position){
        this.itemSelected = position;
        this.itemSelectedLive.setValue(this.itemSelected);
    }

    public int getPosition(){
        return this.itemSelected;
    }

    public void removePlayer(int position){
        playersList.remove(position);
        playerLiveData.setValue(playersList);
    }

    public Player getPlayer(int position){
        return this.playersList.get(position);
    }
}
