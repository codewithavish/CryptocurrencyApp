package com.example.cryptocurrencyapp.presentation.coindetail


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.common.Constants
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.domain.usecase.GetCoinDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
        private val getCoinDetailUseCase: GetCoinDetailUseCase,
        private val savedStateHandle: SavedStateHandle
    ): ViewModel() {

    private val _state = MutableStateFlow<CoinDetailState>(CoinDetailState(isLoading = true))
    val state: StateFlow<CoinDetailState> = _state

        init {
           savedStateHandle.get<String>(Constants.COIN_ID)?.let { coinId ->
               getCoin(coinId)
           }
        }


        private fun getCoin(coinId: String) {
            viewModelScope.launch {
                getCoinDetailUseCase(coinId).collect{ result ->
                    when(result){
                        is Resource.Success ->{
                            _state.value = CoinDetailState(coins = result.data)
                        }
                        is Resource.Error ->{
                            _state.value = CoinDetailState(
                                error = result.message?: "An unexpected error occurred"
                            )
                        }
                        is Resource.Loading ->{
                            _state.value = CoinDetailState(isLoading = true)
                        }
                    }
                }
            }
        }


}