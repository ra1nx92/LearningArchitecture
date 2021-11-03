package com.example.learningarchitecture.presentation

import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learningarchitecture.R
import com.example.learningarchitecture.databinding.ItemRvEnableBinding
import com.example.learningarchitecture.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    //функция клика по элементу
    var onShopItemLongClickListener:((ShopItem) -> Unit)? = null

    //в методе onCreateViewHolder из макета создается вью, сама ячейка будущего списка
    //количество ячеек создается либо на необходимое количество, если все влазят в экран,
    //либо столько сколько помещается в экран, + дополнительные ячейки для переиспользования(+-5)

    //Все скрытые с экрана ячейки отправляются в пул, и переиспользуются из пула если необходимо
    //отобразить новый элемент.

    //Если RV необходимо отобразить новый элемент, а его тип отличается от тех что лежат в пуле,
    //будет создан новый элемент и добавлен в пул.Если пул заполнен, одна из ячеек будет удалена

    //Размер пула можно увеличивать по своему усмотрению, для каждого типа ячейки можно сделать
    // свой пул, но без фанатизма, чтобы не занимать память
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        //выбираем необходимый элемент, благодаря методу getItemViewType
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_rv_enable
            VIEW_TYPE_DISABLED -> R.layout.item_rv_disable
            else -> throw RuntimeException("Не найдет тип view $viewType")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    //в этом методе в макет устанавливаются необходимые данные
    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        with(holder) {
            binding.tvName.text = shopItem.name
            binding.tvCount.text = shopItem.count.toString()
            itemView.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)//invoke - явный вызов,
                                                            //лямбда сработает только если не придет null
                true
            }
        }
    }

    //этот метод возвращает количество элементов в коллекци
    override fun getItemCount(): Int {
        return shopList.size
    }
    //метод нужен чтобы определить какой макет нужено использовать для конкретного элемента
    //это значение придет в метод onCreateViewHolder в качестве парамента viewType
    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else
            VIEW_TYPE_DISABLED
    }

    //класс - хранилище для наших вью, к которым потом привязываются данные
    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRvEnableBinding.bind(view)
    }
        //интерфейс для функции клика по элементу
    interface OnShopItemLongClick{
        fun onShopItemLongClick(shopItem: ShopItem)
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0

        const val MAX_POOL_SIZE_RV = 5//количество "запасных" view для RV можно установить вручную
    }
}