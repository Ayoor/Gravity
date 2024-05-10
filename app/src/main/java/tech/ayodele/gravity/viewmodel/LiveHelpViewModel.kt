package tech.ayodele.gravity.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import papaya.`in`.sendmail.SendMail
import tech.ayodele.gravity.model.UserDetails

class LiveHelpViewModel : ViewModel() {
    private val _mailSentStatus = MutableLiveData<Boolean>()
    val mailSentStatus: LiveData<Boolean> = _mailSentStatus

    fun sendMail(context: Context, userDetails: UserDetails, message: String, subject: String) {
        val mail = SendMail(
            "gbengajohn4god@gmail.com", "eeoacjjcsbbzfajt",
            "gbengajohn4god@gmail.com", // Actual sender email should be used here
            "Gravity Live Help Assistance",
            "Sender: ${userDetails.email}\n" +
                    "Subject: $subject\n" +
                    "Name: ${userDetails.name}\n" +
                    "Message: $message"
        )
        val confirmationMail = SendMail(
            "gbengajohn4god@gmail.com", "eeoacjjcsbbzfajt",
            userDetails.email, // Actual recipient email should be used here
            "Confirmation!",
            "Hello ${userDetails.name},\n\n" +
                    "This is to confirm that we've received your email and will respond to you as soon as possible.\n\n" +
                    "We hope you have a good day and wish you the best in your weight loss journey."
        )
        try {
            mail.execute()
            confirmationMail.execute()
            _mailSentStatus.postValue(true)
        } catch (e: Exception) {
            _mailSentStatus.postValue(false)
        }
    }
}
