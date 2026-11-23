package com.example.listanddetails.domain.usecase

import com.example.listanddetails.domain.model.Profile
import com.example.listanddetails.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Profile> = profileRepository.getProfile()
}
