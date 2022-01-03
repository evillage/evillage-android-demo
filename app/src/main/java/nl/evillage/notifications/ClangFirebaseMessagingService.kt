package nl.evillage.notifications

import android.R.attr
import android.R.attr.*
import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nl.evillage.R
import nl.evillage.ui.notification.NotificationClickedActivity
import nl.evillage.views.Functions
import nl.worth.clangnotifications.Clang
import nl.worth.clangnotifications.data.model.ClangNotification
import kotlin.random.Random
import nl.evillage.App
import kotlinx.android.synthetic.main.fragment_main.*
import androidx.fragment.app.Fragment
import android.app.ActivityManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult

import android.os.Bundle

import android.content.ComponentName
import android.app.Activity
import nl.evillage.ui.MainFragment
import android.content.Intent.getIntent
import android.content.Intent.getIntent
import android.os.Build
import android.service.notification.StatusBarNotification
import java.io.File
import android.content.SharedPreferences
import android.app.AlarmManager


import org.json.JSONObject

import androidx.annotation.RequiresApi

import org.json.JSONArray
import android.app.NotificationManager

import android.media.RingtoneManager
import android.net.Uri
import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.app.NotificationChannel
import nl.evillage.ui.MainActivity































open class ClangFirebaseMessagingService : FirebaseMessagingService() {



    @RequiresApi(Build.VERSION_CODES.M)
    override fun handleIntent(intent: Intent) {

        if ((applicationContext as App).currentFragment != null) {

        } else {

            handleIntentInSharedPreferences(intent)

        }

        super.handleIntent(intent)

    }

    private fun handleIntentInSharedPreferences(intent : Intent)
    {
        if (intent.extras != null) {
        intent.extras?.let {
            //make sure you rename the gcm.notification.tag the notificationId inside the data
            intent?.putExtra("gcm.notification.tag", it["notificationId"].toString())
            val payload = it["cd_payload"].toString()
            val expiry = it["expiry"].toString()

            //create the preferences and name it the same as the notificationId
            val editor = getSharedPreferences(it["notificationId"].toString(), MODE_PRIVATE).edit()
            editor.putString("cd_payload", payload)
            editor.putString("expiry", expiry)
            editor.apply()
            }
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if ((applicationContext as App).currentFragment != null) {

            val mFragment: MainFragment = (applicationContext as App).currentFragment


                if (remoteMessage.data.keys.contains("cd_payload")) {
                    val mFragment: MainFragment = (applicationContext as App).currentFragment
                    mFragment.callTicket(remoteMessage.data["cd_payload"].toString())
                }


            if (ClangNotification.isClangNotification(remoteMessage)) {
                handleClangNotification(ClangNotification(remoteMessage))
            } else {
                super.onMessageReceived(remoteMessage)
            }

        } else {


        }


    }



    private fun handleClangNotification(clangNotification: ClangNotification): Boolean {
        val systemNotificationId = Random.nextInt(0, Int.MAX_VALUE)
        val productTitle = clangNotification.title
        val productContent = clangNotification.message

        Log.d("tag", "Message Notification Body:" + clangNotification.title)

        val intent = Intent(this, MainActivity::class.java)

        intent.apply {
            putExtra("clangNotification", clangNotification)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                this,
                systemNotificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel-01"
        val channelName = "Channel Name"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_error)
            .setContentTitle(productTitle)
            .setContentText(productContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        addActions(clangNotification, builder, systemNotificationId)

        with(NotificationManagerCompat.from(this)) {
            notify(systemNotificationId, builder.build())
        }

        return true
    }

    private fun addActions(
        clangNotification: ClangNotification,
        notificationBuilder: NotificationCompat.Builder,
        systemNotificationId: Int
    ) {
        clangNotification.actions.forEach {
            val pendingIntent = PendingIntent.getService(
                this, Random.nextInt(0, Int.MAX_VALUE),
                Intent(this, ClangIntentService::class.java).apply {
                    this.putExtra("notificationId", clangNotification.id)
                    this.putExtra("actionId", it.id)
                    this.putExtra("systemNotificationId", systemNotificationId)
                }, PendingIntent.FLAG_UPDATE_CURRENT
            )
            Log.d("tag", "Message Notification Body:" + clangNotification.title)

            notificationBuilder.addAction(
                android.R.drawable.ic_notification_overlay,
                it.title,
                pendingIntent
            )
        }
    }


    override fun onNewToken(token: String) {
        Clang.getInstance().updateToken(token,
            {
                Log.d("TAG", "Refreshed token: $token")
            },
            {
                Log.d("TAG", "Failed to update token")
            }
        )

    }
}
