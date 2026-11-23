package com.example.listanddetails.domain.repository

import com.example.listanddetails.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun saveProfile(profile: Profile)
}
