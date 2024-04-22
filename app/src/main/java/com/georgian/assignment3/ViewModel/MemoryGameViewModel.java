package com.georgian.assignment3.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.georgian.assignment3.Model.MemoryGame;

public class MemoryGameViewModel extends ViewModel {

    private final MemoryGame memoryGame = new MemoryGame();

    public MemoryGameViewModel() {
    }

    public LiveData<String> getName() {
        return memoryGame.getName();
    }

    public void setName(String name){
        memoryGame.setName(name);
    }

    public LiveData<Integer> getScore() {
        return memoryGame.getScore();
    }

    public void setScore(Integer score){
        memoryGame.setScore(score);
    }

}