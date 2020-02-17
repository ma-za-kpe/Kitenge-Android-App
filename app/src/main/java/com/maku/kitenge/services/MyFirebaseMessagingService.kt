package com.maku.kitenge.services

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maku.kitenge.ui.MainActivity
import timber.log.Timber


class MyFirebaseMessagingService: FirebaseMessagingService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) { // ...
// TODO(developer): Handle FCM messages here.
// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: " + remoteMessage.from)
        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Timber.d("Message data payload: " + remoteMessage.data)
            if ( /* Check if data needs to be processed by long running job */true) { // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob()
            } else { // Handle message within 10 seconds
                handleNow()
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Timber.d("Message Notification Body: " + remoteMessage.notification!!.body)
            val notificationMsg = remoteMessage.notification!!.body
            notificationBuilder(notificationMsg)
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
// message, here is where that should be initiated. See sendNotification method below.
    }

    private fun notificationBuilder(notificationMsg: String?) {

        //handling notification click event
        //handling notification click event
        val notificationReceivedIntent: Intent = Intent(this, MainActivity::class.java)

        notificationReceivedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationReceivedIntent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val notificationSoundUri: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.alert_light_frame)
            .setContentTitle("Kitenge")
            .setContentText(notificationMsg)
            .setSound(notificationSoundUri)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setWhen(0)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.btn_default, "OPEN APP",
                pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificationMsg))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "channel_id"
        val channelName = "channel_name"
        // [START] handling notifications in android 8 and above

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        //[END] handling notifications in android 8 and above

        //[END] handling notifications in android 8 and above
        notificationManager?.notify(0, notificationBuilder.build())

    }

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        Timber.d("task is done.")

        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Timber.d("Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Timber.d("sendRegistrationTokenToServer($token)")
    }
}