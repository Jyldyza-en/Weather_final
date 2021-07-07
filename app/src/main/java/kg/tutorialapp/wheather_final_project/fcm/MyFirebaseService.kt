package kg.tutorialapp.wheather_final_project.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kg.tutorialapp.wheather_final_project.R
import kg.tutorialapp.wheather_final_project.ui.MainActivity

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA", remoteMessage.data["body"])
        }

        val pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "myChannel"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = Notification.Builder(this, channelId)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["body"])
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_water_drop)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}