package com.example.learningarchitecture.presentation

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
    val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name,count)
        if(fieldValid) {
            val shopItem = ShopItem(name,count,true)
            addShopItemUseCase.addShopItem(shopItem)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            val shopItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(shopItem)
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
        //проверяем правильность ввода
    private fun validateInput(name:String, count:Int):Boolean{
        return name.isNotEmpty() && count > 0
    }

}