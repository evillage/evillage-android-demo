package nl.evillage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.messaging.FirebaseMessaging

import nl.evillage.R
import nl.evillage.notifications.ClangFirebaseMessagingService
import nl.worth.clangnotifications.Clang
import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import nl.evillage.App
import nl.evillage.views.Functions


class MainActivity : AppCompatActivity() {

    var mMyApp: App? = null
    lateinit var clang: Clang
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        clang = Clang.getInstance()
         mMyApp = this.applicationContext as App
         mMyApp?.currentActivity = this


         FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if(result != null){
                var fbToken = result
                //clang.updateToken(fbToken, null, null)
                //val token = it.result?.token ?: "FCM not defined"
                Log.d("FCM_TOKEN", fbToken)
                // Log.w("FCM_TOKEN", "getInstanceId failed" + fbToken)
                // DO your thing with your firebase token
            }
        }





        /*
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("FCM_TOKEN", "getInstanceId failed", it.exception)
            }

            // Get new Instance ID token
            val token = it.result?.token ?: "FCM not defined"

            // Log and toast
            Log.d("FCM_TOKEN", token)
        }

         */

    }



}
