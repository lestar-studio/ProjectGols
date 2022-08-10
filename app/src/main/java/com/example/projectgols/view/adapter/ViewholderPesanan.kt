package com.example.projectgols.view.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.KeranjangBarang
import com.example.projectgols.model.Pesanan
import com.example.projectgols.view.customer.ActivityDetail
import com.example.projectgols.view.customer.ActivityPesanan
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderPesanan(val data: ArrayList<Pesanan>): RecyclerView.Adapter<ViewholderPesanan.ViewHolder>() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tanggalPesanan : TextView
        val alamatPesanan : TextView
        val bayarPesanan : TextView
        val mView: View
        init {
            tanggalPesanan = view.findViewById(R.id.tanggalPesanan)
            alamatPesanan = view.findViewById(R.id.alamatPesanan)
            bayarPesanan = view.findViewById(R.id.bayarPesanan)
            mView = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_pesanan, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tanggalPesanan.text = data[position].tgl_get
        holder.alamatPesanan.text = data[position].alamat
        holder.bayarPesanan.text = "Rp. " + formatNumber.format(data[position].total) + ",00"

        holder.mView.setOnClickListener {
            val intent = Intent(it.context, ActivityPesanan::class.java)
            intent.putExtra("id_pesanan", data[position].id_pesanan)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}