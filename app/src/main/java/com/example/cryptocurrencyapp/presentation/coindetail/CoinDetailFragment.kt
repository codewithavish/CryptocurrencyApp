package com.example.cryptocurrencyapp.presentation.coindetail


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.FragmentCoinDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinDetailFragment : Fragment() {


    private val viewModel: CoinDetailViewModel by viewModels()
    lateinit var coinDetailBinding: FragmentCoinDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        coinDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_detail, container, false)
        lifecycleScope.launch {
            initialiseData()
        }
        return coinDetailBinding.root
    }

    private suspend fun initialiseData() {
        viewModel.state.collect { state ->
            setUiData(state)

            if (state.error.isNotBlank()) {
                Log.e("error", state.error)
            }
            if (state.isLoading) {
                Log.e("loading", "Loading...")
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUiData(coinData: CoinDetailState) {
        coinDetailBinding.tvId.text = coinData.coins?.rank.toString()+"."
        coinDetailBinding.tvName.text = coinData.coins?.name +" "+"(${coinData.coins?.symbol})"
        coinDetailBinding.tvDesc.text = coinData.coins?.description
        if( coinData.coins?.isActive == true){
            coinDetailBinding.tvStatus.text = "active"
        }else{
            coinDetailBinding.tvStatus.text = "inactive"
        }

    }


}