package com.example.projectgols.view.admin.fragment

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.model.Pesanan
import com.example.projectgols.view.adapter.ViewholderPesanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentPesanan : Fragment() {
    lateinit var btnMenunggu: Button
    lateinit var btnDiproses: Button
    lateinit var btnDibatalkan: Button
    lateinit var btnSelesai: Button

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    lateinit var dataPesanan: ArrayList<Pesanan>
    lateinit var adapterPesanan: ViewholderPesanan

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_fragment_pesanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnMenunggu = requireActivity().findViewById(R.id.btnMenunggu)
        btnDiproses = requireActivity().findViewById(R.id.btnDiproses)
        btnDibatalkan = requireActivity().findViewById(R.id.btnDibatalkan)
        btnSelesai = requireActivity().findViewById(R.id.btnSelesai)

        mLayoutManager = LinearLayoutManager(requireActivity())
        mRecyclerView = requireView().findViewById(R.id.recyclerPesanan)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
        dataPesanan = arrayListOf()
        adapterPesanan = ViewholderPesanan(dataPesanan)

        loadData()
    }

    private fun loadData() {
        btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        btnSelesai.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        load("menunggu")

        btnMenunggu.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnSelesai.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

            load("menunggu")
        }
        btnDiproses.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnSelesai.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

            load("proses")
        }
        btnDibatalkan.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btnSelesai.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

            load("batal")
        }
        btnSelesai.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnSelesai.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))

            load("selesai")
        }
    }

    private fun load(status: String) {
        dataPesanan.clear()
        mRecyclerView.invalidate()
        FirebaseDatabase.getInstance().getReference("pesanan")
            .orderByChild("status")
            .equalTo(status)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (vl in snapshot.children){
                        val data = vl.getValue(Pesanan::class.java)

                        dataPesanan.add(data!!)
                    }

                    adapterPesanan = ViewholderPesanan(dataPesanan)
                    mRecyclerView.adapter = adapterPesanan
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}