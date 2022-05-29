package com.example.mqttkotlinsample.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mqttkotlinsample.MainActivity
import com.example.mqttkotlinsample.R


class Notification(private  val context: Context, private  val icon: Bitmap) {//(private  val context: Context,activity : MainActivity?, private  val icon: Bitmap) {

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    //var main: MainActivity? = null


    fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Not Title"
            val descriptionText = "Not description"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel: NotificationChannel =  NotificationChannel (CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel (channel)
        }
    }

    fun sentNotification(tag: String,  message:String, currentContext: Context = context){
        val intent: Intent = Intent(currentContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivities(context,0, arrayOf(intent),0)

        val builder = NotificationCompat.Builder(currentContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.mind)
            .setContentTitle(tag)
            .setContentText(message)
            //  .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(icon)
            .setContentIntent(pendingIntent)


        with(NotificationManagerCompat.from(currentContext)) {
            notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
        }
    }

}