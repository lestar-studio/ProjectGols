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
import android.os.Debug
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projectgols.R
import com.example.projectgols.ml.Model
import com.example.projectgols.view.customer.ActivityKeranjang
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FragmentBeranda : Fragment() {
    lateinit var textcariBeranda: TextView
    lateinit var pictureBeranda: ImageView
    lateinit var keranjangBeranda: ImageView
    lateinit var tempImage: ImageView

    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2

    var imageSize = 32

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun classifyImage(image: Bitmap) {
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

            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }
}