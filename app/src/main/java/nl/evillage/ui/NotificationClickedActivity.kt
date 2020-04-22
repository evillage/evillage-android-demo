package nl.evillage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import nl.evillage.R
import nl.worth.clangnotifications.Clang
import nl.worth.clangnotifications.data.model.ClangNotification

internal class NotificationClickedActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_detailed_message
        )

        val title = findViewById<TextView>(R.id.title)
        val description = findViewById<TextView>(R.id.description)
        val action1Btn = findViewById<Button>(R.id.action1)
        val action2Btn = findViewById<Button>(R.id.action2)
        val action3Btn = findViewById<Button>(R.id.action3)

        val clangNotification: ClangNotification = intent.getSerializableExtra("clangNotification") as ClangNotification

        title.text = clangNotification.title
        description.text = clangNotification.message
        action1Btn.text = clangNotification.actions[0].title
        action2Btn.text = clangNotification.actions[1].title
        action3Btn.text = clangNotification.actions[2].title

        val notificationId = clangNotification.id
        notificationId?.let {
            action1Btn.apply {
                setOnClickListener {
                    onActionClick(clangNotification.actions[0].id, notificationId)
                }
            }
            action2Btn.apply {
                setOnClickListener {
                    onActionClick(clangNotification.actions[1].id, notificationId)
                }
            }
            action3Btn.apply {
                setOnClickListener {
                    onActionClick(clangNotification.actions[2].id, notificationId)
                }
            }
        }

        clang = Clang.getInstance()
    }

    private fun onActionClick(
        id: String,
        notificationId: String
    ) {
        sendAction(id, notificationId)
        finish()
    }

    private fun sendAction(actionId: String, notificationId: String) {
        clang.logNotificationAction(actionId, notificationId, {
            Toast.makeText(this, "Logged action with id: $actionId", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        })
    }
}