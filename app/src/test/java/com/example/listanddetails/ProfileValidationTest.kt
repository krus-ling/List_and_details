package com.example.listanddetails

import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.mapper.toDto
import com.example.listanddetails.domain.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileValidationTest {

    private val timeRegex = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

    @Test
    fun validTimeFormats_passRegex() {
        val validTimes = listOf(
            "00:00",
            "08:30",
            "12:00",
            "14:45",
            "20:15",
            "23:59"
        )

        validTimes.forEach { time ->
            assertTrue("Expected $time to be valid", time.matches(timeRegex))
        }
    }

    @Test
    fun invalidTimeFormats_failRegex() {
        val invalidTimes = listOf(
            "",
            "   ",
            "8:30",
            "24:00",
            "12:60",
            "25:10",
            "12:5",
            "ab:cd",
            "14-30",
            "14:30:00",
            "24:59",
            "00:60"
        )

        invalidTimes.forEach { time ->
            assertFalse("Expected $time to be invalid", time.matches(timeRegex))
        }
    }

    @Test
    fun profileMapper_preservesFavoriteLessonTime() {
        val profile = Profile(
            name = "Иван Иванов",
            position = "Android Developer",
            photoUri = "file:///avatar.jpg",
            resumeUrl = "https://example.com/resume.pdf",
            favoriteLessonTime = "16:20"
        )

        val dto = profile.toDto()
        val mappedProfile = dto.toDomain()

        assertEquals(profile.favoriteLessonTime, mappedProfile.favoriteLessonTime)
        assertEquals(profile.name, mappedProfile.name)
        assertEquals(profile.position, mappedProfile.position)
        assertEquals(profile.photoUri, mappedProfile.photoUri)
        assertEquals(profile.resumeUrl, mappedProfile.resumeUrl)
    }
}
