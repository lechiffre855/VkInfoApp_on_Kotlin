package utils

import android.net.Uri
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import java.util.Scanner

class NetworkUtils {

    companion object {

        private const val VK_API_BASE_URL = "https://api.vk.com/"
        private const val VK_USERS_GET = "method/users.get"
        private const val PARAM_ACCESS_TOKEN = "access_token"
        private const val PARAM_USER_ID = "user_ids"
        private const val PARAM_VERSION = "v"

        private val TOKEN = "4bc163d04bc163d04bc163d0d848d430c444bc14bc163d02f1a4e990db48b1fdfb9b2e5"

        fun generateURL(userId: String):URL {
            var builtURI: Uri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_ACCESS_TOKEN, TOKEN)
                .appendQueryParameter(PARAM_USER_ID, userId)
                .appendQueryParameter(PARAM_VERSION, "5.131")
                .build()
            var url: URL? = null
            url = URL(builtURI.toString())
            return url
        }

        fun getResponseFromURL (url: URL?): String? {
            var urlConnection: HttpURLConnection = url?.openConnection() as HttpURLConnection
            try {
                var inputStream: InputStream = urlConnection.inputStream

                var scanner: Scanner = Scanner(inputStream)
                scanner.useDelimiter("\\A")

                if (scanner.hasNext())
                    return scanner.next()
                else
                    return null
            }
            catch (e: UnknownHostException) {
                return null
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}