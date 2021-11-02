package com.example.learningarchitecture.data

import com.example.learningarchitecture.domain.ShopItem
import com.example.learningarchitecture.domain.ShopListRepository
import java.lang.RuntimeException
//data слой отвечает за работу с данными, и предоставляет конкретную реализацию репозитория
//data слой зависит от domain слоя, и знает о нем все. Domain слой ничего не знает о data слое
class ShopListRepositoryImpl : ShopListRepository {
    private val shoplist = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {

    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId
            autoIncrementId++
        }
        shoplist.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shoplist.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shoplist.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shoplist.find { it.id == shopItemId }
            ?: throw RuntimeException("Element not found $shopItemId") // на случай если придет null
    }

    override fun getShopList(): List<ShopItem> {
        return shoplist.toList()
    }
}