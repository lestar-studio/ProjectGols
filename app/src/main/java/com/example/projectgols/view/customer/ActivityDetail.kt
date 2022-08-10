package com.example.projectgols.view.customer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.example.projectgols.model.Keranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityDetail : AppCompatActivity() {
    lateinit var namaDetail: TextView
    lateinit var imageSlider: ImageSlider
    lateinit var hargaDetail: TextView
    lateinit var stokDetail: TextView
    lateinit var deskripsiDetail: TextView
    lateinit var btnPesan: Button
    var imgList = ArrayList<SlideModel>()
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var id_barang = 0
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        namaDetail  = findViewById(R.id.namaDetail)
        hargaDetail  = findViewById(R.id.hargaDetail)
        stokDetail  = findViewById(R.id.stokDetail)
        deskripsiDetail  = findViewById(R.id.deskripsiDetail)
        btnPesan = findViewById(R.id.btnPesan)
        SP = getSharedPreferences("User", Context.MODE_PRIVATE)

        imageSlider = findViewById(R.id.imgDetail)

        id_barang = intent.getStringExtra("id_brg").toString().toInt()
        load(id_barang.toString())

        btnPesan.setOnClickListener {
            addKeranjang()
        }
    }

    private fun load(id: String){
        FirebaseDatabase.getInstance().getReference("barang").orderByKey().equalTo(id).get().addOnSuccessListener {
            for (i in it.children){
                val vl = i.getValue(Barang::class.java)
                namaDetail.setText(vl!!.nama_brg)
                hargaDetail.text = "Rp. " + formatNumber.format(vl.harga.toInt()) + ",00"
                stokDetail.text = vl.qty_brg.toString()
                deskripsiDetail.text = vl.deskripsi
                vl.img_brg.forEach { row ->
                    imgList.add(SlideModel(row))
                }
            }
            imageSlider.setImageList(imgList, ScaleTypes.CENTER_INSIDE)
        }
    }

    private fun addKeranjang(){
        val ref = FirebaseDatabase.getInstance().getReference("keranjang")
        val id  = ref.push().key.toString()
        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())

        val newdata = Keranjang(id,
            SP.getString("id_user", "").toString(),
            currentDate.toString(), 1, id_barang)
        ref.child(id)
            .setValue(newdata).addOnCompleteListener {
                val intent = Intent(this@ActivityDetail,
                    ActivityKeranjang::class.java)
                startActivity(intent)
                finish()
            }
//        FirebaseDatabase.getInstance().getReference("keranjang")
//            .orderByKey().limitToLast(1).addListenerForSingleValueEvent( object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val sdf = SimpleDateFormat("dd-M-yyyy")
//                    val currentDate = sdf.format(Date())
//                    if (snapshot.exists()){
//                        for (data in snapshot.children){
//                            val value = data.getValue(Keranjang::class.java)
//                            val lastid = value!!.id_keranjang + 1
//
//                            val newdata = Keranjang(lastid,
//                                SP.getString("id_user", "").toString().toInt(),
//                                currentDate.toString(), 1, id_barang)
//                            FirebaseDatabase.getInstance().getReference("keranjang").child(lastid.toString())
//                                .setValue(newdata).addOnCompleteListener {
//                                    val intent = Intent(this@ActivityDetail,
//                                        ActivityKeranjang::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }
//                        }
//                    }
//                    else{
//                        val lastid = 0
//
//                        val newdata = Keranjang(lastid,
//                            SP.getString("id_user", "").toString().toInt(),
//                            currentDate.toString(), 1, id_barang)
//                        FirebaseDatabase.getInstance().getReference("keranjang").child(lastid.toString())
//                            .setValue(newdata).addOnCompleteListener {
//                                val intent = Intent(this@ActivityDetail,
//                                    ActivityKeranjang::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
    }
}