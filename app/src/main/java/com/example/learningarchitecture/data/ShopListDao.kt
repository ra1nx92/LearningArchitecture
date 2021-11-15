package com.example.learningarchitecture.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//в объекте Dao описываются методы для работы с базой данных
//описываем их в интерфейсе с аннотацией Dao
@Dao
interface ShopListDao {
    //чтобы запросить из базы список объектов, необходимо в Dao создать метод с аннотацией Query
//запрашиваем из базы данных обьекты и указываем какого они класса
//При вызове этого метода Room сделает запрос в таблицу, конвертирует полученные данные
//в объекты и упакует их в List
    @Query("SELECT * FROM shop_items")//shop_items - название базы данных в Entity
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    //в качестве возвращаемого типа указываем лист обьектов Entity
    //Room умеет возвращать данные в LiveData обертке
//-------------------------------------------------------------------------------------------------
//описываем метод, который на вход принимает Entity объект. К методу добавляем аннотацию Insert и
//Room сгенерирует необходимый код в реализации этого интерфейса.
    @Insert(onConflict = OnConflictStrategy.REPLACE)//решение конфликтов, если в базу попадет обьект
//с тем же ID который уже есть то он перезапишется последним пришедшим значением,
// а если такого нет, добавится новый
    fun addShopItem(shopItemDbModel: ShopItemDbModel)

    //---------------------------------------------------------------------------------------------
//удаление обьекта из списка БД по его ID, передается имя таблицы указанное в Entity
    // и переменная с Id обьекта
    @Query("DELETE FROM shop_items WHERE id =:shopItemId")
    fun deleteShopItem(shopItemId: Int)

    //---------------------------------------------------------------------------------------------
//получение обьекта из списка БД по его ID, передается имя таблицы указанное в Entity
//и переменная с Id обьекта, и на всякий случай добавляем лимит, чтобы по запросу всегда возвращался
    // только 1 элемент
    @Query("SELECT * FROM shop_items WHERE id=:shopItemId LIMIT 1")
    fun getShopItem(shopItemId: Int): ShopItemDbModel

}