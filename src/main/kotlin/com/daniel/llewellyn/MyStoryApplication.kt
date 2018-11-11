package com.daniel.llewellyn

import com.dan.llewellyn.base.AppLinkCard
import com.dan.llewellyn.base.AppProperties
import com.dan.llewellyn.base.Response
import com.dan.llewellyn.base.response
import com.dan.llewellyn.interfaces.AbstractApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyStoryApplication : AbstractApplication() {

    lateinit var sessionHolder : S3Session
    lateinit var session : Session

    val story = listOf(
            StoryPhase("You're sitting at home on a cold, winter day. You hear the wind blowing outside and move closer to the log fire burning away in the corner. You hear a gentle knock at the door and walk over to see who it is. On opening the door you see someone you recognise. What is their name?", "What is their name?"))

    override fun getProperties(): AppProperties = AppProperties(isAccountLinkingRequired = false)

    fun sessionStore(slots : Map<String, String>) {

        GlobalScope.launch {
            session.questionAnswer.putAll(slots)
            sessionHolder.updateSession(session)
        }

    }

    override fun listOfActions(): Map<String, (Map<String, String>) -> Response> {
        return mapOf("story_phase" to { slots ->
            this.sessionStore(slots)
            this.handleStoryPhase(slots)
        })
    }

    private fun handleStoryPhase(slots : Map<String, String>): Response =
        response {
            speech = story[session.currentPhase].story
            reprompt = story[session.currentPhase].reprompt
            card = card {
                title = "My Story"
                text = story[session.currentPhase].story

            }

        }


    override fun setSessionId(sessionId: String) {
        super.setSessionId(sessionId)
        sessionHolder = S3Session(sessionId)
        session = sessionHolder.retrieveSession()
    }

    override fun respondToEnd(): Response =
        response {
            speech = "Thanks for my listening to my story. I hope you've enjoyed the app"
            endSession = true
        }


    override fun respondToHelp(): Response =
            response {
                speech = "Welcome to my story. To start your story, call 'Start my story'"
                endSession = false
            }

    override fun respondToStart(): Response =
        response {
            speech = "Welcome to my story. To start your story, call 'Start my story'"
            endSession = false
        }

    override fun respondWithLinkCard(): AppLinkCard? = null
}