package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {
    private val _word = MutableLiveData<String>("")
    val word: LiveData<String> get() = _word

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _eventGameFinish = MutableLiveData(false)
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish

    private lateinit var wordList: MutableList<String>

    var timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTimeString: LiveData<String> = Transformations.map(_currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val _buzzEvent = MutableLiveData<BuzzType>()
    val buzzEvent: LiveData<BuzzType> get() = _buzzEvent

    var panicVibrate = false

    init {
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= THREE_SECOND && !panicVibrate) {
                    _buzzEvent.value = BuzzType.COUNTDOWN_PANIC
                    panicVibrate = true
                }
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _buzzEvent.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }
        }
        timer.start()

        resetList()
        nextWord()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = if ((_score.value)?.minus(1)!! < 0) {
            0
        } else {
            (_score.value)?.minus(1)
        }
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        _buzzEvent.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun onBuzzComplete() {
        _buzzEvent.value = BuzzType.NO_BUZZ
    }

    companion object {
        const val TAG = "GameViewModel"

        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val THREE_SECOND = 3000L
        const val COUNTDOWN_TIME = 10000L //10 Seconds

        enum class BuzzType(val pattern: LongArray) {
            CORRECT(CORRECT_BUZZ_PATTERN),
            GAME_OVER(GAME_OVER_BUZZ_PATTERN),
            COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
            NO_BUZZ(NO_BUZZ_PATTERN)
        }
    }
}