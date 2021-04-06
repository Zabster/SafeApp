package ru.zabster.safepassapp.utils.secure

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zabster.safepassapp.BuildConfig
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Utils for cryptographic
 */
object SecurityUtils {

    private const val TAG = "SecurityUtils"

    private const val PBK_ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_SPECK_ALGORITHM = "AES"
    private const val KEY_LENGTH = 256
    private const val ITERATION_COUNT = 65536

    private val gson = Gson()

    /**
     * Encrypt key
     * Used from MainThread is not recommended
     *
     * @param text text for encrypt
     *
     * @return [SecureKeyData] data for decrypt
     */
    fun getNewSecret(text: String): SecureKeyData? {
        val salt = ByteArray(8).apply {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                SecureRandom.getInstanceStrong().nextBytes(this)
            else SecureRandom().nextBytes(this)
        }
        return runCatching {
            val secretKey = generateSecretKey(salt)
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encode = cipher.doFinal(text.encodeToByteArray())

            SecureKeyData(
                key = Base64.encodeToString(encode, Base64.DEFAULT),
                iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT),
                salt = Base64.encodeToString(salt, Base64.DEFAULT)
            )
        }.onFailure { tr ->
            Log.w(TAG, "Error with generation to the new secret signature", tr)
        }.getOrNull()
    }

    /**
     * Decrypt key
     * Used from MainThread is not recommended
     *
     * @param data secure data for decrypt
     * @param dispatcher thread for decrypt
     *
     * @return [String] decrypt text
     */
    suspend fun decryptSecret(data: SecureKeyData, dispatcher: CoroutineDispatcher): String {
        return withContext(dispatcher) {
            val salt = Base64.decode(data.salt, Base64.DEFAULT)
            val iv = Base64.decode(data.iv, Base64.DEFAULT)
            val key = Base64.decode(data.key, Base64.DEFAULT)

            runCatching {
                val decodeSecretKey = generateSecretKey(salt)
                val decodeCipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                decodeCipher.init(Cipher.DECRYPT_MODE, decodeSecretKey, IvParameterSpec(iv))

                String(decodeCipher.doFinal(key))
            }.onFailure { tr ->
                Log.w(TAG, "Error with decrypt secret signature", tr)
            }.getOrDefault("")
        }
    }

    /**
     * Generate json from [SecureKeyData]
     *
     * @param secureKeyData data for generate json string
     *
     * @return json or empty string
     */
    fun secureKeyDataToGson(secureKeyData: SecureKeyData): String =
        runCatching { gson.toJson(secureKeyData) }
            .onFailure { tr -> Log.w(TAG, "Error with generation json for $secureKeyData", tr) }
            .getOrDefault("")

    /**
     * Parse [SecureKeyData] from json
     *
     * @param json string with [SecureKeyData] signature
     *
     * @return [SecureKeyData] or null
     */
    fun secureKeyDataFromGson(json: String): SecureKeyData? =
        runCatching { gson.fromJson(json, SecureKeyData::class.java) }
            .onFailure { tr -> Log.w(TAG, "Error with json parsing from str: $json", tr) }
            .getOrNull()

    private fun generateSecretKey(salt: ByteArray): SecretKeySpec {
        val secretKeyFactory = SecretKeyFactory.getInstance(PBK_ALGORITHM)
        val spec = PBEKeySpec(BuildConfig.APP_SECRET.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        return SecretKeySpec(secretKeyFactory.generateSecret(spec).encoded, KEY_SPECK_ALGORITHM)
    }
}