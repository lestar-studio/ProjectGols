package com.example.projectgols.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.example.projectgols.view.customer.ActivityDetail
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderBeranda(val data: ArrayList<Barang>): RecyclerView.Adapter<ViewholderBeranda.ViewHolder>() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaMenu : TextView
        val hargaMenu : TextView
        val deskripsiMenu : TextView
        val imgMenu: ImageView
        val mView: View
        init {
            namaMenu = view.findViewById(R.id.namaMenu)
            hargaMenu = view.findViewById(R.id.hargaMenu)
            deskripsiMenu = view.findViewById(R.id.deskripsiMenu)
            imgMenu = view.findViewById(R.id.imgMenu)
            mView = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listmenu_beranda, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaMenu.text = data[position].nama_brg
        holder.hargaMenu.text = "Rp. " + formatNumber.format(data[position].harga.toInt()) + ",00"
        holder.deskripsiMenu.text = data[position].deskripsi
        Picasso.get().load(data[position].img_brg[0]).into(holder.imgMenu)

        holder.mView.setOnClickListener {
            val intent = Intent(it.context, ActivityDetail::class.java)
            intent.putExtra("id_brg", data[position].id_brg)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}