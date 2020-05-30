package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**MutableLiveData vs. LiveData:
 * --- Data in a MutableLiveData object can be changed, as the name implies.
 * Inside the ViewModel, the data should be editable, so it uses MutableLiveData.
 * --- Data in a LiveData object can be read, but not changed. From outside the ViewModel,
 * data should be readable, but not editable, so the data should be exposed as LiveData.
 */
class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word
    // The current score
    private val _score = MutableLiveData<Int>()
    // A backing property allows you to return something from a getter other than the exact object:
    val score: LiveData<Int>
        get() = _score
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        _word.value = ""
        _score.value = 0
        resetList()
        nextWord()
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
        }
//        updateWordText()
//        updateScoreText()
    }

    /** Methods for buttons presses **/
    fun onSkip() {
//        score--
        _score.value = (score.value)?.minus(1)  // performs the subtraction with null-safety
        nextWord()
    }

    fun onCorrect() {
//        score++
        _score.value = (score.value)?.plus(1)
        nextWord()
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}