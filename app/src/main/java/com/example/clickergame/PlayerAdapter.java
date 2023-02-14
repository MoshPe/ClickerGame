package com.example.clickergame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private final Context context;
    private final PlayersModel viewModel;
    private ArrayList<Player> playersList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean isSelected;

    public PlayerAdapter(Context context, FragmentActivity activity, PlayersModel viewModel) {
        this.viewModel = viewModel;
        this.context = context;

        viewModel.getPlayerLiveData().observe(activity, new Observer<ArrayList<Player>>() {
            @Override
            public void onChanged(ArrayList<Player> players) {
                setPlayersList(players);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.layout_player_cube, parent, false);
        return new ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = playersList.get(position);
        this.selectedPosition = this.viewModel.getPosition();
        holder.itemView.setBackgroundResource(R.color.white);
/*        if (this.selectedPosition == position)
            holder.itemView.setBackgroundResource(R.color.white);
        else
            holder.itemView.setBackgroundResource(R.color.transparent);*/
        holder.setPlayer(position, player);
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

    public void setPlayersList(ArrayList<Player> playersList){
        this.playersList = playersList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView score;
        private final TextView name;
        private final View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            score = (TextView) itemView.findViewById(R.id.score);
            name = (TextView) itemView.findViewById(R.id.name);
            this.view = itemView;
        }

        public void setPlayer(int position, Player player){
            this.name.setText(player.getName());
            this.score.setText(Integer.toString(player.getScore()));
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.setItemSelected(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
