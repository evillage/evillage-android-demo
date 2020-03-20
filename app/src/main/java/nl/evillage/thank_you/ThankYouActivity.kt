package nl.evillage.thank_you

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import nl.evillage.R

internal class ThankYouActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)
        setResult(Activity.RESULT_OK)
        findViewById<Button>(R.id.done).setOnClickListener {
            finish()
        }
    }

    companion object {

        fun getIntent(context: Context): Intent =
            Intent(context, ThankYouActivity::class.java)
    }
}
