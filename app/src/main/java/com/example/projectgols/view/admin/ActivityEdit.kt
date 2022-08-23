package com.example.projectgols.view.admin

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectgols.R
import com.example.projectgols.model.Barang
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ActivityEdit : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100
    private val REQUEST_PICK_IMAGE = 2
    var imgList = ArrayList<SlideModel>()
    var imgUri = ArrayList<Uri>()
    lateinit var imageSlider: ImageSlider
    lateinit var uploadImg: LinearLayout
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var namaMenu: EditText
    lateinit var deskripsiMenu: EditText
    lateinit var hargaMenu: EditText
    lateinit var stokMenu: EditText
    lateinit var tipeMenu: Spinner
    lateinit var btnSimpan: Button

    var oldData: Barang? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        checkCameraPermission()
        alertDialog = AlertDialog.Builder(this)

        imageSlider = findViewById(R.id.imgUpload)
        uploadImg = findViewById(R.id.uploadImg)
        namaMenu = findViewById(R.id.namaMenu)
        deskripsiMenu = findViewById(R.id.deskripsiMenu)
        hargaMenu = findViewById(R.id.hargaMenu)
        stokMenu = findViewById(R.id.stokMenu)
        tipeMenu = findViewById(R.id.tipeMenu)
        btnSimpan = findViewById(R.id.btnSimpan)

        val tipe = arrayListOf<String>("Celana Formal", "Celana Panjang", "Celana Pendek", "Kaos", "Kemeja", "Sweater")
        val adapterTipe = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipe)
        tipeMenu.adapter = adapterTipe

        if(intent.getStringExtra("id_barang").toString() != "")
            FirebaseDatabase.getInstance().reference.child("barang")
                .orderByKey()
                .equalTo(intent.getStringExtra("id_barang").toString())
                .get()
                .addOnSuccessListener {
                    for (i in it.children){
                        val value = i.getValue(Barang::class.java)
                        oldData = value!!

                        var type = 0
                        when (oldData!!.jenis) {
                            "Celana Formal" -> type = 0
                            "Celana Panjang"-> type = 1
                            "Celana Pendek"-> type = 2
                            "Kaos"-> type = 3
                            "Kemeja"-> type = 4
                            "Sweater"-> type = 5
                        }

                        tipeMenu.setSelection(type)
                        namaMenu.setText(value.nama_brg)
                        deskripsiMenu.setText(value.deskripsi)
                        hargaMenu.setText(value.harga.toString())
                        stokMenu.setText(value.qty_brg.toString())

                        value.img_brg.forEach { uri ->
                            imgList.add(SlideModel(uri))
                            imgUri.add(Uri.parse(uri))
                            setImageList()
                        }
                    }
                }

        uploadImg.setOnClickListener {
            openGallery()
        }

        btnSimpan.setOnClickListener {
            save()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.data
                imgList.add(SlideModel(uri.toString()))
                imgUri.add(uri!!)
                setImageList()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setImageList(){
        imageSlider.setImageList(imgList, ScaleTypes.CENTER_INSIDE)

        imageSlider.setItemClickListener( object : ItemClickListener{
            override fun onItemSelected(position: Int) {
                alertDialog.setMessage("Hapus gambar ?").setCancelable(false)
                    .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            imgList.removeAt(position)
                            setImageList()
                            dialog.cancel()
                        }
                    })
                    .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            dialog.cancel()
                        }
                    }).create().show()
            }
        })
    }

    private fun validate(): Boolean {
        if(namaMenu.text.toString() == "") {
            Toast.makeText(this, "Nama masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(deskripsiMenu.text.toString() == "") {
            Toast.makeText(this, "Deskripsi masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(stokMenu.text.toString() == "") {
            Toast.makeText(this, "Stok masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(hargaMenu.text.toString() == "") {
            Toast.makeText(this, "Harga masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(imgList.size == 0) {
            Toast.makeText(this, "Gambar masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun save(){
        if(validate())
            alertDialog.setMessage("Simpan barang ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        btnSimpan.text = "Menyimpan..."
                        btnSimpan.isClickable = false
                        btnSimpan.isEnabled = false
                        val urlBarang = arrayListOf<String>()
                        val tipe      = tipeMenu.selectedItem.toString()

                        var index = 0
                        imgUri.forEach {
                            if(it.toString().contains("https://firebasestorage.googleapis.com")) {
                                urlBarang.add(it.toString())
                                index++

                                toSave(index, urlBarang)
                            }
                            else {
                                val ref = FirebaseStorage.getInstance().reference
                                    .child("barang/" + tipe + "/" + UUID.randomUUID().toString())
                                ref.putFile(it)
                                    .continueWithTask { task ->
                                        if (!task.isSuccessful) {
                                            task.exception?.let {
                                                throw it
                                            }
                                        }
                                        ref.downloadUrl
                                    }
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            urlBarang.add(task.result.toString())
                                            index++

                                            toSave(index, urlBarang)
                                        }
                                    }
                            }
                        }
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
    }

    private fun toSave(index: Int, urlBarang: ArrayList<String>) {
        if(imgUri.size.equals(index)){
            if(oldData != null)
                FirebaseDatabase.getInstance().reference
                    .child("barang")
                    .child(oldData!!.id_brg.toString())
                    .setValue( Barang(
                        oldData!!.id_brg,
                        namaMenu.text.toString(),
                        deskripsiMenu.text.toString(),
                        tipeMenu.selectedItem.toString(),
                        stokMenu.text.toString().toInt(),
                        hargaMenu.text.toString().toInt(),
                        urlBarang
                    ))
                    .addOnCompleteListener {
                        val intent = Intent(this@ActivityEdit, ActivityUtama::class.java)
                        startActivity(intent)
                        finish()
                    }
            else
                FirebaseDatabase.getInstance().reference
                    .child("barang")
                    .orderByKey()
                    .limitToLast(1)
                    .get()
                    .addOnSuccessListener { row ->
                        for (i in row.children){
                            val value = i.getValue(Barang::class.java)

                            FirebaseDatabase.getInstance().reference
                                .child("barang")
                                .child((value!!.id_brg + 1).toString())
                                .setValue( Barang(
                                    (value.id_brg + 1),
                                    namaMenu.text.toString(),
                                    deskripsiMenu.text.toString(),
                                    tipeMenu.selectedItem.toString(),
                                    stokMenu.text.toString().toInt(),
                                    hargaMenu.text.toString().toInt(),
                                    urlBarang
                                ))
                                .addOnCompleteListener {
                                    val intent = Intent(this@ActivityEdit, ActivityUtama::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }
        }
    }

}