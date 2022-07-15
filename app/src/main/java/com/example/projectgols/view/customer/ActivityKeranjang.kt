package com.example.projectgols.view.customer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.example.projectgols.R

class ActivityKeranjang : AppCompatActivity() {
    lateinit var identitasKeranjang: TextView
    lateinit var lokasiKeranjang: EditText
    lateinit var subtotalKeranjang: TextView
    lateinit var ongkirKeranjang: TextView
    lateinit var ppnKeranjang: TextView
    lateinit var adminKeranjang: TextView
    lateinit var totalKeranjang: TextView
    lateinit var btnPesanKeranjang: Button
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        identitasKeranjang = findViewById(R.id.identitasKeranjang)
        lokasiKeranjang = findViewById(R.id.lokasiKeranjang)
        subtotalKeranjang = findViewById(R.id.subtotalKeranjang)
        ongkirKeranjang = findViewById(R.id.ongkirKeranjang)
        ppnKeranjang = findViewById(R.id.ppnKeranjang)
        adminKeranjang = findViewById(R.id.adminKeranjang)
        totalKeranjang = findViewById(R.id.totalKeranjang)
        btnPesanKeranjang = findViewById(R.id.btnPesanKeranjang)
        alertDialog = AlertDialog.Builder(this)

        btnPesanKeranjang.setOnClickListener {
            alertDialog.setMessage("Cek kembali pesanan anda, Apakah sudah sesuai ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            // Save Pesanan

                            val intent = Intent(this@ActivityKeranjang, ActivityUtama::class.java)
                            intent.putExtra("pesanan", "true")
                            startActivity(intent)
                            finish()
                        }
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun validate(): Boolean {
        if(lokasiKeranjang.text.toString() == "") {
            Toast.makeText(this, "Alamat masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}