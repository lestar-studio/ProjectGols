package com.example.projectgols.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.projectgols.R

class ActivityRegister : AppCompatActivity() {
    lateinit var namaRegister: EditText
    lateinit var emailRegister: EditText
    lateinit var passwordRegister: EditText
    lateinit var konfirmasiRegister: EditText
    lateinit var alamatRegister: EditText
    lateinit var telpRegister: EditText
    lateinit var btnRegister: Button
    lateinit var textLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        namaRegister = findViewById(R.id.namaRegister)
        emailRegister = findViewById(R.id.emailRegister)
        passwordRegister = findViewById(R.id.passwordRegister)
        konfirmasiRegister = findViewById(R.id.konfirmasiRegister)
        alamatRegister = findViewById(R.id.alamatRegister)
        telpRegister = findViewById(R.id.telpRegister)
        btnRegister = findViewById(R.id.btnRegister)
        textLogin = findViewById(R.id.textLogin)

        textLogin.setOnClickListener {
            finish()
        }
    }
}