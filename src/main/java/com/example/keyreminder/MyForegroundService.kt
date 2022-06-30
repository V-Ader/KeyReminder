package com.example.keyreminder

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.os.SystemClock.sleep
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*
import kotlin.math.sqrt

class MyForegroundService: Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var locationCallback: LocationCallback

    var longitude:Double?=null;
    var latitude:Double?=null;

    var longitudeLocked:Double?=null;
    var latitudeLocked:Double?=null;

    var maxDistanceLimit:Double?=null;


    var fusedLocationProviderClient: FusedLocationProviderClient?=null;

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        maxDistanceLimit = getRecord("maxDistanceLimit")?.toDouble()

        latitudeLocked = getRecord("latitude")?.toDouble()
        longitudeLocked = getRecord("longitude")?.toDouble()

        Log.v("info", "onCreate: $latitudeLocked, $longitudeLocked $maxDistanceLimit")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val t = Thread() {
            Log.v("info", "foreground is alive")
            while(true) {

                getLocation();

                Log.v("infoinit", "pin: $latitudeLocked $longitudeLocked")
                Log.v("infoinit", "data: $latitude $longitude")
                if (latitude != null && longitude != null) {

                    latitudeLocked = latitude
                    longitudeLocked = longitude


                    break
                }

                Thread.sleep(1000)

            }

            Log.v("info", "not bad")

            while(true){

                getLocation();

                Log.v("infot", "pin: $latitudeLocked $longitudeLocked")
                Log.v("infot", "data: $latitude $longitude")
                if(latitude != null && longitude != null){
                    Log.v("infot", "distance: ${getDistance()}")
                    if (getDistance() >= maxDistanceLimit!!){
                        Log.v("infot", "info: ${getDistance()}  $maxDistanceLimit")
                        sendAlert()
                        break
                    }
                }

                Thread.sleep(10000)

            }
        }

        t.start()


        val channelId = "MyChannelID"
        val channelName = "MyChannelName"
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(Notification.PRIORITY_LOW)
            .setCategory(Notification.CATEGORY_SERVICE)


        startForeground(101, notification.build())
        return super.onStartCommand(intent, flags, startId)

    }

    private fun getDistance(): Double {
        return sqrt((latitude!!-latitudeLocked!!)*(latitude!!-latitudeLocked!!) + (longitude!!-longitudeLocked!!)*(longitude!!-longitudeLocked!!))
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(p0: Location) {
//        Log.v("My info", " update onLocationChanged")
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            fusedLocationProviderClient!!.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    try {
                        Log.v("My info", "current location update")

                        latitude = location!!.latitude
                        longitude = location.longitude


                    } catch (e: Exception) { }
        }
    }

    fun sendAlert(){
        Log.v("alert", "hola hola!!!")
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, "ChannelID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getRecord("Title"))
            .setContentText(getRecord("Description"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(12, builder.build())
        }


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

    fun setRecord(u_name: String, u_password: String){
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared_pass",0)
        val edit = sharedScore.edit()
        edit.putString(u_name, u_password)
        edit.apply()
    }

    private fun getRecord(u_name: String): String? {
        val sharedData = this.getSharedPreferences("com.example.myapplication.shared_pass", 0)
        return sharedData.getString(u_name, "0")
    }
}