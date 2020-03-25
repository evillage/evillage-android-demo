package nl.evillage.notifications

import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import nl.worth.clangnotifications.Clang

internal class ClangIntentService : IntentService("ClangIntentService") {
    lateinit var clang : Clang

    override fun onHandleIntent(intent: Intent?) {
        clang = Clang.getInstance(applicationContext,"46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5", "63f4bf70-2a0d-4eb2-b35a-531da0a61b20")

        val actionId = intent?.getStringExtra("actionId")
        val notificationId = intent?.getStringExtra("notificationId")

        if (notificationId != null && actionId != null) {
            clang.logNotificationAction(actionId, notificationId,
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
