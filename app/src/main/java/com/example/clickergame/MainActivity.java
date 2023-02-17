package com.example.clickergame;

import static com.example.clickergame.Finals.PLAYER_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements HomePageClicker.FragHomePageListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameBoardClicker gameBoardClicker = (GameBoardClicker) getSupportFragmentManager().findFragmentByTag("GameBoardClicker");

        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
            if (gameBoardClicker != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(gameBoardClicker)
                        .commit();
            }
            else {
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.fragmentDetailContainerView, gameBoardClicker.class,null, "GameBoardClicker")
//                        .commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }
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

    // func for pass data between the fragment
    @Override
    public void OnClickJoinGame(String playerName){
        Bundle bundle = new Bundle();
        bundle.putString(PLAYER_NAME, playerName);
//        Log.i("hello", playerName);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, GameBoardClicker.class, bundle,"GameBoardClicker")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        PlayersModel viewModel = new ViewModelProvider(this).get(PlayersModel.class);
        Player player = viewModel.getMyPlayer();
        player.setMyState(Finals.State.NOT_ACTIVE);
        viewModel.onPauseUpdatePlayer();
    }
}