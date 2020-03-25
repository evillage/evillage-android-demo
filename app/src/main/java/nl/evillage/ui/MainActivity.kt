package nl.evillage.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.evillage.R
import nl.worth.clangnotifications.Clang

class MainActivity : AppCompatActivity() {

    lateinit var clang: Clang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clang = Clang.getInstance(applicationContext,"46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5", "63f4bf70-2a0d-4eb2-b35a-531da0a61b20")
    }
}
