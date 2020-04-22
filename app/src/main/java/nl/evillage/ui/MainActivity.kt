package nl.evillage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import nl.evillage.R
import nl.worth.clangnotifications.Clang

class MainActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clang = Clang.getInstance()

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("FCM_TOKEN", "getInstanceId failed", it.exception)
            }

            // Get new Instance ID token
            val token = it.result?.token ?: "FCM not defined"

            // Log and toast
            Log.d("FCM_TOKEN", token)
        }
    }
}
