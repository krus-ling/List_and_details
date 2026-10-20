package com.example.listanddetails.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class NoConnectivityException : IOException("No internet connection")
class AirplaneModeException : IOException("Airplane mode is enabled")

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val online = isOnline(context)

        // Если сети нет, то говорим OkHttp брать данные только из кэша
        if (!online) {
            val cacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(7, TimeUnit.DAYS)
                .build()
            request = request.newBuilder()
                .cacheControl(cacheControl)
                .build()
        }

        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            if (!online) {
                if (isAirplaneModeOn(context)) {
                    throw AirplaneModeException()
                } else {
                    throw NoConnectivityException()
                }
            }
            throw e
        }

        // Если сети нет, и в кэше пусто, OkHttp возвращает 504 Gateway Timeout
        if (!online && response.code == 504) {
            response.close()
            if (isAirplaneModeOn(context)) {
                throw AirplaneModeException()
            } else {
                throw NoConnectivityException()
            }
        }

        return response
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0
        } catch (_: Exception) {
            false
        }
    }
}
