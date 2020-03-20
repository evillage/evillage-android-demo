package nl.evillage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import nl.evillage.clangnotifications.Clang
import nl.evillage.clangnotifications.R
import nl.evillage.clangnotifications.data.model.KeyValue

internal class NotificationClickedActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_clang)

        val title = findViewById<TextView>(R.id.title)
        val description = findViewById<TextView>(R.id.description)
        val action1Btn = findViewById<Button>(R.id.action1)
        val action2Btn = findViewById<Button>(R.id.action2)
        val action3Btn = findViewById<Button>(R.id.action3)

        val keyValue: ArrayList<KeyValue> = intent.getParcelableArrayListExtra("keyValue")

        title.text = keyValue.find { it.key == "notificationTitle" }?.value
        description.text = keyValue.find { it.key == "notificationBody" }?.value
        action1Btn.text = keyValue.find { it.key == "action1Title" }?.value
        action2Btn.text = keyValue.find { it.key == "action2Title" }?.value
        action3Btn.text = keyValue.find { it.key == "action3Title" }?.value

        val notificationId = keyValue.find { it.key == "notificationId" }?.value
        notificationId?.let {
            action1Btn.apply {
                setOnClickListener {
                    onActionClick(keyValue, notificationId, "action1Id")
                }
            }
            action2Btn.apply {
                setOnClickListener {
                    onActionClick(keyValue, notificationId, "action2Id")
                }
            }
            action3Btn.apply {
                setOnClickListener {
                    onActionClick(keyValue, notificationId, "action3Id")
                }
            }
        }

        clang = Clang.getInstance(
            "https://94fd32f3.ngrok.io",
            applicationContext,
            "46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5",
            "63f4bf70-2a0d-4eb2-b35a-531da0a61b20"
        )
    }

    private fun onActionClick(
        keyValue: ArrayList<KeyValue>,
        notificationId: String,
        actionKey: String
    ) {
        keyValue.find { it.key == actionKey }?.value?.let {
            sendAction(it, notificationId)
        }
        finish()
    }

    private fun sendAction(actionId: String, notificationId: String) {
        clang.logNotificationAction(actionId, notificationId,
            {

            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
    }
}
