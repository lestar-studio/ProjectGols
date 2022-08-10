package com.example.projectgols.view.customer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectgols.R
import com.example.projectgols.model.*
import com.example.projectgols.view.adapter.ViewholderBeranda
import com.example.projectgols.view.adapter.ViewholderKeranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
    lateinit var SP: SharedPreferences

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    lateinit var dataKeranjang: ArrayList<KeranjangBarang>
    lateinit var adapterKeranjang: ViewholderKeranjang

    var formatNumber: NumberFormat = DecimalFormat("#,###")

    var konfigurasi = Konfigurasi()

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
        SP = getSharedPreferences("User", Context.MODE_PRIVATE)

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView = findViewById(R.id.recyclerKeranjang)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
        dataKeranjang = arrayListOf()

        identitasKeranjang.text = SP.getString("nama", "").toString()
        lokasiKeranjang.setText(SP.getString("alamat", ""))

        FirebaseDatabase.getInstance().getReference().orderByKey().equalTo("konfigurasi")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val vl = i.getValue(Konfigurasi::class.java)

                        konfigurasi = vl!!
                        ongkirKeranjang.text = "Rp. " + formatNumber.format(vl.ongkir) + ",00"
                        adminKeranjang.text = "Rp. " + formatNumber.format(vl.biaya_admin) + ",00"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        load(SP.getString("id_user", "").toString())

        btnPesanKeranjang.setOnClickListener {
            alertDialog.setMessage("Cek kembali pesanan anda, Apakah sudah sesuai ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            // Save Pesanan
                            savePesanan()

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

    private fun load(id: String){
        FirebaseDatabase.getInstance().getReference("keranjang").orderByChild("id_user").equalTo(id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var subtotal = 0
                    dataKeranjang.clear()
                    for (i in snapshot.children){
                        val vl = i.getValue(Keranjang::class.java)

                        FirebaseDatabase.getInstance().getReference("barang").orderByKey().equalTo(vl!!.id_brg.toString())
                            .get().addOnSuccessListener { row ->
                                for (j in row.children){
                                    val vlb = j.getValue(Barang::class.java)

                                    dataKeranjang.add(KeranjangBarang(vl, vlb!!))
                                    subtotal += (vl.qty * vlb.harga)
                                }
                                subtotalKeranjang.text = "Rp. " + formatNumber.format(subtotal) + ",00"
                                val ppn = konfigurasi.ppn * subtotal / 100
                                ppnKeranjang.text = "Rp. " + formatNumber.format(ppn) + ",00"
                                val total = subtotal + konfigurasi.ongkir + ppn + konfigurasi.biaya_admin
                                totalKeranjang.text = "Rp. " + formatNumber.format(total) + ",00"

                                adapterKeranjang = ViewholderKeranjang(dataKeranjang)
                                mRecyclerView.adapter = adapterKeranjang
                                adapterKeranjang.notifyDataSetChanged()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun savePesanan(){
        val ref = FirebaseDatabase.getInstance().getReference("pesanan")
        val id  = ref.push().key.toString()

        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())

        var detail = arrayListOf<DetailPesanan>()
        var index = 0
        dataKeranjang.forEach {
            detail.add(
                DetailPesanan(
                    id, index, it.keranjang.id_brg, it.keranjang.qty
                )
            )
            index++
        }
        val newValue = Pesanan(id, SP.getString("id_user", "").toString(), currentDate, "menunggu", detail)
        ref.child(id).setValue(newValue).addOnSuccessListener {
            dataKeranjang.forEach {
                val barang = it.barang
                val keranjang = it.keranjang
                FirebaseDatabase.getInstance().getReference("log_keranjang")
                    .child(keranjang.id_keranjang)
                    .setValue(it)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("barang")
                            .child(barang.id_brg.toString())
                            .child("qty_brg")
                            .setValue(barang.qty_brg - keranjang.qty)
                        FirebaseDatabase.getInstance().getReference("keranjang").child(keranjang.id_keranjang).removeValue()
                    }
            }
            Toast.makeText(this, "Berhasil membuat pesanan", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal membuat pesanan", Toast.LENGTH_SHORT).show()
        }
    }
}