package com.example.quote.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quote.data.entity.QuoteRoomEntity
import com.example.quote.data.entity.QuotesRespondItem
import com.example.quote.retrofit.QuoteDao
import com.example.quote.room.QuotesRoomDao
import com.example.quote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(val quoteApi: QuoteDao, val quotesRoomDao: QuotesRoomDao) :
    ViewModel() {

    private var _quotes = MutableLiveData<Resource<QuotesRespondItem>>()
    private var _checkDb = MutableLiveData(false)

    val quotes: LiveData<Resource<QuotesRespondItem>> get() = _quotes
    val checkDb: LiveData<Boolean> get() = _checkDb


    fun getQuotes() {
        _quotes.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = quoteApi.getQuote()
            if (response.isSuccessful) {
                val q = response.body()
                q?.let {
                    _quotes.postValue(Resource.Success(q[0]))
                }

            } else {
                _quotes.postValue(Resource.Error(Exception("Something unexpected happened")))
            }
        }
    }

    fun insertRoom(content: String, author: String, category: String) {
        checkRoom(content)
        if (_checkDb.value == false) {
            viewModelScope.launch(Dispatchers.IO) {
                val s = quotesRoomDao.insert(QuoteRoomEntity(0, content, author, category))
                if (s != -1L) {
                    _checkDb.postValue(true)
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val s = quotesRoomDao.search(content)
                if (s != null) {
                    quotesRoomDao.delete(QuoteRoomEntity(s, content, author, category))
                    _checkDb.postValue(false)
                }

            }
        }

    }


    fun checkRoom(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val c = quotesRoomDao.search(content)
            if (c != null) {
                _checkDb.postValue(true)
            } else {
                _checkDb.postValue(false)
            }
        }
    }

}