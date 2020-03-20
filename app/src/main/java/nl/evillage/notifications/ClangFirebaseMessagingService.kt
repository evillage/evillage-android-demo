package nl.evillage.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nl.evillage.NotificationClickedActivity
import nl.evillage.clangnotifications.Clang
import nl.evillage.clangnotifications.R
import nl.evillage.clangnotifications.data.model.KeyValue
import kotlin.random.Random

open class ClangFirebaseMessagingService : FirebaseMessagingService() {
    lateinit var clang: Clang

    override fun onMessageReceived(p0: RemoteMessage?) {
        p0?.data?.let { data ->
            when (data["type"]) {
                "clang" -> {
                    if (handleClangNotification(data)) return
                }
                else -> super.onMessageReceived(p0)
            }
        }
        super.onMessageReceived(p0)
    }

    private fun handleClangNotification(data: Map<String, String>): Boolean {
        val systemNotificationId = Random.nextInt(0, Int.MAX_VALUE)
        val productTitle = data["notificationTitle"]
        val productContent = data["notificationBody"]

        val intent = Intent(this, NotificationClickedActivity::class.java)

        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val list = arrayListOf<KeyValue>().apply {
                data.keys.forEach { add(
                    KeyValue(
                        it,
                        data[it] ?: ""
                    )
                ) }
            }
            putExtra("keyValue", list)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, systemNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(productTitle)
            .setContentText(productContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        addActions(data, builder, systemNotificationId)

        with(NotificationManagerCompat.from(this)) {
            notify(systemNotificationId, builder.build())
        }

        return true
    }

    private fun addActions(
        data: Map<String, String>,
        notificationBuilder: NotificationCompat.Builder,
        systemNotificationId: Int
    ) {
        val notId = data["notificationId"]
        for (i in 1..3) {
            val actionId = data["action${i}Id"]
            val actionTitle = data["action${i}Title"]

            actionId?.let {
                actionTitle.let {
                    val pendingIntent =
                        PendingIntent.getService(this, Random.nextInt(0, Int.MAX_VALUE) + i,
                            Intent(this, ClangIntentService::class.java).apply {
                                this.putExtra("notificationId", notId)
                                this.putExtra("actionId", actionId)
                                this.putExtra("systemNotificationId", systemNotificationId)
                            }, PendingIntent.FLAG_UPDATE_CURRENT
                        )

                    notificationBuilder.addAction(
                        android.R.drawable.ic_notification_overlay,
                        actionTitle,
                        pendingIntent
                    )
                }
            }
        }
    }

    override fun onNewToken(token: String?) {
        token?.let { fbToken ->
            clang = Clang.getInstance(
                "https://94fd32f3.ngrok.io",
                applicationContext,
                "46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5",
                "63f4bf70-2a0d-4eb2-b35a-531da0a61b20"
            )

            clang.updateToken(fbToken,
                {
                    Log.d("TAG", "Refreshed token: $fbToken")
                },
                {
                    Log.d("TAG", "failed to update token")
                }
            )
        }
    }
}