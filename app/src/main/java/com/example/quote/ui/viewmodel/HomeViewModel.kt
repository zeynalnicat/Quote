package com.example.quote.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quote.data.entity.QuotesRespondItem
import com.example.quote.retrofit.QuoteDao
import com.example.quote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(val quoteApi: QuoteDao) : ViewModel() {

    private var _quotes = MutableLiveData<Resource<QuotesRespondItem>>()

    val quotes: LiveData<Resource<QuotesRespondItem>> get() = _quotes

    fun getQuotes() {
        _quotes.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = quoteApi.getQuote()
            if (response.isSuccessful) {
                val q = response.body()
                q?.let {
                    _quotes.postValue(Resource.Success(q[0]))
                }

            }else{
                _quotes.postValue(Resource.Error(Exception("Something unexpected happened")))
            }
        }
    }

}