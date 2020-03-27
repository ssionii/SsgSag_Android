package com.icoo.ssgsag_android.util.sdk.firebase

import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import androidx.core.app.NotificationCompat
import android.util.Log
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.R

class FireBaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage!!.from!!)
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message detailData payload: " + remoteMessage.data)

            if (true) {

            } else {
                handleNow()
            }
        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            sendNotification(remoteMessage.notification!!.body, remoteMessage.notification!!.title, remoteMessage.data)
        }
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendNotification(messageBody: String?, messageTitle: String?, type: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        //val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if((type.getValue("type") == "card" && SharedPreferenceController.getReceivableCard(this) == "true")) {
            val notificationBuilder =
                NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(R.drawable.ic_main_active)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //val channelName = getString(R.string.default_notification_channel_name)
                val channel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        } else if((type.getValue("type") == "todo" && SharedPreferenceController.getReceivableTodo(this) == "true")) {
            val notificationBuilder =
                NotificationCompat.Builder(this, "channelId")
                    .setSmallIcon(R.drawable.ic_main_active)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //val channelName = getString(R.string.default_notification_channel_name)
                val channel = NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }

}