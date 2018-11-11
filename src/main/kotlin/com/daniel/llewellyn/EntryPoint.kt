package com.daniel.llewellyn

import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.dan.llewellyn.ApplicationAdapter

class Factory {

    companion object {
        fun getApp() : ApplicationAdapter {
            return ApplicationAdapter(MyStoryApplication())
        }
    }
}

class EntryPoint : SkillStreamHandler(getSkill()) {

    companion object {
        fun getSkill() =
                Skills.standard()
                        .addRequestHandlers(Factory.getApp())
                        .withSkillId(System.getenv("ALEXA_ID"))
                        .build()
    }
}