package com.example.projectgols.view.admin.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.projectgols.R
import com.example.projectgols.view.ActivityLogin

class FragmentLaporan : Fragment() {
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var btnLogout: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_fragment_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout = requireActivity().findViewById(R.id.btnLogout)
        alertDialog = AlertDialog.Builder(requireActivity())
        SP = requireActivity().applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE)

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
}