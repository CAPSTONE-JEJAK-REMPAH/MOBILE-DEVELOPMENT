package com.example.jejakrempah.Authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jejakrempah.MainActivity
import com.example.jejakrempah.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Membuat teks "Daftar!" menjadi tebal dan clickable
        val spannableText = SpannableString("Belum Punya Akun? Daftar!")
        val boldSpan = StyleSpan(Typeface.BOLD)
        val colorSpan = ForegroundColorSpan(Color.BLACK)

        // Indeks teks "Daftar!"
        val startIndex = spannableText.indexOf("Daftar!")
        val endIndex = startIndex + "Daftar!".length

        // Membuat "Daftar!" tebal
        spannableText.setSpan(boldSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Menambahkan klik pada "Daftar!"
        spannableText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
                finish() // Menutup SignInActivity agar tidak kembali ke layar ini
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Terapkan spannable ke TextView
        binding.textView.text = spannableText
        binding.textView.movementMethod = LinkMovementMethod.getInstance()

        // Tombol Login
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userEmail = firebaseAuth.currentUser?.email
                        Toast.makeText(this, "Login Berhasil! Selamat datang, $userEmail", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("USER_EMAIL", userEmail)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
