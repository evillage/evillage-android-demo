package nl.evillage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import nl.evillage.clangnotifications.Clang
import nl.evillage.thank_you.ThankYouActivity

class LoginActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val submit = findViewById<Button>(R.id.submit)
        val email = findViewById<EditText>(R.id.email)

        clang = Clang.getInstance(
            "https://94fd32f3.ngrok.io",
            applicationContext,
            "46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5",
            "63f4bf70-2a0d-4eb2-b35a-531da0a61b20"
        )

        submit.setOnClickListener {
            clang.logEvent("login", mapOf("title" to "Login", "userEmail" to email.text.toString()),
                {
                    startActivityForResult(ThankYouActivity.getIntent(this), 0)
                    finish()
                },
                {
                    showAlertDialogOnErrorOccured(it)
                }
            )
        }
    }

    private fun showAlertDialogOnErrorOccured(throwable: Throwable) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error!Error!Panic!")
        builder.setMessage(throwable.message)

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, LoginActivity::class.java)
    }
}
