package com.maku.kitenge

import android.R
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity : AppCompatActivity() {

    // Creates instance of the manager.
    var appUpdateManager = AppUpdateManagerFactory.create(this)

    // Returns an intent object that you use to check for an update.
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    private val MY_REQUEST_CODE = 20154

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkUpdate()

    }

    private fun checkUpdate() {

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.d(
                "appUpdateInfo :",
                "packageName :" + appUpdateInfo.packageName() + ", " + "availableVersionCode :" + appUpdateInfo.availableVersionCode() + ", " + "updateAvailability :" + appUpdateInfo.updateAvailability() + ", " + "installStatus :" + appUpdateInfo.installStatus()
            )
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                requestUpdate(appUpdateInfo)
                Log.d("UpdateAvailable", "update is there ")
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                Log.d("Update", "3")
                notifyUser()
            } else {
                Toast.makeText(this@MainActivity, "No Update Available", Toast.LENGTH_SHORT)
                    .show()
                Log.d("NoUpdateAvailable", "update is not there ")
            }

        }
    }

    //request update
    private fun requestUpdate(appUpdateInfo: AppUpdateInfo): Unit {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                FLEXIBLE,
                this,
                MY_REQUEST_CODE
            )
            onResume()
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    //get a callback update status
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "RESULT_OK$resultCode", Toast.LENGTH_LONG).show()
                    Log.d("RESULT_OK  :", "" + resultCode)
                }
                Activity.RESULT_CANCELED -> if (resultCode != Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "RESULT_CANCELED$resultCode", Toast.LENGTH_LONG).show()
                    Log.d("RESULT_CANCELED  :", "" + resultCode)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> if (resultCode != RESULT_IN_APP_UPDATE_FAILED) {
                    Toast.makeText(
                        this,
                        "RESULT_IN_APP_UPDATE_FAILED$resultCode",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("RESULT_IN_APP_FAILED:", "" + resultCode)
                }
            }
        }
    }

    //handling the flexible ui
    var listener: InstallStateUpdatedListener? =
        InstallStateUpdatedListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                Log.d("InstallDownloded", "InstallStatus sucsses")
                notifyUser()
            }
        }

    private fun notifyUser() {
        val snackbar = Snackbar.make(
            findViewById(R.id.message),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.black))
            show()
        }

        snackbar.setActionTextColor(
            getResources().getColor(R.color.black));
        snackbar.show()

    }

    // Checks that the update is not stalled during 'onResume()'.
    // However, you should execute this check at all app entry points.
    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    notifyUser();
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(this as InstallStateUpdatedListener)
    }

}
