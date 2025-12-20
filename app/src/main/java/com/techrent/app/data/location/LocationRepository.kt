package com.techrent.app.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepository(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Result<Location> =
        suspendCancellableCoroutine { cont ->
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val provider = when {
                lm.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                else -> {
                    cont.resume(Result.failure(Exception("Ubicación desactivada (GPS/Red)")))
                    return@suspendCancellableCoroutine
                }
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    lm.removeUpdates(this)
                    if (cont.isActive) cont.resume(Result.success(location))
                }

                @Deprecated("Deprecated in API 29")
                override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) = Unit

                override fun onProviderDisabled(provider: String) {
                    lm.removeUpdates(this)
                    if (cont.isActive) cont.resume(Result.failure(Exception("Provider deshabilitado")))
                }
            }

            cont.invokeOnCancellation { lm.removeUpdates(listener) }

            try {
                // Pide un update y corta al primero recibido.
                lm.requestLocationUpdates(
                    provider,
                    0L,
                    0f,
                    listener,
                    Looper.getMainLooper()
                )

                // Fallback: última conocida (si existe) para mejorar UX en emulador.
                val last = lm.getLastKnownLocation(provider)
                if (last != null && cont.isActive) {
                    lm.removeUpdates(listener)
                    cont.resume(Result.success(last))
                }
            } catch (e: Exception) {
                lm.removeUpdates(listener)
                cont.resume(Result.failure(e))
            }
        }
}
