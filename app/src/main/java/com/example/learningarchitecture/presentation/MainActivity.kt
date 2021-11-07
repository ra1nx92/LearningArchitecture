package com.example.learningarchitecture.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.learningarchitecture.databinding.ActivityMainBinding

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
            shopListAdapter.submitList(it) //реализация с ListAdapter
            //при вызове метода submitList запускается новый поток
            // в котором проходят все вычисления, после которых список RV обновляется
        }
        binding.btnAddShopItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
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
        setupLongClick()
        setupClick()
        setupSwipeListener(rvShopList)
    }

    private fun setupClick() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("rewq", it.toString())
            val intent = ShopItemActivity.newIntentEditItem(this,it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClick() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnabledItemState(it)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                //currentList - текущий список с которым работает адаптер (ShopList)
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}