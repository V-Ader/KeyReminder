package com.example.keyreminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    var txtPostion:TextView?=null;

    var button:Button?=null;
    var buttonsetNotifications:Button?=null;

    var monitoring:Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        buttonsetNotifications = findViewById(R.id.NewPositionSet)

        setRecord("Current", "unspecified")

        setRecord("Title", "Remember!")
        setRecord("Description", "Are you sure you have your keys?")


        button!!.setOnClickListener() {
            if (!monitoring){
                setRecord("maxDistanceLimit", "0.0001")
                setRecord("latitude", "0")
                setRecord("longitude", "0")

                val servicesIntent = Intent(this, MyForegroundService::class.java)
                startForegroundService(servicesIntent)

                button?.text = "STOP MONITORING"
            }
            else{
                button?.text = "START MONITORING"
                stopService(Intent(this, MyForegroundService::class.java))
            }

            monitoring = !monitoring

        }

        buttonsetNotifications!!.setOnClickListener(){

            if(getRecord("Current")!= "unspecified" && getRecord("Current")!= "0"){
                val intent = Intent(this, NotificationsList::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }

        }


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            askPermission()
        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            101)
    }

    fun setRecord(key: String, value: String){
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared_pass",0)
        val edit = sharedScore.edit()
        edit.putString(key, value)
        edit.apply()
    }

    private fun getRecord(key: String): String? {
        val sharedData = this.getSharedPreferences("com.example.myapplication.shared_pass", 0)
        return sharedData.getString(key, "0")
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "Channel"
        val id = "ChannelID"
        val descriptionText = "the descr"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}


//            createNotificationChannel()
//            val builder = NotificationCompat.Builder(this, "ChannelID")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("textTitle")
//                .setContentText("textContent")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//            with(NotificationManagerCompat.from(this)) {
//                // notificationId is a unique int for each notification that you must define
//                notify(123, builder.build())
//            }