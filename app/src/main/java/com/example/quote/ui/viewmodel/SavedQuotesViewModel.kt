package com.example.quote.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quote.data.entity.QuoteRoomEntity
import com.example.quote.room.QuotesRoomDao
import com.example.quote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedQuotesViewModel @Inject constructor(val quotesRoomDao: QuotesRoomDao) : ViewModel() {
    private val _quotes = MutableLiveData<Resource<List<QuoteRoomEntity>>>()

    val quotes: LiveData<Resource<List<QuoteRoomEntity>>> get() = _quotes

    fun getAll() {
        _quotes.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val items = quotesRoomDao.getAll()
            if(items.isNotEmpty()){
                _quotes.postValue(Resource.Success(items))
            }else{
                _quotes.postValue(Resource.Error(Exception("No elements found")))
            }
        }
    }

    fun remove(quote: QuoteRoomEntity){
        viewModelScope.launch {
            quotesRoomDao.delete(quote)
            getAll()
        }
    }

}