package com.daniel.llewellyn

data class Session(val questionAnswer : MutableMap<String, String> = mutableMapOf(), var currentPhase : Int = 0)