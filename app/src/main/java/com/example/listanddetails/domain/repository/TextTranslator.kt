package com.example.listanddetails.domain.repository

interface TextTranslator {
    suspend fun translate(text: String): Result<String>
    fun close() {}
}
