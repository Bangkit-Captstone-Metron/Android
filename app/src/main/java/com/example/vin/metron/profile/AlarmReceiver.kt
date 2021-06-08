package com.example.vin.metron.profile

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.vin.metron.*
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            ACTION_REMIND_PLN ->{
                val message = "Jangan lupa mengupload meteran listrik bulan ini di Metron!!"
                showReminderNotification(context=context,notifId= NOTIF_REQ_CODE_PLN,message=message)
            }
            ACTION_REMIND_PDAM ->{
                val message = "Jangan lupa mengupload meteran air bulan ini di Metron!!"
                showReminderNotification(context=context,notifId= NOTIF_REQ_CODE_PDAM,message=message)
            }
            else->{
                Toast.makeText(context,"Unknown Action",Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun setAlarm(context: Context, schedule: Calendar?,isPLN:Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = if (isPLN) ACTION_REMIND_PLN else ACTION_REMIND_PDAM

        val reqCode = if (isPLN) NOTIF_REQ_CODE_PLN else NOTIF_REQ_CODE_PDAM
        val pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0)
        if (schedule != null) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                schedule.timeInMillis,
                AlarmManager.INTERVAL_DAY * ALARM_RECURRING_PERIOD,
                pendingIntent
            )
        }
    }

    private fun showReminderNotification(context: Context, notifId: Int, message: String) {
        val type = if (notifId == NOTIF_REQ_CODE_PLN) "listrik" else "air"
        val channelId = "Reminder Penggunaan"
        val channelName = "Reminder Penggunaan"
        val title = "Reminder upload penggunaan $type"
        val notificationCompatManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val onTapPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notification_important_30)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(onTapPendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationCompatManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationCompatManager.notify(notifId, notification)
    }
}