package com.example.projectgols.view.customer.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.RestrictionsManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectgols.R
import com.example.projectgols.ml.Model
import com.example.projectgols.model.Barang
import com.example.projectgols.view.adapter.ViewholderBeranda
import com.example.projectgols.view.customer.ActivityDetail
import com.example.projectgols.view.customer.ActivityKeranjang
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.io.File

class FragmentBeranda : Fragment() {
    lateinit var textcariBeranda: EditText
    lateinit var pictureBeranda: ImageView
    lateinit var keranjangBeranda: ImageView
    lateinit var tempImage: ImageView

    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2

    var imageSize = 32

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    lateinit var dataBarang: ArrayList<Barang>
    lateinit var adapterBarang: ViewholderBeranda

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayoutManager = LinearLayoutManager(requireActivity())
        mRecyclerView = requireView().findViewById(R.id.recyclerBeranda)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
        dataBarang = arrayListOf()
        adapterBarang = ViewholderBeranda(dataBarang)

        load("", 0)

        textcariBeranda = requireActivity().findViewById(R.id.textcariBeranda)
        pictureBeranda = requireActivity().findViewById(R.id.pictureBeranda)
        keranjangBeranda = requireActivity().findViewById(R.id.keranjangBeranda)
        tempImage = requireActivity().findViewById(R.id.tempImage)

        pictureBeranda.setOnClickListener {
            var dialog: AlertDialog? = null
            val builder = AlertDialog.Builder(context)
            val views = layoutInflater.inflate(R.layout.camera_open, null)

            val btn_camera = views.findViewById<ImageView>(R.id.camera)
            val btn_gallery = views.findViewById<ImageView>(R.id.gallery)

            btn_camera.setOnClickListener{
                openCamera()
                dialog?.dismiss()
            }

            btn_gallery.setOnClickListener{
                openGallery()
                dialog?.dismiss()
            }

            builder.setView(views)
            dialog = builder.create()
            dialog.show()
        }
        keranjangBeranda.setOnClickListener {
            val intent = Intent(view.context, ActivityKeranjang::class.java)
            startActivity(intent)

        }

        textcariBeranda.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                load(p0.toString(), 0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun load(search: String, type: Int){
        dataBarang.clear()
        mRecyclerView.invalidate()
        FirebaseDatabase.getInstance().getReference("barang").get().addOnSuccessListener {
            for (vl in it.children){
                val data = vl.getValue(Barang::class.java)

                if(type.equals(0)){
                    if(search.equals(""))
                        dataBarang.add(data!!)
                    else{
                        if(data!!.nama_brg.toLowerCase().contains(search))
                            dataBarang.add(data)
                    }
                } else {
                    if(data!!.jenis.equals(search))
                        dataBarang.add(data)
                }
            }

            adapterBarang = ViewholderBeranda(dataBarang.distinctBy { it.nama_brg } as ArrayList<Barang>)
            mRecyclerView.adapter = adapterBarang
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "Title", null)
                val hasilConvert = Uri.parse(path)
                tempImage.setImageURI(hasilConvert)
            }
            else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.data
                tempImage.setImageURI(uri)
            }

            var bmp = (tempImage.drawable as BitmapDrawable).bitmap
            bmp = Bitmap.createScaledBitmap(bmp!!, imageSize, imageSize, false)
            Log.d("imageBitmap", bmp.toString())
            classifyImage(bmp!!)
        }
        else if (resultCode == RestrictionsManager.RESULT_ERROR) {
            Toast.makeText(context, "Ambil Gambar Error", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Ambil Gambar Batal", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun classifyImageOld(image: Bitmap) {
        try {
            val model: Model = Model.newInstance(requireContext().applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Celana Formal", "Celana Panjang", "Celana Pendek", "Kaos", "Kemeja", "Sweater")
            Log.d("clasifikasi", classes[maxPos])
            load(classes[maxPos], 1)

            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            return outputStream.toByteArray()
        } catch (e: Exception) {
            return ByteArray(1)
        }
    }

    // Fungsi untuk mengirim gambar ke server
    fun classifyImage(bitmap: Bitmap) {
        val file = File(requireContext().cacheDir, "temp_file.jpg") // Ganti ekstensi file sesuai format gambar yang Anda dapatkan
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        file.writeBytes(outputStream.toByteArray())

        Fuel.upload("http://192.168.18.14:5001/predict", Method.POST)
            .add(FileDataPart(file, name = "file"))
            .response { result ->
                Log.d("testT", result.toString())
            }
    }
}