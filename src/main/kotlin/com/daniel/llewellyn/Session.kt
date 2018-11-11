package com.daniel.llewellyn

data class Session(val questionAnswer : MutableMap<String, String>, var currentPhase : Int = 0)