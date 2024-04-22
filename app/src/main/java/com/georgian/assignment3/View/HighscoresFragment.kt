package com.georgian.assignment3.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.georgian.assignment3.R
import com.georgian.assignment3.ViewModel.MemoryGameViewModel

class HighscoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_highscores, container, false)

        // Find TextViews in the layout
        val textFirst = rootView.findViewById<TextView>(R.id.textFirst)
        val textSecond = rootView.findViewById<TextView>(R.id.textSecond)
        val textThird = rootView.findViewById<TextView>(R.id.textThird)

        // Display high scores in TextViews
        displayHighScores(textFirst, textSecond, textThird)

        return rootView
    }

    // Display high scores in TextViews
    private fun displayHighScores(textFirst: TextView, textSecond: TextView, textThird: TextView) {
        // Retrieve high scores from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val playerNameFirst = sharedPreferences.getString("playerNameFirst","None")
        val scoreFirst = sharedPreferences.getString("scoreFirst","None")
        val playerNameSecond = sharedPreferences.getString("playerNameSecond","None")
        val scoreSecond = sharedPreferences.getString("scoreSecond","None")
        val playerNameThird = sharedPreferences.getString("playerNameThird","None")
        val scoreThird = sharedPreferences.getString("scoreThird","None")

        // Display the first place high score if available
        if (playerNameFirst != "None" && scoreFirst != "None") {
            textFirst.text = String.format("1. %s : %s", playerNameFirst, scoreFirst)
        }

        // Display the second place high score if available
        if (playerNameSecond != "None" && scoreSecond != "None") {
            textSecond.text = String.format("2. %s : %s", playerNameSecond, scoreSecond)
        }

        // Display the third place high score if available
        if (playerNameThird != "None" && scoreThird != "None") {
            textThird.text = String.format("3. %s : %s", playerNameThird, scoreThird)
        }
    }
}