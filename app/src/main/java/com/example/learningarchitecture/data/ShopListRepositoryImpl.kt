package com.example.learningarchitecture.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.learningarchitecture.domain.ShopItem
import com.example.learningarchitecture.domain.ShopListRepository


//data слой отвечает за работу с данными, и предоставляет конкретную реализацию репозитория
//data слой зависит от domain слоя, и знает о нем все. Domain слой ничего не знает о data слое
class ShopListRepositoryImpl(application: Application) : ShopListRepository {


    //создаем экземпляр интерфейса Dao
    private val shopListDao = AppDataBase.getInstance(application).shopListDao()

    //создаем экземпляр мапера
    private val mapper = ShopListMapper()


    override fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }


    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }


    //в бд обработан конфликт, благодаря этому можно использовать метод для добавления
// как метод редактирования
    override fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }


    //необходимо смапить обьект бд в обьект domain слоя
    override fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    //метод возвращает обьект LD в которой лежит коллекция из слоя data - List<ShopItemDbModel>
//чтобы сохранить чистоту архитектуры, коллекция должна быть преобразована в коллекцию обьектов
// из domain слоя List<ShopItem>
// чтобы перехватить и обработать обновления в LD используется MediatorLiveData
    override fun getShopList(): LiveData<List<ShopItem>> =
        MediatorLiveData<List<ShopItem>>().apply {
//добавляем в него источник данных(addSource) и добавляем LD значения которой нужно
// перехватить (shopListDao.getShopList())
            addSource(shopListDao.getShopList()) {
//После перехвата значения его можно обработать и установить значение вручную
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
}