package com.example.learningarchitecture.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.learningarchitecture.databinding.ActivityMainBinding
import com.example.learningarchitecture.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
            Log.d("MAIN_ACTIVITY", it.toString())
        }
    }

    private fun setupRecyclerView() {
        val rvShopList = binding.rvShopItem
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter
        //увеличиваем количество переиспользуемых ячеек для RV
        //первый итем
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE_RV
        )
        //второй итем
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.VIEW_TYPE_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE_RV
        )
        //метод для смены макета элемента при долгом нажатии
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnabledItemState(it)
        }
    }
}