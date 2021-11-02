package com.example.learningarchitecture.domain
//интерфейс репозитория
//Use case - какая то одна операция бизнес логики которую может вызвать пользователь
//Use case зависят от интерфейса репозитория, а не от конкретной реализации

interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): List<ShopItem>
}