package nl.evillage.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.evillage.R
import nl.worth.clangnotifications.Clang
import nl.worth.clangnotifications.data.model.ClangNotification

internal class NotificationClickedActivity : AppCompatActivity(),
    NotificationAdapter.ActionClickListener {

    lateinit var clang: Clang
    private var notificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_detailed_message
        )

        val title = findViewById<TextView>(R.id.title)
        val description = findViewById<TextView>(R.id.description)
        val actionRecyclerView = findViewById<RecyclerView>(R.id.rv)

        val clangNotification: ClangNotification =
            intent.getSerializableExtra("clangNotification") as ClangNotification

        title.text = clangNotification.title
        description.text = clangNotification.message
        notificationId = clangNotification.id

        if(clangNotification.actions.isEmpty()){
            val doneButton = findViewById<Button>(R.id.btn_done)
            doneButton.visibility = View.VISIBLE
            doneButton.setOnClickListener {finish()}
        }else {
            actionRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@NotificationClickedActivity)
                adapter =
                    NotificationAdapter(clangNotification.actions, this@NotificationClickedActivity)
            }
        }

        clang = Clang.getInstance()
    }

    override fun onActionClicked(action: ClangNotification.ClangAction) {
        notificationId?.let {
            clang.logNotificationAction(action.id, it, {
                Toast.makeText(this, "Logged action with id: ${action.id}", Toast.LENGTH_SHORT)
                    .show()
            }, { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
        }
        finish()
    }
}
