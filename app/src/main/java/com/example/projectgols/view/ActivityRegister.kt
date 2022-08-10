package com.example.projectgols.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.projectgols.R
import com.example.projectgols.model.User
import com.example.projectgols.view.customer.ActivityUtama
import com.google.firebase.database.FirebaseDatabase

class ActivityRegister : AppCompatActivity() {
    lateinit var namaRegister: EditText
    lateinit var emailRegister: EditText
    lateinit var passwordRegister: EditText
    lateinit var konfirmasiRegister: EditText
    lateinit var alamatRegister: EditText
    lateinit var telpRegister: EditText
    lateinit var btnRegister: Button
    lateinit var textLogin: TextView
    lateinit var SP: SharedPreferences

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
        SP = getSharedPreferences("User", Context.MODE_PRIVATE)

        textLogin.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            if (validate()){
                val ref = FirebaseDatabase.getInstance().getReference("user")
                val id  = ref.push().key.toString()
                ref.child(id).setValue(
                    User(
                        id,
                        namaRegister.text.toString(),
                        emailRegister.text.toString(),
                        passwordRegister.text.toString(),
                        alamatRegister.text.toString(),
                        telpRegister.text.toString(),
                        "Customer"
                    )
                ).addOnSuccessListener {
                    val editor = SP.edit()
                    editor.putString("id_user", id)
                    editor.putString("nama", namaRegister.text.toString())
                    editor.putString("email", emailRegister.text.toString())
                    editor.putString("password", passwordRegister.text.toString())
                    editor.putString("alamat", alamatRegister.text.toString())
                    editor.putString("telp", telpRegister.text.toString())
                    editor.putString("level", "Customer")
                    editor.apply()

                    Toast.makeText(this, "Berhasil register", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,
                        ActivityUtama::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validate(): Boolean {
        if(namaRegister.text.toString() == "") {
            Toast.makeText(this, "Nama masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(emailRegister.text.toString() == "") {
            Toast.makeText(this, "Email masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passwordRegister.text.toString() == "") {
            Toast.makeText(this, "Password masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(konfirmasiRegister.text.toString() == "") {
            Toast.makeText(this, "Konfirmasi masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(alamatRegister.text.toString() == "") {
            Toast.makeText(this, "Alamat masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(telpRegister.text.toString() == "") {
            Toast.makeText(this, "Nomor Telepon masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passwordRegister.text.toString() != konfirmasiRegister.text.toString()) {
            Toast.makeText(this, "Konfirmasi password tidak sama", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun register(){

    }
}