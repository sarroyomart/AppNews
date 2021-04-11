package com.example.appnews.Cypher

import com.example.appnews.GlobalClass
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CypherPol {
    companion object{
        fun generateKey():ByteArray{
            val key = GlobalClass.email
            var binary= key.toByteArray(charset("UTF-8"))
            var sha:MessageDigest = MessageDigest.getInstance("SHA-1")
            binary = sha.digest(binary)
            binary = Arrays.copyOf(binary, 16)
            return binary
        }
        fun encrypt(key: ByteArray, value: String):String{
            require(key.size == 16) { "Invalid key size." }
            val skeySpec = SecretKeySpec(key, "AES")
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(16)))
            val original: ByteArray = value.toByteArray(Charset.forName("UTF-8"))
            val binary: ByteArray = cipher.doFinal(original)
            return Base64.getEncoder().encodeToString(binary)
        }

        fun decrypt(key: ByteArray, encrypted: String): String{
            require(key.size == 16) { "Invalid key size." }
            val skeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(16)))
            val binary: ByteArray = Base64.getDecoder().decode(encrypted)
            val original = cipher.doFinal(binary)
            return String(original, Charset.forName("UTF-8"))
        }
    }


}