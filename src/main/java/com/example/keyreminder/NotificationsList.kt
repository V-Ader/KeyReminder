package com.example.keyreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import java.util.Objects.hash

class NotificationsList : AppCompatActivity() {

    var titletxt: EditText?=null
    var infotxt: EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_list)

        titletxt = findViewById<EditText>(R.id.titleTXT)
        infotxt = findViewById<EditText>(R.id.infoTXT)
        val addButton = findViewById<Button>(R.id.addButton)

        var labels_ = mutableListOf<String>()
        var descs_ = mutableListOf<String>()

        val db = DBHelperNotifications(this)
        val rows = db.getAllElements(getRecord("Current").toString())

        labels_.add("Warning!")
        descs_.add("Check if you took keys")
        for(row in rows){
            labels_.add(row.title.toString())
            descs_.add(row.description.toString())
        }

        addButton.setOnClickListener(){
            db.addElement(Notification_row(hash(titletxt?.text), getRecord("Current").toString(), titletxt?.text.toString(), infotxt?.text.toString()))
            titletxt?.text?.clear()
            infotxt?.text?.clear()
            Toast.makeText(this, "Element added", Toast.LENGTH_SHORT).show()

            finish();
            startActivity(intent);
        }


        val list = findViewById<ListView>(R.id.list)

        val adapter = MyListAdapter(this, labels_.toTypedArray(), descs_.toTypedArray())
        list.adapter = adapter

        list.setOnItemClickListener(){adapterView, view, position, id ->
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val title = labels_[itemIdAtPos.toInt()]
            val desc = descs_[itemIdAtPos.toInt()]

            setRecord("Title", title)
            setRecord("Description", desc)

        }

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
}