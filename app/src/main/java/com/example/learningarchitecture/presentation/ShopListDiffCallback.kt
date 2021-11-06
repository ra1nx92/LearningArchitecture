package com.example.learningarchitecture.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.learningarchitecture.domain.ShopItem
//вместо notifyDataSetChange
//первый способ DiffUtils, минус в том что работает в главном потоке
// второй способ лучше ListAdapter (ShopItemDiffCallback)
class ShopListDiffCallback(
    //передаем старый лист, и новый, который будет установлен в RecyclerView
    private val oldList:List<ShopItem>,
    private val newList:List<ShopItem>
): DiffUtil.Callback() {
        //размер старого списка
    override fun getOldListSize(): Int {
        return oldList.size
    }
        //размер нового списка
    override fun getNewListSize(): Int {
        return newList.size
    }
        //сравниваем ID, если одинаковый, то это один и тот же обьект
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }
        //сравниваем поля обьекта
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem //так как ShopItem - data class
    // у него переопределен метод equals
    }

}

        //реализация этого способа в адаптере, устанавливается в сетер переменной содержащий
        //показываемый список
        //var shopList = listOf<ShopItem>()
//        set(value) {
//            field = value
//          }
//-----------------------------------------------------------------------------------
//когда мы устанавливаем новое значение в список, создаем обьект класса DiffCallback
// создаем обьект, передаем старый список, и новый(value)
//val callback = ShopListDiffCallback(shopList,value)
//val diffResult = DiffUtil.calculateDiff(callback)//метод сравнивает
// списки, элементы, поля и т.д
//после вычислений, diffResult сообщает адаптеру какие методы необходимо вызвать
//diffResult.dispatchUpdatesTo(this)
