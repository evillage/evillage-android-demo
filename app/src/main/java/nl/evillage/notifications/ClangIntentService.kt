package nl.evillage.notifications

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import nl.worth.clangnotifications.Clang

internal class ClangIntentService : IntentService("ClangIntentService") {

    override fun onHandleIntent(intent: Intent?) {

        val actionId = intent?.getStringExtra("actionId")
        val notificationId = intent?.getStringExtra("notificationId")

        if (notificationId != null && actionId != null) {
            Clang.getInstance().logNotificationAction(actionId, notificationId,
                {
                    Toast.makeText(applicationContext, "Thank you for your submit!", Toast.LENGTH_LONG).show()
                },
                {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            )
        }

        val systemNotificationId = intent?.getIntExtra("systemNotificationId", 0) ?: 0
        NotificationManagerCompat.from(this).cancel(systemNotificationId)
    }
}
