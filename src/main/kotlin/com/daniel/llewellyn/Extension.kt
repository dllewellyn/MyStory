package com.daniel.llewellyn

import com.dan.llewellyn.base.Card

fun card(block : Card.() -> Unit) : Card {
    val card = Card()
    card.block()
    return card
}