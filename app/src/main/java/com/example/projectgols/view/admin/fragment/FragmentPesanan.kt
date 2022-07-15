package com.example.projectgols.view.admin.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.projectgols.R

class FragmentPesanan : Fragment() {
    lateinit var btnMenunggu: Button
    lateinit var btnDiproses: Button
    lateinit var btnDibatalkan: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_fragment_pesanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnMenunggu = requireActivity().findViewById(R.id.btnMenunggu)
        btnDiproses = requireActivity().findViewById(R.id.btnDiproses)
        btnDibatalkan = requireActivity().findViewById(R.id.btnDibatalkan)

        loadData()
    }

    private fun loadData() {
        btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

        btnMenunggu.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        }
        btnDiproses.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        }
        btnDibatalkan.setOnClickListener {
            btnMenunggu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDiproses.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
            btnDibatalkan.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        }
    }
}