package com.example.clickergame;

import static com.example.clickergame.Finals.IS_NEW_PLAYER;
import static com.example.clickergame.Finals.PLAYER_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements HomePageClicker.FragHomePageListener {
    private NetworkBroadcastReceiver r = new NetworkBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // manu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        GameSettings prefFrag = (GameSettings) getSupportFragmentManager().findFragmentByTag("prefFrag");
        if (prefFrag != null)
            return true;
        if (item.getItemId() == R.id.settings) {
            showGameSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGameSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(android.R.id.content, new GameSettings(), "prefFrag")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnClickJoinGame(String playerName) {
        Bundle bundle = new Bundle();
        bundle.putString(PLAYER_NAME, playerName);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, GameBoardClicker.class, bundle, "GameBoardClicker")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(r);
        PlayersModel viewModel = new ViewModelProvider(this).get(PlayersModel.class);
        Player player = viewModel.getMyPlayer();
        if (player != null) {
            Gson gson = new Gson();
            String playerJson = gson.toJson(player);
            player.setMyState(Finals.State.NOT_ACTIVE);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("playerKey", playerJson);
            editor.commit();
            viewModel.onPauseUpdatePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(r, i);
    }
}
