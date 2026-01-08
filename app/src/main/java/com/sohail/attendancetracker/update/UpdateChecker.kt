package com.sohail.attendancetracker.update

import android.util.Log
import com.sohail.attendancetracker.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Metadata about an available update pulled from the remote JSON file.
 */
data class UpdateInfo(
    val versionCode: Int,
    val versionName: String?,
    val downloadUrl: String,
    val isForceUpdate: Boolean,
    val message: String?
)

sealed class UpdateResult {
    data object Idle : UpdateResult()
    data object NoUpdate : UpdateResult()
    data class UpdateAvailable(val info: UpdateInfo) : UpdateResult()
    data class Error(val throwable: Throwable) : UpdateResult()
}

/**
 * Simple HTTP-based update checker.
 */
object UpdateChecker {
    private const val TAG = "UpdateChecker"

    suspend fun checkForUpdates(
        url: String = BuildConfig.UPDATE_INFO_URL
    ): UpdateResult = withContext(Dispatchers.IO) {
        if (url.isBlank()) {
            return@withContext UpdateResult.NoUpdate
        }

        var connection: HttpURLConnection? = null
        try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 7_000
                readTimeout = 7_000
            }

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                return@withContext UpdateResult.Error(IOException("Unexpected HTTP $responseCode"))
            }

            val raw = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(raw)

            val remoteVersion = json.optInt("versionCode", -1)
            val downloadUrl = json.optString("downloadUrl")
            if (remoteVersion <= BuildConfig.VERSION_CODE || remoteVersion <= 0 || downloadUrl.isBlank()) {
                return@withContext UpdateResult.NoUpdate
            }

            val info = UpdateInfo(
                versionCode = remoteVersion,
                versionName = json.optString("versionName").takeIf { it.isNotBlank() },
                downloadUrl = downloadUrl,
                isForceUpdate = json.optBoolean("forceUpdate", false),
                message = json.optString("message").takeIf { it.isNotBlank() }
            )
            UpdateResult.UpdateAvailable(info)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to check updates", e)
            UpdateResult.Error(e)
        } finally {
            connection?.disconnect()
        }
    }
}
