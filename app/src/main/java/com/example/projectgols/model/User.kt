package com.example.projectgols.model

class User {
    lateinit var id_user: String
    lateinit var nama: String
    lateinit var email: String
    lateinit var password: String
    lateinit var alamat: String
    lateinit var telp: String
    lateinit var level: String

    constructor() {}
    constructor(id_user:String, nama:String, email:String, password:String, alamat:String,
                telp: String, level: String) {
        this.id_user = id_user
        this.nama = nama
        this.email = email
        this.password = password
        this.alamat = alamat
        this.telp = telp
        this.level = level
    }
}