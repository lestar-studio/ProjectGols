package com.example.projectgols.view.customer.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projectgols.R
import com.example.projectgols.view.customer.ActivityKeranjang

class FragmentBeranda : Fragment() {
    lateinit var textcariBeranda: TextView
    lateinit var pictureBeranda: ImageView
    lateinit var keranjangBeranda: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textcariBeranda = requireActivity().findViewById(R.id.textcariBeranda)
        pictureBeranda = requireActivity().findViewById(R.id.pictureBeranda)
        keranjangBeranda = requireActivity().findViewById(R.id.keranjangBeranda)

        pictureBeranda.setOnClickListener {  }
        keranjangBeranda.setOnClickListener {
            val intent = Intent(view.context, ActivityKeranjang::class.java)
            startActivity(intent)
        }
    }
}