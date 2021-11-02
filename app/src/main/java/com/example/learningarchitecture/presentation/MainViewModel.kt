package com.example.learningarchitecture.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learningarchitecture.data.ShopListRepositoryImpl
import com.example.learningarchitecture.domain.DeleteShopItemUseCase
import com.example.learningarchitecture.domain.EditShopItemUseCase
import com.example.learningarchitecture.domain.GetShopListUseCase
import com.example.learningarchitecture.domain.ShopItem
//presentation слой отвечает за отображение данных и взаимодействие с пользователем
//все элементы бизнес логики вызываются из use case, с которыми взаимодействует ViewModel
//в чистой архитектуре Presentation все знает о Domain, но ничего не должен знать о слое Data

class MainViewModel:ViewModel() {

    // делать так нельзя, для этого используется иньекция зависимостей, до которой я еще увы не допер
    //presentation слой ничего не должен знать о data слое, и наоборот
    val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopListUseCase = DeleteShopItemUseCase(repository)
    private val editShopListUseCase = EditShopItemUseCase(repository)

    // нельзя создать обьект LiveData т.к он абстрактный, но у него есть наследник MutableLiveData
    // он такой же как и LiveData но в который можно вставлять обьекты,
    // и подписчики его сразу получат
    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem:ShopItem){
         deleteShopListUseCase.deleteShopItem(shopItem)
    }

    fun changeEnabledItemState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopListUseCase.editShopItem(newItem)
    }
}