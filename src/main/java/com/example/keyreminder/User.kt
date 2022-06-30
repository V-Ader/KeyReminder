package com.example.keyreminder

class User {
    var id : Int = 0
    var login : String? = null
    var password : String? = null


    constructor(){}

    constructor(id:Int, login:String,
                password:String){
        this.id = id
        this.login = login
        this.password = password

    }
}