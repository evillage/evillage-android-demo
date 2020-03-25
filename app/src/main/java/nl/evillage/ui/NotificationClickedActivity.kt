package nl.evillage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import nl.worth.clangnotifications.Clang
import nl.worth.clangnotifications.R
import nl.worth.clangnotifications.data.model.ClangKeyValue

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

        val clangKeyValue: ArrayList<ClangKeyValue> = intent.getParcelableArrayListExtra("keyValue")

        title.text = clangKeyValue.find { it.key == "notificationTitle" }?.value
        description.text = clangKeyValue.find { it.key == "notificationBody" }?.value
        action1Btn.text = clangKeyValue.find { it.key == "action1Title" }?.value
        action2Btn.text = clangKeyValue.find { it.key == "action2Title" }?.value
        action3Btn.text = clangKeyValue.find { it.key == "action3Title" }?.value

        val notificationId = clangKeyValue.find { it.key == "notificationId" }?.value
        notificationId?.let {
            action1Btn.apply {
                setOnClickListener {
                    onActionClick(clangKeyValue, notificationId, "action1Id")
                }
            }
            action2Btn.apply {
                setOnClickListener {
                    onActionClick(clangKeyValue, notificationId, "action2Id")
                }
            }
            action3Btn.apply {
                setOnClickListener {
                    onActionClick(clangKeyValue, notificationId, "action3Id")
                }
            }
        }

        clang = Clang.getInstance(applicationContext,"46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5", "63f4bf70-2a0d-4eb2-b35a-531da0a61b20")
    }

    private fun onActionClick(
        clangKeyValue: ArrayList<ClangKeyValue>,
        notificationId: String,
        actionKey: String
    ) {
        clangKeyValue.find { it.key == actionKey }?.value?.let {
            sendAction(it, notificationId)
        }
        finish()
    }

    private fun sendAction(actionId: String, notificationId: String) {
        clang.logNotificationAction(actionId, notificationId, {

        }, {
            Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
        })
    }
}