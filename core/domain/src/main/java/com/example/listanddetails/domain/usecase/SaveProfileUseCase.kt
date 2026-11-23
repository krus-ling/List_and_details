package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.Profile
import com.example.listanddetails.domain.repository.ProfileRepository

class SaveProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) {
        profileRepository.saveProfile(profile)
    }
}
