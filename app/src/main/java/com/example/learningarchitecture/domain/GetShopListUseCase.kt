package com.example.learningarchitecture.domain

import androidx.lifecycle.LiveData

//для каждого метода бизнес логики создается отдельник класс
// первый принцип SOLID - принцип единой ответственнсти
//Use case - какая то одна операция бизнес логики которую может вызвать пользователь
class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}