package com.example.listanddetails.data.repository

import androidx.datastore.core.DataStore
import com.example.listanddetails.data.mapper.toDomain
import com.example.listanddetails.data.mapper.toDto
import com.example.listanddetails.data.model.ProfileDto
import com.example.listanddetails.domain.model.Profile
import com.example.listanddetails.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val dataStore: DataStore<ProfileDto>
) : ProfileRepository {

    override fun getProfile(): Flow<Profile> {
        return dataStore.data.map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun saveProfile(profile: Profile) {
        dataStore.updateData {
            profile.toDto()
        }
    }
}
