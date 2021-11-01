package com.example.learningarchitecture.domain
//слой domain - слой в котором содержится вся бизнес логика приложения
//слой domain не зависит от других слоев
//для каждого метода бизнес логики создается отдельник класс
// первый принцип SOLID - принцип единой ответственнсти(S), класс отвечает только за что то одно

data class ShopItem(
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
) {
}