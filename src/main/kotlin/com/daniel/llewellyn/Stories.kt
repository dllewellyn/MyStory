package com.dan.llewellyn.mystory

import com.daniel.llewellyn.StoryPhase


object Story {
    fun firstPhase()
            = StoryPhase(
                mapOf("" to "It's a Sunday, and you don't have any plans for the day, except to relax. You're sitting at home in a comfy chair, looking out of the window. What season is it?"),
                "What's the season?",
                "season",
                listOf("Autumn", "Winter", "Summer", "Spring"),
            ""
            )

    fun secondPhase() = StoryPhase(
            mapOf(
                    "Autumn" to "The wind blows hard and you see brown leaves from the nearby tree stripped from the branch and scattered into the distance. You hear a gentle knock at the door and walk over to see who it is. On opening the door you see someone you recognise. What is their name?",
                    "Winter" to "You move closer to the log fire burning away in the corner whilst you watch as a strong gust of wind blows a nearby tree, already stripped of its leaves, you can see a bird taking flight. You hear a gentle knock at the door and walk over to see who it is. On opening the door you see someone you recognise. What is their name?",
                    "Summer" to "You watch a gentle breeze cause the full, green leaves of a nearby tree tree to rustle slightly. You hear a gentle knock at the door and walk over to see who it is. On opening the door you see someone you recognise. What is their name?",
                    "Spring" to "You watch a gentle breeze blowing a nearby tree, with its new leaves struggling to hold onto the sturdy branches of a tree that has been there for many years. You hear a gentle knock at the door and walk over to see who it is. On opening the door you see someone you recognise. What is their name?"

            ),
            "What is their name?",
            "character_a_name",
            conditionalStorySlot = "season")
    fun thirdPhase() = StoryPhase(

            mapOf("" to "You and %character_a_name% talk for a while, before deciding that you're going to go for a stroll. You pick up your coat before setting off. Where are you and %character_a_name% going?"),
            "Where are you going?", "destination_a",
            conditionalStorySlot = "")

    fun fourthPhase() = StoryPhase(mapOf("" to "You and %character_a_name% begin walking towards %destination_a%. You stop suddenly, and take in the %season%'s smells, when the wind suddenly picks up! You look up and see something large coming toward you. What is it?"),
            "What is coming towards you?",
            answerSlot = "object_coming_towards_you",
            conditionalStorySlot = "")

    fun fifthPhase() = StoryPhase (
            mapOf("" to "With the %object_coming_towards_you% flying towards you, you need to react quickly. You grab %character_a_name% and pull them to the ground as the %object_coming_towards_you% flies overhead, missing you by inches. You look into %character_a_name%'s eyes and see the utter terror at the near miss... You get up to your feet, brushing debris off. You help %character_a_name% to their feet, and notice they look startled. What do you say to try and calm %character_a_name% down?"),
            reprompt = "How do you calm down %character_a_name%?",
            answerSlot = "calming_phrase",
            conditionalStorySlot = ""
    )

    fun sixthPhase() = StoryPhase (
            mapOf("" to "It seems to work %character_a_name% calms down, and you see a smile spread across their face. The wind seems to have died down now, and you see no reason to ruin a lovely day - you suggest continuing the walk. Does %character_a_name% agree to come with you?"),
            reprompt = "Does %character_a_name% want to continue the walk?",
            answerSlot = "continue_walk",
            slotOptions = listOf("Yes", "No"),
            conditionalStorySlot = ""
    )

    fun seventhPhase() = StoryPhase (
            mapOf("Yes" to "You and %character_a_name% carry on your walk to %destination_a% together, chatting as you go. In years gone by, you're bound to wonder if you should have followed %character_a_name% and simply stayed at home... This story is to be continued",
                    "No" to "You say goodbye to %character_a_name% and carry on your walk to %destination_a% alone. In years gone by, you're bound to wonder if you should have followed %character_a_name% and simply stayed at home... This story is to be continued"),
            reprompt = "",
            answerSlot = "",
            conditionalStorySlot = "continue_walk"


    )

    val STORIES = listOf(
            firstPhase(),
            secondPhase(),
            thirdPhase(),
            fourthPhase(),
            fifthPhase(),
            sixthPhase(),
            seventhPhase()
    )
}