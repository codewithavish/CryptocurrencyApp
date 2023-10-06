package com.example.cryptocurrencyapp.presentation.coinlist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.domain.usecase.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCases: GetCoinsUseCase,
): ViewModel() {



    private val _state = MutableStateFlow<CoinListState>(CoinListState(isLoading = true))
    val state: StateFlow<CoinListState> = _state

    init {
        getCoins()
    }


    private fun getCoins() {
        viewModelScope.launch {
            getCoinsUseCases().collect { result ->
                when(result){
                    is Resource.Success ->{
                        _state.value = CoinListState(coins = result.data ?: emptyList())
                    }
                    is Resource.Error ->{
                        _state.value = CoinListState(
                            error = result.message?: "An unexpected error occurred"
                        )
                    }
                    is Resource.Loading ->{
                        _state.value = CoinListState(isLoading = true)
                    }
                }
            }
        }
    }


}