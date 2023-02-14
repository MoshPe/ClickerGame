package com.example.clickergame;

public class Player {
    private String name;
    private String id;
    private int score;

    public Player(String name, int score, String id) {
        this.name = name;
        this.score = score;
        this.id = id;
    }

    public Player() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compare(Player other) {
        return Integer.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return name;
    }
}
