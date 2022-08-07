package com.example.projectgols.view.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityDetail : AppCompatActivity() {
    lateinit var namaDetail: TextView
    lateinit var imageSlider: ImageSlider
    lateinit var hargaDetail: TextView
    lateinit var stokDetail: TextView
    lateinit var deskripsiDetail: TextView
    var imgList = ArrayList<SlideModel>()
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        namaDetail  = findViewById(R.id.namaDetail)
        hargaDetail  = findViewById(R.id.hargaDetail)
        stokDetail  = findViewById(R.id.stokDetail)
        deskripsiDetail  = findViewById(R.id.deskripsiDetail)

        imageSlider = findViewById(R.id.imgDetail)

        load(intent.getStringExtra("id_brg").toString())
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
}