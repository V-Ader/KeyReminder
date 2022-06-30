package com.example.keyreminder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.keyreminder.User

class DBHelperLogin(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VER
) {
    companion object {
        private val DATABASE_VER = 3
        private val DATABASE_NAME = "EDMTDB.db"
        //Table
        private val TABLE_NAME = "Users"
        private val COL_ID = "Id"
        private val COL_LOGIN = "Login"
        private val COL_PASSWORD = "Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_LOGIN TEXT, $COL_PASSWORD TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }

    //CRUD
    val allUsers:List<User>
        @SuppressLint("Range")
        get(){
            val listMessage = ArrayList<User>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor =  db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do {
                    val message = User()
                    message.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    message.login = cursor.getString(cursor.getColumnIndex(COL_LOGIN))
                    message.password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))

                    listMessage.add(message)
                } while (cursor.moveToNext())
            }
            db.close()
            return listMessage
        }

    @SuppressLint("Range")
    fun getUser(login:String):String{
        var ans = ""
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_LOGIN = '$login'"
        val db = this.writableDatabase
        val cursor =  db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){

            ans = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))


        }
        db.close()
        return ans
    }

    fun addUser(user:User){
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_LOGIN, user.login)
        values.put(COL_PASSWORD, user.password)

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