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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.*
import com.example.projectgols.view.adapter.ViewholderKeranjang
import com.example.projectgols.view.adapter.ViewholderPesanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityPesanan : AppCompatActivity() {
    lateinit var identitasPesanan: TextView
    lateinit var lokasiPesanan: EditText
    lateinit var subtotalPesanan: TextView
    lateinit var ongkirPesanan: TextView
    lateinit var ppnPesanan: TextView
    lateinit var adminPesanan: TextView
    lateinit var totalPesanan: TextView
    lateinit var statusPesanan: TextView
    lateinit var btnBatalPesanan: Button
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    lateinit var dataPesanan: ArrayList<KeranjangBarang>
    lateinit var adapterPesanan: ViewholderKeranjang

    var formatNumber: NumberFormat = DecimalFormat("#,###")

    var konfigurasi = Konfigurasi()
    var total = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        identitasPesanan = findViewById(R.id.identitasPesanan)
        lokasiPesanan = findViewById(R.id.lokasiPesanan)
        subtotalPesanan = findViewById(R.id.subtotalPesanan)
        ongkirPesanan = findViewById(R.id.ongkirPesanan)
        ppnPesanan = findViewById(R.id.ppnPesanan)
        adminPesanan = findViewById(R.id.adminPesanan)
        totalPesanan = findViewById(R.id.totalPesanan)
        statusPesanan = findViewById(R.id.statusPesanan)
        btnBatalPesanan = findViewById(R.id.btnBatalPesanan)
        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("User", Context.MODE_PRIVATE)

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView = findViewById(R.id.recyclerPesanan)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
        dataPesanan = arrayListOf()

        FirebaseDatabase.getInstance().getReference().orderByKey().equalTo("konfigurasi")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val vl = i.getValue(Konfigurasi::class.java)

                        konfigurasi = vl!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        load(intent.getStringExtra("id_pesanan").toString())
    }

    private fun load(id: String){
        FirebaseDatabase.getInstance().getReference("pesanan").orderByKey().equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var fetchData = Pesanan()
                    for (i in snapshot.children){
                        val vlp = i.getValue(Pesanan::class.java)

                        fetchData = vlp!!
                    }
                    statusPesanan.text = fetchData.status.toUpperCase()
                    lokasiPesanan.setText(fetchData.alamat)

                    FirebaseDatabase.getInstance().getReference("user").orderByKey().equalTo(fetchData.id_user)
                        .get().addOnSuccessListener { rowu ->
                            for (i in rowu.children){
                                val vlu = i.getValue(User::class.java)

                                identitasPesanan.text = vlu!!.nama
                            }
                        }

                    var subtotal = 0
                    fetchData.detail.forEach { acc ->
                        FirebaseDatabase.getInstance().getReference("barang").orderByKey().equalTo(acc.id_brg.toString())
                            .get().addOnSuccessListener { row ->
                                for (j in row.children){
                                    val vlb = j.getValue(Barang::class.java)

                                    dataPesanan.add(KeranjangBarang(
                                        Keranjang("", "", "", acc.qty, acc.id_brg), vlb!!)
                                    )
                                    subtotal += (acc.qty * vlb.harga)
                                }
                                ongkirPesanan.text = "Rp. " + formatNumber.format(konfigurasi.ongkir) + ",00"
                                adminPesanan.text = "Rp. " + formatNumber.format(konfigurasi.biaya_admin) + ",00"
                                subtotalPesanan.text = "Rp. " + formatNumber.format(subtotal) + ",00"
                                val ppn = konfigurasi.ppn * subtotal / 100
                                ppnPesanan.text = "Rp. " + formatNumber.format(ppn) + ",00"
                                total = subtotal + konfigurasi.ongkir + ppn + konfigurasi.biaya_admin
                                totalPesanan.text = "Rp. " + formatNumber.format(total) + ",00"

                                adapterPesanan = ViewholderKeranjang(dataPesanan, 1)
                                mRecyclerView.adapter = adapterPesanan
                                adapterPesanan.notifyDataSetChanged()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}