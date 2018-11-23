import com.dan.llewellyn.mystory.Story
import com.daniel.llewellyn.MyStoryApplication
import com.daniel.llewellyn.toUpper
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test

class MyStoryApplicationTest {

    @Test
    fun testTheWholeStory() {
        val story = MyStoryApplication()

        var response = story.listOfActions()["new_story"]?.invoke(mapOf<String, String>())
        Assert.assertEquals("${Story.STORIES[0].storyToSlots.values.first()} Try one of the following values. Autumn or Winter or Summer or Spring", response?.speech)
        Assert.assertEquals(story.SLOT_VALUE , response?.directives?.get(0)?.slotToElicit)
        response = story.listOfActions()["new_story"]?.invoke(mapOf(story.SLOT_VALUE to "InvalidValue"))

        Assert.assertEquals("I was expecting one of Autumn or Winter or Summer or Spring", response?.speech)
        response = story.listOfActions()["new_story"]?.invoke(mapOf(story.SLOT_VALUE to "autumn"))

        Assert.assertEquals("Got it.. To carry on the story, say 'continue'", response?.speech)
        Assert.assertEquals("autumn", story.getAttributes().get(Story.STORIES[0].answerSlot))

        story.updateAttributes(story.getAttributes())

        response = story.listOfActions()["story_phase"]?.invoke(mapOf<String, String>())
        Assert.assertEquals(Story.STORIES[1].storyToSlots.values.first().replace("%${Story.STORIES[0].answerSlot!!}%", "David"), response?.speech)
    }

    @Test
    fun toUpperTest() {
        assertEquals(listOf("ABCDEF", "FRANCIS", "THOMAS"), listOf("AbcDef", "FranCis", "ThOmas").toUpper())
    }
}