package com.example.projectgols.view.admin.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.example.projectgols.model.Pesanan
import com.example.projectgols.view.ActivityLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat

class FragmentLaporan : Fragment() {
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var btnLogout: ImageView

    lateinit var stokBarangLaporan: TextView
    lateinit var terjualLaporan: TextView
    lateinit var pendapatanLaporan: TextView

    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_fragment_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout = requireActivity().findViewById(R.id.btnLogout)
        alertDialog = AlertDialog.Builder(requireActivity())
        SP = requireActivity().applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE)

        stokBarangLaporan = requireActivity().findViewById(R.id.stokBarangLaporan)
        terjualLaporan    = requireActivity().findViewById(R.id.terjualLaporan)
        pendapatanLaporan = requireActivity().findViewById(R.id.pendapatanLaporan)

        load()

        btnLogout.setOnClickListener {
            alertDialog.setTitle("Keluar Akun")
            alertDialog.setMessage("Apakah anda ingin keluar dari akun ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val editor = SP.edit()
                        editor.putString("id_user", "")
                        editor.putString("nama", "")
                        editor.putString("email", "")
                        editor.putString("password", "")
                        editor.putString("alamat", "")
                        editor.putString("telp", "")
                        editor.putString("level", "")
                        editor.apply()

                        val intent = Intent(context, ActivityLogin::class.java)
                        startActivity(intent)
                        activity!!.finish()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun load() {
        FirebaseDatabase.getInstance().reference.child("barang")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var stok = 0
                    for (i in snapshot.children){
                        val value = i.getValue(Barang::class.java)

                        stok += value!!.qty_brg
                    }

                    stokBarangLaporan.text = stok.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        FirebaseDatabase.getInstance().getReference("pesanan")
            .orderByChild("status")
            .equalTo("selesai")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var jumlah = 0
                    var pendapatan = 0
                    for (i in snapshot.children){
                        val value = i.getValue(Pesanan::class.java)

                        value!!.detail.forEach { row ->
                            jumlah += row.qty
                        }

                        pendapatan += value.total
                    }

                    terjualLaporan.text = jumlah.toString()
                    pendapatanLaporan.text = "Rp. " + formatNumber.format(pendapatan) + ",00"
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}