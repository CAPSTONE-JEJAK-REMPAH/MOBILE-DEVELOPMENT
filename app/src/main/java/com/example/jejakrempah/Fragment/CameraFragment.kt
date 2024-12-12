package com.example.jejakrempah.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jejakrempah.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CameraFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var scanButton: Button
    private lateinit var detectButton: Button
    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        // Inisialisasi UI
        imageView = view.findViewById(R.id.imageView)
        uploadButton = view.findViewById(R.id.uploadButton)
        scanButton = view.findViewById(R.id.scanButton)
        detectButton = view.findViewById(R.id.detectButton)

        // Aksi tombol upload gambar dari galeri
        uploadButton.setOnClickListener { openGallery() }

        // Aksi tombol scan menggunakan kamera
        scanButton.setOnClickListener { openCamera() }

        // Aksi tombol deteksi rempah
        detectButton.setOnClickListener { detectSpices() }

        return view
    }

    private fun detectSpices() {
        if (selectedBitmap == null) {
            Toast.makeText(requireContext(), "Silakan unggah atau pindai gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        // Tampilkan ProgressBar
        val progressBar = view?.findViewById<ProgressBar >(R.id.progressBar)
        progressBar?.visibility = View.VISIBLE

        // Konversi Bitmap ke file sementara
        val imageFile = bitmapToFile(selectedBitmap!!)

        val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

        val client = OkHttpClient.Builder().build()

        val request = okhttp3.Request.Builder()
            .url("https://jejak-rempah-api-gjmefcu64q-et.a.run.app/api/scan")
            .post(
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(imagePart)
                    .build()
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseString = response.body?.string()
                Log.d("DetectSpices", "Response: $responseString")

                // Sembunyikan ProgressBar di UI thread
                requireActivity().runOnUiThread {
                    progressBar?.visibility = View.GONE
                }

                if (response.isSuccessful) {
                    try {
                        val jsonResponse = JSONObject(responseString ?: "{}")
                        val rempahName = jsonResponse.getJSONObject("data").getString("rempah_name")

                        requireActivity().runOnUiThread {
                            if (rempahName == "Bukan Rempah") {
                                Toast.makeText(
                                    requireContext(),
                                    "Gambar itu bukan Rempah",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val action = CameraFragmentDirections.actionCameraFragmentToDetectionResultFragment(rempahName)
                                findNavController().navigate(action)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("DetectSpices", "JSON Parsing Error: ${e.message}")
                    }
                } else {
                    Log.e("DetectSpices", "Failed: $responseString, Code: ${response.code}")
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Failed to detect spice", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("DetectSpices", "Error: ${e.message}")
                requireActivity().runOnUiThread {
                    progressBar?.visibility = View.GONE // Sembunyikan ProgressBar
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }




    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, "temp_image.jpg")
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()
        FileOutputStream(file).apply {
            write(bitmapData)
            flush()
            close()
        }
        return file
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->
                    selectedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    }
                    imageView.setImageBitmap(selectedBitmap)
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo: Bitmap? = result.data?.extras?.get("data") as Bitmap?
                photo?.let {
                    selectedBitmap = it
                    imageView.setImageBitmap(it)
                }
            }
        }
}
