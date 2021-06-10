package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.i(TAG, "Called ViewModelProvider()")
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        binding.gameViewModel = gameViewModel
        binding.lifecycleOwner = this

        gameViewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { eventGameFinished ->
            if (eventGameFinished == true) {
                gameFinished()
                gameViewModel.onGameFinishComplete()
            }
        })

        gameViewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime).toString()
        })

        return binding.root

    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val currentScore = gameViewModel.score.value ?: 0
        val action = GameFragmentDirections.actionGameToScore(currentScore)
        findNavController(this).navigate(action)
    }

    companion object {
        const val TAG = "GameFragment"
    }
}
