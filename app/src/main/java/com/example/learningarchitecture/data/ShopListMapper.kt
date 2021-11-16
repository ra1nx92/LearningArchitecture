package com.example.learningarchitecture.data

import com.example.learningarchitecture.domain.ShopItem

//класс содержит методы для преобразования обьектов domain слоя в модель БД, это необходимо для
//сохранения принципа чистой архитектуры, domain слой ничего не должен знать о data слое,
//data слой зависит от domain
class ShopListMapper() {
    //метод преобразует сущность domain слоя в data слой
    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    }

    //обратное преобразование
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            enabled = shopItemDbModel.enabled
        )
    }

    //преобразует лист обьектов ShopItemDbModel в лист обьектов ShopItem
    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

}