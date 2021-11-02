package com.example.learningarchitecture.domain
//для каждого метода бизнес логики создается отдельник класс
// первый принцип SOLID - принцип единой ответственнсти
//Use case - какая то одна операция бизнес логики которую может вызвать пользователь
class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItem(shopItemId: Int): ShopItem{
       return shopListRepository.getShopItem(shopItemId)
    }
}