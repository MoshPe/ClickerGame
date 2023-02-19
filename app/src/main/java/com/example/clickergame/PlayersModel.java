package com.example.clickergame;

import com.example.clickergame.Finals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayersModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Player>> playersLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    MutableLiveData<Player> playerLiveData;
    Integer itemSelected;
    ArrayList<Player> playersList;
    Player player;
    private final DatabaseReference database;

    public PlayersModel(Application app) {
        super(app);
        this.playersLiveData = new MutableLiveData<>();
        this.itemSelectedLive = new MutableLiveData<>();
        this.playerLiveData = new MutableLiveData<>();
        this.itemSelected = RecyclerView.NO_POSITION;
        this.itemSelectedLive.setValue(this.itemSelected);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.database = FirebaseDatabase.getInstance().getReference("players");
        this.database.keepSynced(true);
        this.playersList = new ArrayList<Player>();
    }

    public void initPlayersList(Context context) {
        this.playersLiveData.setValue(playersList);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pullData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        database.limitToFirst(16).addValueEventListener(postListener);

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

    private void pullData(DataSnapshot dataSnapshot){
        this.playersList = new ArrayList<>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Player temp = postSnapshot.getValue(Player.class);
            if (temp.isEqual(this.player))
                this.playersList.add(0, new Player(temp));
            else
                this.playersList.add(new Player(temp));
        }
        if (!this.playersList.isEmpty())
            this.playerLiveData.setValue(this.playersList.get(0));
        this.playersLiveData.setValue(this.playersList);
    }

    public MutableLiveData<ArrayList<Player>> getPlayersLiveData() {
        return this.playersLiveData;
    }

    public MutableLiveData<Player> getPlayerLiveData() {
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

    public void setPlayer(Player player) {
        this.player = player;
        this.player.setId(this.playersList.size() + 1);
        this.player.setVisibility(false);
        // add player to db
        String key = this.database.push().getKey(); // generate unique key
        player.setKey(key);
        this.database.child(player.getKey()).setValue(player);
    }
    public void onPauseUpdatePlayer() {
        this.database.child(player.getKey()).child("myState").setValue(player.getMyState());
    }

    public void removePlayer(Player player){
        this.database.child(player.getKey()).removeValue();
    }

    public void increasePlayerScore(Player player){
        this.database.child(player.getKey()).child("score").setValue(ServerValue.increment(1));
    }

    public void decreasePlayerScore(Player player){
        this.database.child(player.getKey()).child("score").setValue(ServerValue.increment(-1));
    }

    public Player getPlayer(int position){
        return this.playersList.get(position);
    }

    public Player getMyPlayer(){
        return this.player;
    }

    public void setMyPlayerVisibility(boolean visibility){
        this.player.setVisibility(visibility);
    }

    public boolean getMyPlayerVisibility() {
        return this.player.isVisibility();
    }

    public void resetGame() {
        if (!playersList.isEmpty()){
            this.database.removeValue();
            playersList.clear();
        }
    }
}
