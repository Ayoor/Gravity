package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

val key: String = "mysecretkey12345" // You can use any tech.ayodele.gravity.getKey
val secretKeySpec: Key = SecretKeySpec(key.toByteArray(), "AES")

@RequiresApi(Build.VERSION_CODES.O)
fun encryptString(input: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun decryptString(input: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input))
    return String(decryptedBytes, Charsets.UTF_8)
}

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val originalText = "basket"
    val encryptedText = encryptString(originalText)
    println("Encrypted: $encryptedText")

    val decryptedText = decryptString(encryptedText)
    println("Decrypted: $decryptedText")
}
