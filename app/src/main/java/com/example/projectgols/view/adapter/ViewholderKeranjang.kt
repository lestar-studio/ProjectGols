package com.example.projectgols.view.adapter

import android.content.DialogInterface
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
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderKeranjang(val data: ArrayList<KeranjangBarang>): RecyclerView.Adapter<ViewholderKeranjang.ViewHolder>() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaBarang : TextView
        val hargaBarang : TextView
        val stokBarang : TextView
        val tambahBarang : ImageView
        val kurangBarang: ImageView
        val jumlahBarang: EditText
        init {
            namaBarang = view.findViewById(R.id.namaBarang)
            hargaBarang = view.findViewById(R.id.hargaBarang)
            stokBarang = view.findViewById(R.id.stokBarang)
            tambahBarang = view.findViewById(R.id.tambahBarang)
            kurangBarang = view.findViewById(R.id.kurangBarang)
            jumlahBarang = view.findViewById(R.id.jumlahBarang)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_keranjang, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaBarang.text = data[position].barang.nama_brg
        holder.hargaBarang.text = "Rp. " + formatNumber.format(data[position].barang.harga) + ",00"
        holder.stokBarang.text = data[position].barang.qty_brg.toString()

        var jumlah = data[position].keranjang.qty
        holder.jumlahBarang.setText(jumlah.toString())

        if(jumlah.equals(data[position].barang.qty_brg))
            holder.tambahBarang.visibility = View.INVISIBLE
        else
            holder.tambahBarang.visibility = View.VISIBLE

        holder.tambahBarang.setOnClickListener {
            jumlah++

            setQty(holder.jumlahBarang, data[position].keranjang.id_keranjang, jumlah)
        }

        holder.kurangBarang.setOnClickListener {
            jumlah--

            if(jumlah.equals(0)){
                val id_keranjang = data[position].keranjang.id_keranjang
                AlertDialog.Builder(it.context).setMessage("Hapus "+ data[position].barang.nama_brg +" dari pesanan ?")
                    .setCancelable(false)
                    .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            FirebaseDatabase.getInstance().getReference("keranjang")
                                .child(id_keranjang)
                                .removeValue()
                                .addOnSuccessListener {
                                    dialog.cancel()
                                }
                        }
                    })
                    .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            jumlah++
                            dialog.cancel()
                        }
                    }).create().show()
            } else
                setQty(holder.jumlahBarang, data[position].keranjang.id_keranjang, jumlah)
        }
    }

    private fun setQty(edit: EditText, id: String, jumlah: Int){
        edit.setText(jumlah.toString())

        FirebaseDatabase.getInstance().getReference("keranjang")
            .child(id.toString())
            .child("qty")
            .setValue(jumlah)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}