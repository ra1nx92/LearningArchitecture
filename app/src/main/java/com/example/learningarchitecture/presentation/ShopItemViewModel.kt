package com.example.learningarchitecture.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learningarchitecture.data.ShopListRepositoryImpl
import com.example.learningarchitecture.domain.AddShopItemUseCase
import com.example.learningarchitecture.domain.EditShopItemUseCase
import com.example.learningarchitecture.domain.GetShopItemUseCase
import com.example.learningarchitecture.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {
    // делать так нельзя, для этого используется иньекция зависимостей, до которой я еще увы не допер
    //presentation слой ничего не должен знать о data слое, и наоборот
    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    //из активити работаем с errorInput, а из вью модели с _errorInput
    //переопределяем геттер, который будет возвращать значение переменной _errorInput
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
    get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem:LiveData<ShopItem>
    get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen:LiveData<Unit>
    get() = _shouldCloseScreen


    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            _shouldCloseScreen.value = Unit
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name,count = count)
                editShopItemUseCase.editShopItem(item)
                _shouldCloseScreen.value = Unit
            }



        }
    }

    //проверяем ввод поля name
    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    //проверяем ввод поля count
    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    //проверяем правильность ввода в полях
    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isEmpty()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    //функция для сброса ошибки ввода, если пользователь начал писать заново
    fun resetErrorInputName() {
        _errorInputName.value = false
    }
    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

}