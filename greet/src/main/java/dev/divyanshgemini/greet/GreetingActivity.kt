package dev.divyanshgemini.greet

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import dev.divyanshgemini.greet.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "GreetingActivity"
    }

    private val binding: ActivityGreetingBinding by lazy {
        ActivityGreetingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d(TAG, "onCreate")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Log.d(TAG, "attachBaseContext")

        SplitCompat.installActivity(this)
    }
}