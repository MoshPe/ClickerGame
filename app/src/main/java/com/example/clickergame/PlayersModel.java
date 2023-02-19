package com.example.clickergame;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayersModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Player>> playersLiveData;
    MutableLiveData<Integer> itemSelectedLive;
    MutableLiveData<Player> playerLiveData;
    Integer itemSelected;
    ArrayList<Player> playersList;
    Player player;
    private final DatabaseReference database;
    private final Context context;

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
        this.context = app.getApplicationContext();
    }

    public void initPlayersList() {
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

    public void getSPPlayer(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String playerKey = sharedPreferences.getString("playerKey", null);
        if (playerKey != null){
            for(Player player: this.playersList)
                if (player.getKey() != null && player.getKey().equals(playerKey)){
                    this.player = player;
                    this.playersList.remove(player);
                    this.playersList.add(0, player);
                    this.playerLiveData.setValue(this.playersList.get(0));
                    this.player.setMyState(Finals.State.ACTIVE);
                    onPauseUpdatePlayer();
                    break;
                }
        }
    }

    private void pullData(DataSnapshot dataSnapshot){
        this.playersList = new ArrayList<>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Player temp = postSnapshot.getValue(Player.class);
            if (this.player.getKey() != null && temp.isEqual(this.player))
                this.playersList.add(0, new Player(temp));
            else if (this.player.getKey() != null)
                this.playersList.add(new Player(temp));
        }
        if (!this.playersList.isEmpty())
            this.playerLiveData.setValue(this.playersList.get(0));
        this.playersLiveData.setValue(this.playersList);
        getSPPlayer(this.context);
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

    public void setPlayer(Context context, Player player) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.player = player;
        this.player.setId(this.playersList.size() + 1);
        this.player.setVisibility(false);
//        this.playerLiveData.setValue(this.player);
        if (sharedPreferences.contains("playerKey")) {
            return;
        }
        // add player to db
        String key = this.database.push().getKey(); // generate unique key
        player.setKey(key);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("playerKey", key);
        editor.commit();
        this.database.child(player.getKey()).setValue(player);
    }

    public void onPauseUpdatePlayer() {
        if (player != null && player.getKey() != null)
            this.database.child(player.getKey()).child("myState").setValue(player.getMyState());
    }

    public void removePlayer(Player player){
        if (player.getKey().equals(this.player.getKey())){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("playerKey");
            editor.commit();
        }
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("playerKey");
            editor.commit();
        }
    }
}
