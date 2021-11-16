package com.example.learningarchitecture.presentation.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningarchitecture.data.ShopListRepositoryImpl
import com.example.learningarchitecture.domain.DeleteShopItemUseCase
import com.example.learningarchitecture.domain.EditShopItemUseCase
import com.example.learningarchitecture.domain.GetShopListUseCase
import com.example.learningarchitecture.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

//presentation слой отвечает за отображение данных и взаимодействие с пользователем
//все элементы бизнес логики вызываются из use case, с которыми взаимодействует ViewModel
//в чистой архитектуре Presentation все знает о Domain, но ничего не должен знать о слое Data

class MainViewModel(application: Application):AndroidViewModel(application) {

    // делать так нельзя, для этого используется иньекция зависимостей, до которой я еще увы не допер
    //presentation слой ничего не должен знать о data слое, и наоборот
    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopListUseCase = DeleteShopItemUseCase(repository)
    private val editShopListUseCase = EditShopItemUseCase(repository)

    //корутина может вызываться только в контексте корутины (coroutine scope)
    //необходим скоуп у которого ЖЦ будет совпадать с ЖЦ ViewModel, в методе OnClear он будет
    // останавливать свою работу
    //в CoroutineScope необходимо передать корутин-контекст. Он описывает на каком потоке будет
    // выполняться корутина, как реагировать на ошибки, и т.д
    //Dispatchers позволяет выбрать на каком потоке будет идти работа корутины, Main, IO ,Default
    //IO - input\output
    //private val scope = CoroutineScope(Dispatchers.Main)


    // нельзя создать обьект LiveData т.к он абстрактный, но у него есть наследник MutableLiveData
    // он такой же как и LiveData но в который можно вставлять обьекты,
    // и подписчики его сразу получат
    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem:ShopItem){
        //в VM есть свой скоуп который не надо закрывать, он работает сам
        viewModelScope.launch {
            deleteShopListUseCase.deleteShopItem(shopItem)
        }

    }
    //метод для смены макета элемента при долгом нажатии
    fun changeEnabledItemState(shopItem: ShopItem){
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopListUseCase.editShopItem(newItem)
        }
    }
}