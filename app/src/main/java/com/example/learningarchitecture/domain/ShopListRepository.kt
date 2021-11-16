package com.example.learningarchitecture.domain

import androidx.lifecycle.LiveData

//интерфейс репозитория
//Use case - какая то одна операция бизнес логики которую может вызвать пользователь
//Use case зависят от интерфейса репозитория, а не от конкретной реализации

interface ShopListRepository {
    suspend fun addShopItem(shopItem: ShopItem)
    suspend fun deleteShopItem(shopItem: ShopItem)
    suspend fun editShopItem(shopItem: ShopItem)
    suspend fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
}