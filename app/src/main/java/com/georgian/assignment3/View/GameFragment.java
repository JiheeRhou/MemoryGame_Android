package com.georgian.assignment3.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.georgian.assignment3.databinding.FragmentGameBinding;
import com.georgian.assignment3.ViewModel.MemoryGameViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameFragment extends Fragment {

    // Player's name
    String playerName = "";

    // Flag for the game is active or not
    boolean isGameOn = false;

    // Flag for player's turn or not
    boolean isPlayerTurn = false;

    // Flag for the player's tap the tile is match or not
    boolean isMatch = true;

    // Flag for the timer is running or not
    boolean isTimerRunning = false;

    // Number of hidden tiles
    int hiddenTiles = 4;

    // Round
    int round = 1;

    // Number of matched tiles
    int hits = 0;

    // Game score
    int score = 0;

    // Set to store random numbers
    Set<Integer> randomNumbers = new HashSet<>();

    // Set to store matched tiles
    Set<Integer> matchedTiles = new HashSet<>();

    // View binding object
    private FragmentGameBinding binding;

    // List to store image views of tiles
    private List<ImageView> imageViews;

    // Countdown timer object
    private CountDownTimer countDownTimer;

    // View model for the memory game
    MemoryGameViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get player name from ViewModel and set the name
        viewModel = new ViewModelProvider(requireActivity()).get(MemoryGameViewModel.class);
        playerName = viewModel.getName().getValue();
        binding.textSavedName.setText(playerName);

        // Initialize list to store image views
        imageViews = new ArrayList<>();

        // Get references to image views
        getTiles(root);

        // Set OnClickListener for the tiles
        View.OnClickListener onClickTile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerTap(v);
            }
        };

        for (ImageView imageView : imageViews) {
            imageView.setOnClickListener(onClickTile);
        }

        // Set setOnClickListener for start button
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // START button click
                if (!isGameOn) {
                    // set the values default
                    binding.textGame.setText("");
                    binding.textTimer.setText("0");
                    binding.textScore.setText("0");
                    isGameOn = true;
                    hiddenTiles = 4;
                    round = 1;
                    hits = 0;

                    binding.textHits.setText(String.format("HITS: %d", hits));
                    binding.textRound.setText(String.format("ROUND: %d", round));
                    binding.textHiddenTiles.setText(String.format("HIDDEN TILES: %d", hiddenTiles));

                    // Start the game
                    startGame();
                }
                // STOP button click
                else {
                    // Stop the countdown timer
                    stopTimer();
                }
            }
        });

        return root;
    }

    // Start the game
    private void startGame(){
        // Change button text
        binding.buttonStart.setText("Stop");
        // Reset all tiles to default color
        resetTiles();

        if(isPlayerTurn) {
            setTiles(false);
            startTimer(10000); // 10 seconds
        }
        else {
            binding.textHits.setText(String.format("HITS: %d", hits));
            binding.textRound.setText(String.format("ROUND: %d", round));
            binding.textHiddenTiles.setText(String.format("HIDDEN TILES: %d", hiddenTiles));

            setTiles(true);
            startTimer(5000); // 5 seconds
        }
    }

    // Get references to image views
    private void getTiles(View view) {
        for (int i = 0; i < 36; i++) {
            int imageViewId = getResources().getIdentifier("imageView" + i, "id", requireContext().getPackageName());
            ImageView imageView = view.findViewById(imageViewId);
            imageViews.add(imageView);
        }
    }

    // Set the hidden tiles either hidden or shown
    private void setTiles(boolean showTiles) {
        if (showTiles) {
            Random rnd = new Random();

            // Generate random numbers between 0 and 35
            while (randomNumbers.size() < hiddenTiles) {
                int randomNumber = rnd.nextInt(36);
                randomNumbers.add(randomNumber);
            }

            // Set background color for hidden tiles
            for (int randomNumber : randomNumbers) {
                for (ImageView imageView : imageViews) {
                    Object tag = imageView.getTag();
                    int intTag = Integer.parseInt(tag.toString());
                    if (intTag == randomNumber) {
                        imageView.setBackgroundColor(Color.YELLOW);
                    }
                }
            }
        }
        else {
            // Reset all tiles to default color
            resetTiles();
        }
    }

    // Start the countdown timer
    private void startTimer(int milliseconds) {
        isTimerRunning = true;
        final int[] remainedSec = {milliseconds / 1000};

        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isGameOn) {
                    if (hits == hiddenTiles) {
                        onFinish();
                    }
                    else {
                        binding.textTimer.setText(remainedSec[0]+"");
                        remainedSec[0]--;
                    }
                }
            }

            @Override
            public void onFinish() {
                binding.textTimer.setText("0");

                if (isGameOn) {
                    if (isPlayerTurn) {
                        // Stop the countdown timer
                        stopTimer();

                        // The matched tiles less than the hidden tiles then game over
                        if (hits < hiddenTiles) {
                            // End the game
                            gameOver();
                        } else {
                            // clear the sets
                            matchedTiles.clear();
                            randomNumbers.clear();

                            // Add 5 points to score and set the score
                            score += 5;
                            binding.textScore.setText(String.format("%d", score));

                            // Reset hits
                            hits = 0;

                            // After each 3 rounds, increase the number of tiles by 1
                            if (round%3 == 0) {
                                hiddenTiles++;
                            }
                            // Increase the Round
                            round++;
                        }
                    }

                    // Switch turns
                    isPlayerTurn = !isPlayerTurn;
                    // Start the next game
                    startGame();
                }
            }
        }.start();
    }

    // Stop the countdown timer
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // The matched tiles less than the hidden tiles then game over
        if (hits < hiddenTiles){
            // End the game
            gameOver();
        }
    }

    //Handle player tap on tiles
    public void playerTap(View view) {
        // tile tag
        int tileIndex = Integer.parseInt(view.getTag().toString());
        // If the game is not on, not player's turn or player tap on the one of the matched tiles then return
        if (!isGameOn || !isPlayerTurn || matchedTiles.contains(tileIndex)) {
            return;
        }

        isMatch = false;
        if (randomNumbers.contains(tileIndex)) {
            view.setBackgroundColor(Color.YELLOW);
            isMatch = true;
            matchedTiles.add(tileIndex);
            hits++;
            binding.textHits.setText(String.format("HITS: %d", hits));
        }
        // Tap a wrong tile
        if (!isMatch) {
            // Stop the countdown timer
            stopTimer();
        }
    }

    // Reset all tiles to default color
    private void resetTiles() {
        for (ImageView imageView : imageViews) {
            imageView.setBackgroundColor(Color.parseColor("#CDDC39"));
        }
    }

    //End the game
    private void gameOver() {
        // Save the top 3 high scores
        SaveHighScores();

        // Reset all tiles to default color
        resetTiles();

        // clear the sets
        matchedTiles.clear();
        randomNumbers.clear();

        // Set the variables to default
        isGameOn = false;
        isPlayerTurn = false;
        isTimerRunning = false;
        isMatch = false;


        binding.buttonStart.setText("Start");   // Change button text
        binding.textGame.setText("GAME OVER");  // Display game over message
        Toast.makeText(getActivity(), "Game Over", Toast.LENGTH_LONG).show();
    }

    // Save the top 3 high scores
    private void SaveHighScores() {
        // Saves the player's scores in SharedPreferences
        if (playerName != null && !playerName.isEmpty()) {
            // Retrieving the value from SharedPref
            SharedPreferences sharedPreferences;
            sharedPreferences = this.requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Get existing high scores
            String playerNameFirst = sharedPreferences.getString("playerNameFirst","None");
            String scoreFirst = sharedPreferences.getString("scoreFirst","None");
            String playerNameSecond = sharedPreferences.getString("playerNameSecond","None");
            String scoreSecond = sharedPreferences.getString("scoreSecond","None");
            String playerNameThird = sharedPreferences.getString("playerNameThird","None");
            String scoreThird = sharedPreferences.getString("scoreThird","None");

            // Compare current score with existing high scores and update
            if (!playerNameFirst.equals("None") && !scoreFirst.equals("None")) {
                int s1 = Integer.parseInt(scoreFirst);
                // Update first place
                if (score > s1) {
                    playerNameThird = playerNameSecond;
                    scoreThird = scoreSecond;
                    playerNameSecond = playerNameFirst;
                    scoreSecond = scoreFirst;
                    playerNameFirst = playerName;
                    scoreFirst = String.valueOf(score);
                }
                else if (!playerNameSecond.equals("None") && !scoreSecond.equals("None")) {
                    int s2 = Integer.parseInt(scoreSecond);
                    // Update second place
                    if (score > s2) {
                        playerNameThird = playerNameSecond;
                        scoreThird = scoreSecond;
                        playerNameSecond = playerName;
                        scoreSecond = String.valueOf(score);
                    }
                    // Update third place
                    else if (!playerNameThird.equals("None") && !scoreThird.equals("None")) {
                        int s3 = Integer.parseInt(scoreThird);
                        if (score > s3) {
                            playerNameThird = playerName;
                            scoreThird = String.valueOf(score);
                        }
                    }
                    else {
                        playerNameThird = playerName;
                        scoreThird = String.valueOf(score);
                    }
                }
                else {
                    playerNameSecond = playerName;
                    scoreSecond = String.valueOf(score);
                }
            }
            else {
                playerNameFirst = playerName;
                scoreFirst = String.valueOf(score);
            }

            // Update SharedPreferences with new high scores
            editor.putString("playerNameFirst", playerNameFirst);
            editor.putString("scoreFirst", scoreFirst);
            editor.putString("playerNameSecond", playerNameSecond);
            editor.putString("scoreSecond", scoreSecond);
            editor.putString("playerNameThird", playerNameThird);
            editor.putString("scoreThird", scoreThird);

            editor.apply();

        }
    }
}