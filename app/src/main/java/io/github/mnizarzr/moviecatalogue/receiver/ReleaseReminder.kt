package io.github.mnizarzr.moviecatalogue.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import io.github.mnizarzr.moviecatalogue.MainActivity
import io.github.mnizarzr.moviecatalogue.R
import io.github.mnizarzr.moviecatalogue.data.model.ApiResponse
import io.github.mnizarzr.moviecatalogue.data.model.ItemResult
import io.github.mnizarzr.moviecatalogue.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReleaseReminder : BroadcastReceiver() {

    companion object {
        private const val REQUEST_CODE = 200
        private const val CHANNEL_NAME = "release reminder channel"
        private const val CHANNEL_ID = "channel_02"
    }

    private val notificationId = 1

    override fun onReceive(context: Context, intent: Intent) {
        getTodayRelease(context)
    }

    private fun showNotification(context: Context, movie: ItemResult, notificationId: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_24dp)
            .setContentTitle(movie.title)
            .setContentText("${movie.title} ${context.resources.getString(R.string.has_release)}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 400, 200, 600))
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setChannelId(CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                lightColor = Color.CYAN
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())

    }


    private fun getTodayRelease(context: Context) {
        val apiService = ApiClient.getService()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        apiService.getTodayRelease(currentDate, currentDate)
            .enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.d("ERR_GET_TODAY_RELEASE", t.message ?: "Unknown error")
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    response.body()?.let {
                        getThreeMovies(context, it.results)
                    }
                }
            })

    }

    private fun getThreeMovies(context: Context, items: ArrayList<ItemResult>) {
        val newItems = arrayListOf<ItemResult>()
        for ((index, value) in items.withIndex()) {
            if (index < 3) {
                newItems.add(value)
            }
        }
        setNotification(context, newItems)
    }

    private fun setNotification(context: Context, items: ArrayList<ItemResult>) {
        GlobalScope.launch(Dispatchers.Main) {
            var id = notificationId
            for (item in items) {
                showNotification(context, item, id)
                id++
            }
        }
    }

    fun setReminder(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(context, ReleaseReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    fun isSet(context: Context): Boolean {
        val intent = Intent(context, ReleaseReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        Log.d("RELEASE ALARM IS SET", (pendingIntent != null).toString())
        return pendingIntent != null
    }

}