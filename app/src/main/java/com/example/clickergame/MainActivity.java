package com.example.clickergame;

import static com.example.clickergame.Finals.PLAYER_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire;

public class MainActivity extends AppCompatActivity implements HomePageClicker.FragHomePageListener {
    private NetworkBroadcastReceiver r = new NetworkBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        GameBoardClicker gameBoardClicker = (GameBoardClicker) getSupportFragmentManager().findFragmentByTag("GameBoardClicker");
//
//        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
//            if (gameBoardClicker != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .show(gameBoardClicker)
//                        .commit();
//            } else {
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.fragmentContainerView, HomePageClicker.class,null, "HomePage")
//                        .commit();
//            }
//            getSupportFragmentManager().executePendingTransactions();
//        }
//        checkNetwork();
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
        Log.i("hello on join", "create gameboard");
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
            player.setMyState(Finals.State.NOT_ACTIVE);
            viewModel.onPauseUpdatePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(r, i);
        PlayersModel viewModel = new ViewModelProvider(this).get(PlayersModel.class);
        Player player = viewModel.getMyPlayer();
        if (player != null && player.getKey() != null) {
            player.setMyState(Finals.State.ACTIVE);
            viewModel.onPauseUpdatePlayer();
        }
    }

    public void changeTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = preferences.getBoolean("switch_theme", false);
        Log.i("theme:", "get theme bool from main activity:"+useDarkTheme);
        if (useDarkTheme) {
            setTheme(R.style.Theme_ClickerGame_Dark);
        }
    }
}
