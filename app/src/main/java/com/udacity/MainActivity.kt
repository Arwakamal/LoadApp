package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var URL = ""
    private var projectName=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            if (radio_btn_glide.isChecked == false && radio_btn_load_app.isChecked == false && radio_btn_retrofit.isChecked == false) {
                Toast.makeText(this, "Please select file", Toast.LENGTH_SHORT).show()
            } else {

                setDownloadURL()
                download()
                custom_button.buttonState = ButtonState.Loading
            }
        }

        createChannel(CHANNEL_ID, getString(R.string.notification_channel_name))
    }

    private fun setDownloadURL() {
        if (radio_btn_glide.isChecked) {
            this.URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
            projectName="Glide Project"
        }
        if (radio_btn_load_app.isChecked){
            projectName="Load app project"
            this.URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        }
        if(radio_btn_retrofit.isChecked) {
            projectName="Retrofit Project"
            this.URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            custom_button.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var downloadStatus = "Fail"
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    downloadStatus = "Success"
                }

                val toast = Toast.makeText(
                        applicationContext,
                        getString(R.string.notification_description),
                        Toast.LENGTH_LONG)
                toast.show()

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.sendNotification(
                        CHANNEL_ID,
                        getString(R.string.notification_description),
                        applicationContext,
                        downloadStatus,
                        projectName
                )
            }
        }
    }



    private fun download() {
        val request =
                DownloadManager.Request(Uri.parse(URL))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_name_description)

            val notificationManager = getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    companion object {

        private const val CHANNEL_ID = "channelId"
    }


}
