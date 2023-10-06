package com.example.cryptocurrencyapp.presentation.coinlist


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.LayoutCoinListBinding
import com.example.cryptocurrencyapp.domain.model.Coin

class CoinListAdapter(private val value: CoinListState?, private val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<CoinListAdapter.CoinListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder {
        val binding: LayoutCoinListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.layout_coin_list, parent, false
        )
        return CoinListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        holder.bind(value?.coins?.get(position))
    }

    override fun getItemCount(): Int {
        return value?.coins?.size?:0
    }

    inner class CoinListViewHolder(var binding: LayoutCoinListBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(coins: Coin?) {
            binding.tvId.text = coins?.rank.toString()
            binding.tvName.text = coins?.name
            if( coins?.is_active == true){
                binding.tvStatus.text = "active"
            }else{
                binding.tvStatus.text = "inactive"
            }
            binding.clItem.setOnClickListener {
                itemClickListener.onItemClick(coins?.id)
            }
        }

    }

}