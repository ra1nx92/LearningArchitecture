package com.example.learningarchitecture.data

import androidx.room.Entity
import androidx.room.PrimaryKey
//создаем модель базы данных
//аннотацией Entity нам необходимо пометить объект, который мы хотим хранить в базе данных
//для этого создаем класс который будет представлять собой данные
@Entity(tableName = "shop_items")
data class ShopItemDbModel(
//аннотацией PrimaryKey помечается поле, которое будет ключом в таблице
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
    )

