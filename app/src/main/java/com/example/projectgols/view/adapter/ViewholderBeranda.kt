package com.example.projectgols.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderBeranda(itemView: View): RecyclerView.ViewHolder(itemView) {
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
        Picasso.get().load(barang.img_brg[0]).into(imgMenu)
    }

    interface ClickListener {
        fun onItemClick(view: View, position:Int)
        fun onItemLongClick(view: View, position:Int)
    }

    fun setOnClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }
}