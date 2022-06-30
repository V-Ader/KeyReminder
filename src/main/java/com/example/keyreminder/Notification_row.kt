package com.example.keyreminder

import java.io.FileDescriptor

class Notification_row {
    var id : Int = 0
    var login : String? = null
    var title : String? = null
    var description : String? = null


    constructor(){}

    constructor(id:Int, login:String,
                title:String, description: String){
        this.id = id
        this.login = login
        this.title = title
        this.description = description

    }
}