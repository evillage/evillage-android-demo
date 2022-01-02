package nl.evillage.ui

import android.app.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.messaging.FirebaseMessaging

import nl.evillage.R
import nl.evillage.notifications.ClangFirebaseMessagingService
import nl.worth.clangnotifications.Clang
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import nl.evillage.App
import nl.evillage.views.Functions
import android.app.PendingIntent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import java.io.File
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var mMyApp: App? = null
    lateinit var clang: Clang
     @RequiresApi(Build.VERSION_CODES.M)
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        clang = Clang.getInstance()
         mMyApp = this.applicationContext as App
         mMyApp?.currentActivity = this

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                     as NotificationManager
             val channelId = getString(R.string.default_fcm_channel_id)
             if(manager.getNotificationChannel(channelId)==null) {
                 val channel = NotificationChannel(channelId,
                     getString(R.string.notification_channel_name),
                     NotificationManager.IMPORTANCE_DEFAULT)
                 channel.description =
                     getString(R.string.notification_channel_description)
                 manager.createNotificationChannel(channel)
             }
         }



             if (intent.extras != null) {
                 intent.extras?.let {

                     val payload = it["cd_payload"].toString()
                     if (payload != null) {
                         val mainConstraintLayout = this.layoutInflater.inflate(
                             nl.evillage.R.layout.fragment_main,
                             null
                         ) as ConstraintLayout
                         Functions.buildTheTickets(
                             parent = this,
                             toAdd = payload,
                             mainConstraintLayout
                         )

                     }
                 }
             }

             val notificationManager =
                 this?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

             val activeNotifications: Array<StatusBarNotification> = notificationManager.activeNotifications
             val notificationList: List<Notification> = ArrayList(activeNotifications.count())
             for (notification in activeNotifications) {


                 //get the preferences file via the the notification.tag
                 val prefs = getSharedPreferences(notification.tag.toString(), MODE_PRIVATE)
                 val payload = prefs.getString(
                     "cd_payload",
                     "No name defined"
                 ) //"No name defined" is the default value.

                 val expiry = prefs.getString(
                     "expiry",
                     "No name defined"
                 )
                 if (payload != null) {
                     val mainConstraintLayout = this.layoutInflater.inflate(
                         nl.evillage.R.layout.fragment_main,
                         null
                     ) as ConstraintLayout
                     Functions.buildTheTickets(
                         parent = this,
                         toAdd = payload.toString(),
                         mainConstraintLayout
                     )

                 }

                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                     deleteSharedPreferences(notification.tag.toString())
                 }

             }

         //clean some left preferences
         cleanSharedPreferences()


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


    }


    private fun cleanSharedPreferences() {

        //clean all preferences that have a payload content that his not equel to the defvalue
        val dir = File(getFilesDir().getParent().toString() + "/shared_prefs/")
        val children: Array<String> = dir.list()
        for (i in children.indices) {

            val prefs = getSharedPreferences(children[i], MODE_PRIVATE)
            val payload = prefs.getString(
                "cd_payload",
                "No name defined"
            )
            if (payload != "No name defined") {
                getSharedPreferences(children[i].replace(".xml", ""), MODE_PRIVATE).edit().clear()
                    .commit()
                //delete the file
                File(dir, children[i]).delete()
            }
        }
    }


}
