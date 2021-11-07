package com.example.learningarchitecture.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.learningarchitecture.domain.ShopItem
//второй способ ListAdapter
//этот класс показывает как сравнивать обьекты
class ShopItemDiffCallback():DiffUtil.ItemCallback<ShopItem>(){
    //DiffUtil.ItemCallback<Тип обьекта для сравнения>
    //сюда прилетают уже готовые обьекты, остается только сравнить их, так же как в первом способе
    //без созданий обьекта
    //сравниваем ID обьектов
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }
        //сравниваем поля обьекта, чтобы выяснить нужно ли перерисовывать обьект
    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem //так как ShopItem - data class
        // у него уже переопределен метод equals
    }
}