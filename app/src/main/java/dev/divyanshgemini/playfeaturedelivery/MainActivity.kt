package dev.divyanshgemini.playfeaturedelivery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dev.divyanshgemini.playfeaturedelivery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mySessionId = 0

    companion object {
        private const val TAG = "MainActivity"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val splitInstallManager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this@MainActivity)
    }

    private val stateUpdatedListener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() == mySessionId) {
            handleSplitInstallSessionState(state)
            printSplitInstallError(state.errorCode())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        val request =
            SplitInstallRequest.newBuilder()
                .addModule("greet")
                .build()

        binding.btnInstallAndGreet.setOnClickListener {
            splitInstallManager.startInstall(request)
                .addOnSuccessListener { sessionId ->
                    Log.i(TAG, "Split install started: sessionId = $sessionId")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Split install failed", exception)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

        splitInstallManager.registerListener(stateUpdatedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

        splitInstallManager.unregisterListener(stateUpdatedListener)
    }

    private fun printSplitInstallError(errorCode: Int) {
        when (errorCode) {
            SplitInstallErrorCode.NO_ERROR -> {
                Log.i(TAG, "No error")
            }

            SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {
                Log.i(TAG, "Error: Active sessions limit exceeded")
            }

            SplitInstallErrorCode.API_NOT_AVAILABLE -> {
                Log.i(TAG, "Error: API not available")
            }

            SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> {
                Log.i(TAG, "Error: Incompatible with existing session")
            }

            SplitInstallErrorCode.INTERNAL_ERROR -> {
                Log.i(TAG, "Error: Internal error")
            }

            SplitInstallErrorCode.INVALID_REQUEST -> {
                Log.i(TAG, "Error: Invalid request")
            }

            SplitInstallErrorCode.MODULE_UNAVAILABLE -> {
                Log.i(TAG, "Error: Module unavailable")
            }

            SplitInstallErrorCode.NETWORK_ERROR -> {
                Log.i(TAG, "Error: Network error")
            }

            SplitInstallErrorCode.SESSION_NOT_FOUND -> {
                Log.i(TAG, "Error: Session not found")
            }

            SplitInstallErrorCode.ACCESS_DENIED -> {
                Log.i(TAG, "Error: Access denied")
            }

            SplitInstallErrorCode.APP_NOT_OWNED -> {
                Log.i(TAG, "Error: App not owned")
            }

            SplitInstallErrorCode.INSUFFICIENT_STORAGE -> {
                Log.i(TAG, "Error: Insufficient storage")
            }

            SplitInstallErrorCode.PLAY_STORE_NOT_FOUND -> {
                Log.i(TAG, "Error: Play store not found")
            }

            SplitInstallErrorCode.SPLITCOMPAT_COPY_ERROR -> {
                Log.i(TAG, "Error: Split compat copy error")
            }

            SplitInstallErrorCode.SPLITCOMPAT_EMULATION_ERROR -> {
                Log.i(TAG, "Error: Split compat emulation error")
            }

            SplitInstallErrorCode.SPLITCOMPAT_VERIFICATION_ERROR -> {
                Log.i(TAG, "Error: Split compat verification error")
            }

            SplitInstallErrorCode.SERVICE_DIED -> {
                Log.i(TAG, "Error: Service died")
            }
        }
    }

    private fun handleSplitInstallSessionState(state: SplitInstallSessionState) {
        when (state.status()) {
            SplitInstallSessionStatus.PENDING -> {
                Log.i(TAG, "handleSplitInstallSessionState: Pending")
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                Log.i(
                    TAG,
                    "handleSplitInstallSessionState: Downloading: ${state.bytesDownloaded()}/${state.totalBytesToDownload()}"
                )
            }

            SplitInstallSessionStatus.DOWNLOADED -> {
                Log.i(TAG, "handleSplitInstallSessionState: Downloaded")
            }

            SplitInstallSessionStatus.INSTALLING -> {
                Log.i(TAG, "handleSplitInstallSessionState: Installing: ")
            }

            SplitInstallSessionStatus.INSTALLED -> {
                Log.i(TAG, "handleSplitInstallSessionState: Installed")

                startActivity(
                    Intent()
                        .setClassName(
                            "dev.divyanshgemini.playfeaturedelivery",
                            "dev.divyanshgemini.greet.GreetingActivity"
                        )
                )
            }

            SplitInstallSessionStatus.CANCELED -> {
                Log.i(TAG, "handleSplitInstallSessionState: Canceled")
            }

            SplitInstallSessionStatus.CANCELING -> {
                Log.i(TAG, "handleSplitInstallSessionState: Canceling")
            }

            SplitInstallSessionStatus.FAILED -> {
                Log.i(TAG, "handleSplitInstallSessionState: Failed")
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                Log.i(TAG, "handleSplitInstallSessionState: Requires user confirmation")

                splitInstallManager.startConfirmationDialogForResult(state, activityResultLauncher)
            }

            SplitInstallSessionStatus.UNKNOWN -> {
                Log.i(TAG, "handleSplitInstallSessionState: Unknown")
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                Log.i(TAG, "activityResultLauncher: User confirmed installation")
            } else {
                Log.i(TAG, "activityResultLauncher: User cancelled installation")
            }
        }
}