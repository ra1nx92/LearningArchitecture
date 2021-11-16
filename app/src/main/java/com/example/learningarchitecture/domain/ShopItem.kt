package com.example.learningarchitecture.domain
//слой domain - слой в котором содержится вся бизнес логика приложения
//слой domain не зависит от других слоев
//для каждого метода бизнес логики создается отдельник класс
// первый принцип SOLID - принцип единой ответственнсти(S), класс отвечает только за что то одно
//Use case - какая то одна операция бизнес логики которую может вызвать пользователь


data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object{
        const val UNDEFINED_ID = 0
    }
}