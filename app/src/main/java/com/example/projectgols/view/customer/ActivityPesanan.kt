package com.example.projectgols.view.customer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.*
import com.example.projectgols.view.adapter.ViewholderKeranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat


class ActivityPesanan : AppCompatActivity() {
    lateinit var identitasPesanan: TextView
    lateinit var lokasiPesanan: EditText
    lateinit var subtotalPesanan: TextView
    lateinit var ongkirPesanan: TextView
    lateinit var ppnPesanan: TextView
    lateinit var adminPesanan: TextView
    lateinit var totalPesanan: TextView
    lateinit var statusPesanan: TextView
    lateinit var constraintParent: ConstraintLayout
    lateinit var btnBatalPesanan: Button
    lateinit var btnProsesPesanan: Button
    lateinit var btnSelesaiPesanan: Button
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
        constraintParent = findViewById(R.id.constraintParent)
        btnBatalPesanan = findViewById(R.id.btnBatalPesanan)
        btnProsesPesanan = findViewById(R.id.btnProsesPesanan)
        btnSelesaiPesanan = findViewById(R.id.btnSelesaiPesanan)
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

        btnBatalPesanan.setOnClickListener {
            alertDialog.setMessage("Batalkan pesanan ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(intent.getStringExtra("id_pesanan").toString())
                            .child("status")
                            .setValue("batal")
                            .addOnSuccessListener {
                                dataPesanan.forEach { row ->
                                    FirebaseDatabase.getInstance().getReference("barang")
                                        .child(row.keranjang.id_brg.toString())
                                        .child("qty_brg")
                                        .setValue(row.keranjang.qty + row.barang.qty_brg)
                                }
                                val intent = Intent(this@ActivityPesanan, com.example.projectgols.view.admin.ActivityUtama::class.java)
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

        btnProsesPesanan.setOnClickListener {
            alertDialog.setMessage("Lanjut proses pesanan ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(intent.getStringExtra("id_pesanan").toString())
                            .child("status")
                            .setValue("proses")
                            .addOnSuccessListener {
                                val intent = Intent(this@ActivityPesanan, com.example.projectgols.view.admin.ActivityUtama::class.java)
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

        btnSelesaiPesanan.setOnClickListener {
            alertDialog.setMessage("Lanjut proses pesanan ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(intent.getStringExtra("id_pesanan").toString())
                            .child("status")
                            .setValue("selesai")
                            .addOnSuccessListener {
                                val intent = Intent(this@ActivityPesanan, com.example.projectgols.view.admin.ActivityUtama::class.java)
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



                    if(SP.getString("level", "").equals("Admin")){
                        if(fetchData.status.equals("menunggu")){
                            btnBatalPesanan.visibility = View.VISIBLE
                            btnProsesPesanan.visibility = View.VISIBLE
                        }
                        if(fetchData.status.equals("proses")) {
                            btnBatalPesanan.visibility = View.VISIBLE
                            btnSelesaiPesanan.visibility = View.VISIBLE
                        }
                    }
                    else {
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(constraintParent)

                        if(fetchData.status.equals("menunggu")) {
                            btnBatalPesanan.visibility = View.VISIBLE
                        }
                    }

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