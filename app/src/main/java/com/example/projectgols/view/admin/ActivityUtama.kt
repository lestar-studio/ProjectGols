package com.example.projectgols.view.admin

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.projectgols.R
import com.example.projectgols.view.admin.fragment.FragmentBeranda
import com.example.projectgols.view.admin.fragment.FragmentLaporan
import com.example.projectgols.view.admin.fragment.FragmentPesanan
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityUtama : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var bottomNav: BottomNavigationView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.beranda -> {
                replaceFragment(FragmentBeranda())
                return@OnNavigationItemSelectedListener true
            }
            R.id.pesanan -> {
                replaceFragment(FragmentPesanan())
                return@OnNavigationItemSelectedListener true
            }
            R.id.laporan -> {
                replaceFragment(FragmentLaporan())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_utama)

        bottomNav = findViewById(R.id.bottomNav)
        alertDialog = AlertDialog.Builder(this)
        replaceFragment(FragmentBeranda())
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer, fragment)
        fragmentTransition.commit()
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?")
            .setCancelable(false)
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}