package com.daniel.llewellyn

import java.lang.NullPointerException

data class StoryPhase(val storyToSlots : Map<String, String>,
                      val reprompt : String? = null,
                      val answerSlot : String? = null,
                      val slotOptions : List<String> = listOf(),
                      val conditionalStorySlot : String)


fun Map<String, String?>.toUpper() : Map<String, String> {
    val mut = mutableMapOf<String, String>()
    this.forEach {
        it.value?.let {
            v ->
                mut.put(it.key.toUpperCase(), v)
        }
    }

    return mut
}

fun StoryPhase.storyPhaseFromSlots(slots: Map<String, String?>): String {
    var initialStory = ""

    if (storyToSlots.size == 1) {
        initialStory = storyToSlots.values.first()
    } else {
        val slotValue = slots.toUpper()[conditionalStorySlot.toUpperCase()]?.toUpperCase()
        initialStory = storyToSlots.toUpper()[slotValue] ?: throw NullPointerException()
    }


    slots.forEach {
        if (it.value != null && initialStory.contains(it.key)) {
            initialStory = initialStory.replace("%${it.key}%", it.value!!)
        }
    }

    return initialStory
}