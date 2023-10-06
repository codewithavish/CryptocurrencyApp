package com.example.cryptocurrencyapp.presentation.coinlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.FragmentCoinListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CoinListFragment : Fragment(),ItemClickListener {


    lateinit var fragmentCoinListBinding: FragmentCoinListBinding
    private val viewModel: CoinListViewModel by viewModels()
    lateinit var  navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCoinListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coin_list, container, false)
        lifecycleScope.launch {
            initialiseData()
        }
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        return fragmentCoinListBinding.root
    }

    private suspend fun initialiseData() {
        viewModel.state.collect { state ->
            val coinListAdapter = CoinListAdapter(state,this)
            fragmentCoinListBinding.rvCoinList.adapter = coinListAdapter
            fragmentCoinListBinding.rvCoinList.layoutManager = LinearLayoutManager(context)
            fragmentCoinListBinding.rvCoinList.setHasFixedSize(true)
            if (state.error.isNotBlank()) {
                Log.e("error", state.error)
            }
            if (state.isLoading) {
                Log.e("loading", "Loading...")
            }
        }
    }

    override fun onItemClick(id: String?) {
        val bundle = Bundle()
        bundle.putString("coinId", id)
        navController.navigate(R.id.action_coinListFragment_to_coinDetailFragment,bundle)
    }


}
