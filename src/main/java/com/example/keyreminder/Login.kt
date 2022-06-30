package com.example.keyreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = this.findViewById<Button>(R.id.button5)

        val login = this.findViewById<EditText>(R.id.login)
        val password = this.findViewById<EditText>(R.id.password)

        val db = DBHelperLogin(this)
        setRecord("Current", "unspecified")

//        bScoreboard.setOnClickListener {
//
//            val intent = Intent(this, NotificationsList::class.java)
//            startActivity(intent)
//        }

        button.setOnClickListener{
//            val tmess = "dane: ${login.text.toString()} ${password.text.toString()}"
//            Toast.makeText(applicationContext, tmess, Toast.LENGTH_LONG).show()

            if(login.text.toString() == ""){
                Toast.makeText(applicationContext, "Wrong username", Toast.LENGTH_LONG).show()
            }
            else if(login.text.toString() == "Current" || login.text.toString() == "unspecified" ){
                Toast.makeText(applicationContext, "Wrong username", Toast.LENGTH_LONG).show()
            }
            else if(!db.testOne(login.text.toString())) {
                val user = User(
                    0,
                    login.text.toString(),
                    password.text.toString()
                )
                db.addUser(user)

                Toast.makeText(applicationContext, "New user added", Toast.LENGTH_LONG).show()
                setRecord("Current", login.text.toString())

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{

                if( db.getUser(login.text.toString()).equals(password.text.toString()) ){
                    setRecord("Current", login.text.toString())
                    Toast.makeText(applicationContext, "Welcome ${getRecord("Current")}", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, NotificationsList::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(applicationContext, "Wrong password", Toast.LENGTH_LONG).show()
                }
            }


        }


    }


    fun setRecord(u_name: String, u_password: String){
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared_pass",0)
        val edit = sharedScore.edit()
        edit.putString(u_name, u_password)
        edit.apply()
    }

    private fun getRecord(u_name: String): String? {
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared_pass", 0)
        return sharedScore.getString(u_name, null)
    }
}