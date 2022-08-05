package com.example.projectgols.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderBerandaAdmin(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var barang = Barang()
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(barang: Barang) {
        this.barang = barang
        val namaMenu = mView.findViewById(R.id.namaMenu) as TextView
        val hargaMenu = mView.findViewById(R.id.hargaMenu) as TextView
        val deskripsiMenu = mView.findViewById(R.id.deskripsiMenu) as TextView
        val imgMenu = mView.findViewById(R.id.imgMenu) as ImageView

        namaMenu.text = barang.nama_brg
        hargaMenu.text = "Rp. " + formatNumber.format(barang.harga.toInt()) + ",00"
        deskripsiMenu.text = barang.deskripsi

//        val imgBarang = FirebaseDatabase.getInstance().getReference("barang")
//            .child(barang.id_brg.toString()).child("img_brg")
//        imgBarang.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(datasnapshot: DataSnapshot) {
//                for (snapshot1 in datasnapshot.children) {
//
//                    val allocation = snapshot1.getValue(GambarBarang::class.java)
//                    val arr = arrayOf(allocation!!.id)
//                    Picasso.get().load(Arrays.toString(arr)).into(imgMenu)
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })

//        Picasso.get().load(barang.img_brg.toString()).into(imgMenu)
    }

    interface ClickListener {
        fun onItemClick(view: View, position:Int)
        fun onItemLongClick(view: View, position:Int)
    }

    fun setOnClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }
}