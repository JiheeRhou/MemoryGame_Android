package com.georgian.assignment3.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MemoryGame {

    // LiveData for storing the name
    private MutableLiveData<String> name = new MutableLiveData<>();

    // Default constructor
    public MemoryGame() {

    }

    // Constructor with name parameter
    public MemoryGame(String newName) {
        // Set the name value
        name.setValue(newName);
    }

    // Get the LiveData for the name
    public LiveData<String> getName() {
        return name;
    }

    // Set the name
    public void setName(String newName) {
        name.setValue(newName);
    }
}