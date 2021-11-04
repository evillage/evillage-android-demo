package nl.evillage.notifications

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
import nl.evillage.ui.MainActivity
import android.app.Activity
import nl.evillage.ui.MainFragment


open class ClangFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.keys.contains("cd_payload")) {
            val mFragment: MainFragment = (applicationContext as App).currentFragment
            mFragment.callTicket(remoteMessage.data["cd_payload"].toString())
        }

        if (ClangNotification.isClangNotification(remoteMessage)) {
            handleClangNotification(ClangNotification(remoteMessage))
        } else {
            super.onMessageReceived(remoteMessage)
        }
    }

    private fun handleClangNotification(clangNotification: ClangNotification): Boolean {
        val systemNotificationId = Random.nextInt(0, Int.MAX_VALUE)
        val productTitle = clangNotification.title
        val productContent = clangNotification.message

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