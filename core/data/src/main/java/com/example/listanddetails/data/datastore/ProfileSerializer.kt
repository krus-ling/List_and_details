package com.example.listanddetails.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.listanddetails.data.model.ProfileDto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object ProfileSerializer : Serializer<ProfileDto> {
    override val defaultValue: ProfileDto = ProfileDto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProfileDto {
        return try {
            ProfileDto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ProfileDto, output: OutputStream) {
        t.writeTo(output)
    }
}
