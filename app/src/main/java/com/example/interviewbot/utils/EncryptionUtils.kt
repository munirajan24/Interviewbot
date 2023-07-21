package com.example.interviewbot.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {

    private const val ENCRYPTION_ALGORITHM = "AES"
    private const val SECRET_KEY = "F5Qe/Q61gMFLKAEoPo8qxJWo4r5eYF1G7o0RVcoUb7M=" // Replace with your secret key
    private const val SECRET_KEY_LENGTH = 256 // 256-bit key

    fun generateSecretKey(): String {
        val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM)
        keyGenerator.init(SECRET_KEY_LENGTH, SecureRandom())
        val secretKey = keyGenerator.generateKey()
        return Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
    }

    fun encrypt(apiKey: String): String {
        val encryptedBytes = try {
            val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), ENCRYPTION_ALGORITHM)
            val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(apiKey.toByteArray())
        } catch (e: Exception) {
            return "";
        }
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedApiKey: String): String {
        val decryptedBytes = try {
            val secretKey = SecretKeySpec(SECRET_KEY.toByteArray(), ENCRYPTION_ALGORITHM)
            val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val encryptedBytes = Base64.decode(encryptedApiKey, Base64.DEFAULT)
            cipher.doFinal(encryptedBytes)
        } catch (e: Exception) {
            return "";
        }
        return String(decryptedBytes)
    }
}
