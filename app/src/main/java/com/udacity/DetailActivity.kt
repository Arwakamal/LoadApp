package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

const val DETAIL_ACTIVITY_INTENT_STATUS_KEY = "Status"
const val DETAIL_ACTIVITY_INTENT_FILENAME_KEY = "FileName"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        val fileName = intent.getStringExtra(DETAIL_ACTIVITY_INTENT_STATUS_KEY)
        file_name_value.text = fileName

        val status = intent.getStringExtra(DETAIL_ACTIVITY_INTENT_FILENAME_KEY)
        file_status_value.text = status

        btn_ok.setOnClickListener {
            finish()
        }
    }

}
