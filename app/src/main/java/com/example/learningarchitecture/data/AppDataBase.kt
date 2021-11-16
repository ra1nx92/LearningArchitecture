package com.example.learningarchitecture.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//аннотацией Database помечаем основной класс по работе с базой данных
//этот класс должен быть абстрактным и наследовать RoomDatabase

//в параметрах аннотации Database указываем какие Entity будут использоваться и версию бд
//для каждого Entity класса из списка entities будет создана таблица
@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    //реализация интерфейса Dao
    abstract fun shopListDao(): ShopListDao

    //база данных должна быть синглтоном, чтобы быть точно увереным что работа идет
    // с одной и той же базой
    companion object {
        private var INSTANCE: AppDataBase? = null //переменная экземпляра БД
        private var LOCK = Any() //обьект синхронизации. Синглтоны должны быть синхронизированы
        private const val NAME_DB = "shop_item.db"


        //чтобы получить экземпляр базы данных нужен контекст, передается апликейшн,
        // чтобы не утекал контекст активити

        fun getInstance(application: Application): AppDataBase {
            INSTANCE?.let {
                return it
            }
//синхронищация нужна для того чтобы если два потока одновременно вызвали этот метод,
// в обоих потоках произойдет проверка, и какой то из них раньше зайдет в метод synchronized.
//если ее не сделать то можно случайно присвоить БД новое значение. Способ называется даблчек
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                //создаем экземпляр БД
                val db = Room.databaseBuilder(
                    application,//контекст
                    AppDataBase::class.java, //класс БД
                    NAME_DB //имя БД
                ).build()
                INSTANCE = db //присваиваем полученное значение
                return db
            }
        }
    }
}
