package com.daniel.llewellyn

import com.dan.llewellyn.base.*
import com.dan.llewellyn.display.BuilderTemplate2.BuilderTemplate
import com.dan.llewellyn.display.BuilderTemplate2.TemplateType
import com.dan.llewellyn.interfaces.AbstractApplication
import com.dan.llewellyn.mystory.Story

fun List<String>.toUpper() = this.map { it.toUpperCase() }


class MyStoryApplication : AbstractApplication() {

    private lateinit var session: Session
    private val CURRENT_PHASE = "currentPhase"
    val SLOT_VALUE = "slot_value"


    override fun getProperties(): AppProperties = AppProperties(isAccountLinkingRequired = false, handleMissingSlots = true)

    private fun sessionStore(slots: Map<String, String>) {

        val nonNullSlots = mutableMapOf<String, String>()
        nonNullSlots.putAll(slots)
        nonNullSlots.put(CURRENT_PHASE, "${session.currentPhase}")
        this.updateAttributes(nonNullSlots)
    }

    override fun listOfActions(): Map<String, (Map<String, String>) -> Response> {
        return mapOf(
                "story_phase" to {
                    slots ->
                    this.handleStoryPhase(slots)
                },
                "new_story" to {
                    slots ->
                    this.session = Session()
                    this.handleStoryPhase(slots)
                })
    }

    private fun handleStoryPhase(slots: Map<String, String>): Response {

        val currentStoryPhase = Story.STORIES[session.currentPhase]

        val slotValue = slots.get(SLOT_VALUE)

        return if (slotValue != null &&
                currentStoryPhase.slotOptions.isNotEmpty() &&
                !currentStoryPhase.slotOptions.toUpper().contains(slotValue.toUpperCase()))
        {
            var invalidSpeech = "I was expecting one of"
            currentStoryPhase.slotOptions.forEach {
                invalidSpeech = if (invalidSpeech.endsWith("one of")) {
                    "$invalidSpeech $it"
                } else {
                    "$invalidSpeech or $it"
                }
            }

            response (invalidSpeech){
                reprompt = invalidSpeech
                endSession = false

                Story.STORIES[session.currentPhase].answerSlot?.let {
                    directives = listOf(Directive(SLOT_VALUE, mutableMapOf()))
                }
            }

        } else if (slotValue  != null) {
            session.questionAnswer.put(currentStoryPhase.answerSlot!!, slotValue)
            session.currentPhase += 1
            this.sessionStore(session.questionAnswer)

            response (speech = "Got it.. To carry on the story, say 'continue'") {
                reprompt = "Say 'continue' to carry on the story"
                endSession = false
            }

        } else {
            var storyStr = currentStoryPhase.storyPhaseFromSlots(session.questionAnswer)

            if (currentStoryPhase.slotOptions.isNotEmpty()) {
                storyStr = "$storyStr Try one of the following values."
                currentStoryPhase.slotOptions.forEach {
                    storyStr = if (!storyStr.endsWith(" values.")) {
                        "$storyStr or $it"
                    } else {
                        "$storyStr $it"
                    }
                }
            }

            response (speech = storyStr) {
                reprompt = Story.STORIES[session.currentPhase].reprompt
                card = card {
                    title = "My Story"
                    text = Story.STORIES[session.currentPhase].reprompt
                }
                endSession = (session.currentPhase - 1 == Story.STORIES.size)

                Story.STORIES[session.currentPhase].answerSlot?.let {
                    directives = listOf(Directive(SLOT_VALUE, mutableMapOf()))
                }
            }
        }
    }

    override fun updateAttributes(attributes: Map<String, Any>) {
        super.updateAttributes(attributes)
        val attrs = this.getAttributes()

        val strAttributes = mutableMapOf<String, String>()
        attrs.forEach { strAttributes.put(it.key, it.value as String) }

        var sessionCount = 0
        attrs.get(CURRENT_PHASE)?.let {
            sessionCount = Integer.parseInt(it as String)
        }

        session = Session(currentPhase = sessionCount,
                questionAnswer = strAttributes)
    }

    override fun respondToEnd(): Response =
            response (speech = "Thanks for my listening to my story. I hope you've enjoyed the app") {
                endSession = true
            }


    override fun respondToHelp(): Response =
            response (speech = "Welcome to my story. To start your story, say 'Start my story'. Throughout the story, you'll " +
                    "be asked questions that will prompt you to help write your own story. Enjoy!") {
                reprompt = "Try saying 'Start my story'"
                endSession = false
            }

    override fun respondToStart(): Response =
            response(speech = "Welcome to my story. To start your story, say 'Start my story'") {
                endSession = false
            }

    override fun respondWithLinkCard(): AppLinkCard? = null
}