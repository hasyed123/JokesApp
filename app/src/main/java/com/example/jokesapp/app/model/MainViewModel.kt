package com.example.jokesapp.app.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokesapp.network.ApiResponse
import com.example.jokesapp.network.JokesApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val jokesApi: JokesApi): ViewModel() {
    private val _joke = MutableLiveData<Joke>()
    val joke: LiveData<Joke> = _joke

    private val _programming = MutableLiveData<Category>()
    val programming: LiveData<Category> = _programming

    private val _dark = MutableLiveData<Category>()
    val dark: LiveData<Category> = _dark

    private val _pun = MutableLiveData<Category>()
    val pun: LiveData<Category> = _pun

    private val _spooky = MutableLiveData<Category>()
    val spooky: LiveData<Category> = _spooky

    private val _christmas = MutableLiveData<Category>()
    val christmas: LiveData<Category> = _christmas

    private val _misc = MutableLiveData<Category>()
    val misc: LiveData<Category> = _misc


    private suspend fun getJokes(): ApiResponse<Joke> {
        return try {
            val joke = jokesApi.getJoke(
                category = getCategoriesString()
            )
            ApiResponse.Success(data = joke)
        } catch (exception: Exception) {
            ApiResponse.Error(message = "An error occurred ${exception.message.toString()}")
        }
    }

    fun tellMeAJoke() {
        viewModelScope.launch {
            when(val response = getJokes()){
                is ApiResponse.Success -> {
                    response.data?.let {
                        _joke.value = it
                    }
                }
                else -> {}
            }
        }
    }

    fun init() {
        _programming.value = Category("Programming", true)
        _spooky.value = Category("Spooky", true)
        _dark.value = Category("Dark", true)
        _christmas.value = Category("Christmas", true)
        _pun.value = Category("Pun", true)
        _misc.value = Category("Misc", true)
        tellMeAJoke()
    }

    private fun getCategoriesString(): String {
        var string: StringBuilder = java.lang.StringBuilder("")
        string.append(parseCategoryName(programming.value))
        string.append(parseCategoryName(misc.value))
        string.append(parseCategoryName(spooky.value))
        string.append(parseCategoryName(dark.value))
        string.append(parseCategoryName(pun.value))
        string.append(parseCategoryName(christmas.value))


        return if(string.isNotEmpty()) {
            string.deleteCharAt(string.lastIndex)
            string.toString()
        } else "Any"
    }

    private fun parseCategoryName(category: Category?): java.lang.StringBuilder {
        var string: StringBuilder = java.lang.StringBuilder("")
        category?.let {
            if(it.selected) string.append("${it.name},")
        }
        return string
    }

    fun updateCategory(name: String) {
        when(name) {
            christmas.value?.name -> christmas.value?.let {
                _christmas.value = Category("Christmas", it.selected.not())
            }
            dark.value?.name -> dark.value?.let {
                _dark.value = Category("Dark", it.selected.not())
            }
            misc.value?.name -> misc.value?.let {
                _misc.value = Category("Misc", it.selected.not())
            }
            programming.value?.name -> programming.value?.let {
                _programming.value = Category("Programming", it.selected.not())
            }
            pun.value?.name -> pun.value?.let {
                _pun.value = Category("Pun", it.selected.not())
            }
            spooky.value?.name -> spooky.value?.let {
                _spooky.value = Category("Spooky", it.selected.not())
            }
        }
    }


}