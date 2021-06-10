package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain get() = _eventPlayAgain

    init {
        _score.value = finalScore
    }

    fun onPlayAgain() {
        Log.i("Score", "Update call")
        _eventPlayAgain.value = true
        Log.i("Score", "Updated")
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}