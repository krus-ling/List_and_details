package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.Profile
import com.example.listanddetails.domain.repository.ProfileRepository

class SaveProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) = repository.saveProfile(profile)
}
