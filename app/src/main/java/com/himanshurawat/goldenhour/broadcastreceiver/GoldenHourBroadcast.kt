package com.himanshurawat.goldenhour.broadcastreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.himanshurawat.goldenhour.R
import com.himanshurawat.goldenhour.ui.main.MainActivity

class GoldenHourBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("Notifications"
                , "Golden Hour Notification"
                , NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(context,"Notifications")

        notificationBuilder.setContentTitle("It's Time")
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentText("The Golden Hour is Starting Now. Get your Camera's Ready !")
        notificationBuilder.color = ContextCompat.getColor(context,R.color.colorPrimaryDark)
        val notificationIntent = Intent(context.applicationContext, MainActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context.applicationContext
            ,57
            ,notificationIntent
            , PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setAutoCancel(true)
        val notification: Notification = notificationBuilder.build()
        notificationManager.notify(57,notification)
    }
}
