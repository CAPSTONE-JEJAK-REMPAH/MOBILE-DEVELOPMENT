package com.example.jejakrempah

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jejakrempah.Authentication.SignInActivity
import com.example.jejakrempah.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Menampilkan email pengguna dari Firebase
        val userEmail = firebaseAuth.currentUser?.email
        binding.emailEt.setText(userEmail ?: "Email tidak tersedia")

        // Load gambar profil jika ada
        val imagePath = loadImagePath()
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.profileImageView.setImageBitmap(bitmap)
        } else {
            binding.profileImageView.setImageResource(R.drawable.baseline_photo_camera_24) // Gambar default
        }

        // Tombol untuk mengedit foto profil
        binding.editProfileButton.setOnClickListener {
            val options = arrayOf("Pilih dari Galeri", "Ambil Foto")
            val builder = AlertDialog.Builder(requireContext())
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery() // Pilih dari galeri
                    1 -> openCamera()  // Ambil foto
                }
            }
            builder.show()
        }

        // Tombol Logout
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                val imageUri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                binding.profileImageView.setImageBitmap(bitmap)

                // Simpan gambar di penyimpanan internal
                val path = saveImageToInternalStorage(bitmap)
                saveImagePath(path)
                Toast.makeText(requireContext(), "Foto Profil Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
            } else if (requestCode == CAMERA_REQUEST) {
                val photo: Bitmap = data?.extras?.get("data") as Bitmap
                binding.profileImageView.setImageBitmap(photo)

                // Simpan gambar di penyimpanan internal
                val path = saveImageToInternalStorage(photo)
                saveImagePath(path)
                Toast.makeText(requireContext(), "Foto Profil Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val fileName = "profile_image.jpg"
        val file = File(requireContext().filesDir, fileName)
        try {
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun saveImagePath(path: String) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Activity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profile_image_path", path)
        editor.apply()
    }

    private fun loadImagePath(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Activity.MODE_PRIVATE)
        return sharedPreferences.getString("profile_image_path", null)
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                firebaseAuth.signOut() // Logout dari Firebase
                Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()

                // Arahkan pengguna ke halaman login
                val intent = Intent(requireContext(), SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
