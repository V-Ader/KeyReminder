package com.example.keyreminder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.keyreminder.User

class DBHelperNotifications(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VER
) {
    companion object {
        private val DATABASE_VER = 3
        private val DATABASE_NAME = "EDMTDBNots.db"
        //Table
        private val TABLE_NAME = "Notifications"
        private val COL_ID = "Id"
        private val COL_LOGIN = "User"
        private val COL_TITLE = "Title"
        private val COL_DESCRIPTION = "Description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_LOGIN TEXT, $COL_TITLE TEXT, $COL_DESCRIPTION TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }

    //CRUD
    @SuppressLint("Range")
    fun getAllElements(user: String):List<Notification_row>
    {
        val listMessage = ArrayList<Notification_row>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_LOGIN = '$user'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val message = Notification_row()
                message.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                message.login = cursor.getString(cursor.getColumnIndex(COL_LOGIN))
                message.title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
                message.description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION))

                listMessage.add(message)
            } while (cursor.moveToNext())

            db.close()
            return listMessage
        }

        return emptyList()
    }



    fun addElement(not:Notification_row){
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_LOGIN, not.login)
        values.put(COL_TITLE, not.title)
        values.put(COL_DESCRIPTION, not.description)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun testOne(key:String): Boolean {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_LOGIN = '$key'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            db.close()
            return true
//            if(key == cursor.getString(cursor.getColumnIndex(COL_LOGIN))) {
//                return false
//            }
        }
        db.close()
        return false
    }


}