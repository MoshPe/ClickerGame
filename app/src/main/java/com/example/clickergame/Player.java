package com.example.clickergame;

public class Player {
    private String name;
    private int id;
    private int score;
    private Finals.State myState;

    public Player(String name, int score, int id) {
        this.name = name;
        this.score = score;
        this.id = id;
        this.myState = Finals.State.ACTIVE;
    }

    public Player() {}

    public void increaseScore() {
        this.score++;
    }

    public void decreaseScore() {
        this.score--;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Finals.State getMyState() {
        return myState;
    }

    public void setMyState(Finals.State myState) {
        this.myState = myState;
    }

    public int compare(Player other) {
        return Integer.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return name;
    }
}
