package com.example.projectgols.view.customer.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectgols.R
import com.example.projectgols.view.customer.ActivityKeranjang

class FragmentBeranda : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setHasOptionsMenu(true)
//        val actionBar = requireActivity().findViewById(R.id.toolbarBeranda) as Toolbar
//        (activity as AppCompatActivity).setSupportActionBar(actionBar)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//        val inflater = requireActivity().menuInflater
//        inflater.inflate(R.menu.bar_beranda, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.keranjang) {
//            val intent = Intent(activity, ActivityKeranjang::class.java)
//            startActivity(intent)
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}