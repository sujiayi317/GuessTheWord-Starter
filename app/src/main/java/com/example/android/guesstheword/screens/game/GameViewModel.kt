package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**MutableLiveData vs. LiveData:
 * --- Data in a MutableLiveData object can be changed, as the name implies.
 * Inside the ViewModel, the data should be editable, so it uses MutableLiveData.
 * --- Data in a LiveData object can be read, but not changed. From outside the ViewModel,
 * data should be readable, but not editable, so the data should be exposed as LiveData.
 */
class GameViewModel : ViewModel() {

    private val timer: CountDownTimer

    companion object {
        // Time when the game is over
        private const val DONE = 0L
        // Countdown time interval
        private const val ONE_SECOND = 1000L
        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L
    }

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }


    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    // A backing property allows you to return something from a getter other than the exact object:
    val score: LiveData<Int>
        get() = _score

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        _word.value = ""
        _score.value = 0
        resetList()
        nextWord()

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }

        timer.start()

        Log.i("GameViewModel", "GameViewModel created!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf("queen", "hospital", "basketball", "cat", "change",
                "snail", "soup", "calendar", "sad", "desk", "guitar", "home",
                "railway", "zebra", "jelly", "car", "crow", "trade", "bag", "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isNotEmpty()) {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        } else {
//            onGameFinish()
            // Shuffle the word list, if the list is empty
            resetList()
        }
//        updateWordText()
//        updateScoreText()
    }

    /** Methods for buttons presses **/
    fun onSkip() {
//        score--
        _score.value = (_score.value)?.minus(1)  // performs the subtraction with null-safety
        nextWord()
    }

    fun onCorrect() {
//        score++
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    /** Method for the game completed event **/
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() { // This is called before the ViewModel is destroyed.
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        // Cancel the timer to avoid memory leaks
        timer.cancel()
    }
}