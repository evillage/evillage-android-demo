package nl.evillage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.*
import android.widget.Button
import android.widget.Toast
import nl.evillage.clangnotifications.Clang
import nl.evillage.poll.PollActivity
import nl.evillage.R


class MainActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val registerBtn = findViewById<Button>(R.id.register)
        val loginBtn = findViewById<Button>(R.id.login)
        val pollBtn = findViewById<Button>(R.id.poll)
        val propertyBtn = findViewById<Button>(R.id.property)

        clang = Clang.getInstance(
            "https://94fd32f3.ngrok.io",
            applicationContext,
            "46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5",
            "63f4bf70-2a0d-4eb2-b35a-531da0a61b20"
        )

        // UNIQUE DEVICE IDENTIFIER: prefer to use AdvertisingId
        val deviceId: String = Secure.getString(contentResolver, Secure.ANDROID_ID)

        registerBtn.setOnClickListener {
            clang.createAccount(deviceId, {
                Toast.makeText(applicationContext, it.id, Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        }

        loginBtn.setOnClickListener {
            startActivity(LoginActivity.getIntent(this))
        }

        pollBtn.setOnClickListener {
            startActivity(PollActivity.getIntent(this))
        }

        propertyBtn.setOnClickListener {
            clang.updateProperties(mapOf("pizzaPreference" to "Calzone"), {
                Toast.makeText(applicationContext, "Pizza preference submitted", Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        }
    }
}
