package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun main(){
    val list = mutableListOf<String>()
    println(list.toString())
    list.add("Egg")
    println(list.toString())
}