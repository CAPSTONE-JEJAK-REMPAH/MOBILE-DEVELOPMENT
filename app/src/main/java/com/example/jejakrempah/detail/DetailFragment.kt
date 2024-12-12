package com.example.jejakrempah.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.jejakrempah.R
import com.example.jejakrempah.data.JejakRempahItem
import com.example.jejakrempah.database.FavoriteEvent
import com.example.jejakrempah.databinding.FragmentDetailBinding
import com.example.jejakrempah.viewmodel.DetailViewModel
import com.example.jejakrempah.viewmodel.FavoriteViewModel
import androidx.navigation.fragment.findNavController

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels() // Menambahkan DetailViewModel
    private var isFavorite = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rempahId = DetailFragmentArgs.fromBundle(requireArguments()).rempahId

        // Memanggil fungsi fetchRempahDetail untuk mengambil data dari API
        detailViewModel.fetchRempahDetail(rempahId)

        // Mengamati perubahan data dari LiveData
        detailViewModel.rempahDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { rempahData ->
                val rempah = rempahData // Menyimpan data rempah yang berhasil diambil
                populateUI(rempah)

                binding.fabFavorite.setOnClickListener {
                    // Membuat objek favoriteEvent berdasarkan data yang sudah diambil
                    val favoriteEvent = FavoriteEvent( // Gunakan rempahId dari argumen
                        rempah = rempah.rempah,  // Gunakan nama rempah yang sebenarnya dari objek rempah
                        imageUrl = rempah.imageURLs.firstOrNull() ?: "",  // Ambil gambar pertama dari daftar URL
                        description = rempah.manfaat  // Gunakan deskripsi manfaat yang sebenarnya
                    )

                    if (isFavorite) {
                        favoriteViewModel.deleteFavoriteEvent(favoriteEvent)
                        isFavorite = false
                        binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    } else {
                        favoriteViewModel.addFavoriteEvent(favoriteEvent)
                        isFavorite = true
                        binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                        Toast.makeText(requireContext(), "Berhasil ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                    }

                    // Navigasi ke FavoriteFragment setelah menambah atau menghapus favorit
                    val action = DetailFragmentDirections.actionDetailFragmentToFavoriteFragment()
                    findNavController().navigate(action)
                }
            }.onFailure {
                // Tangani error jika data gagal didapat
                // Misalnya tampilkan pesan error ke pengguna
            }
        }


    }

    private fun populateUI(rempah: JejakRempahItem) {
        binding.rempahName.text = rempah.rempah
        binding.funFact1.text = "Fun Fact 1: ${rempah.funfact1}"
        binding.funFact2.text = "Fun Fact 2: ${rempah.funfact2}"
        binding.manfaat.text = "Manfaat: ${rempah.manfaat}"

        if (rempah.imageURLs.isNotEmpty()) {
            Glide.with(this)
                .load(rempah.imageURLs[0])
                .into(binding.rempahImage)
        }

        // Menangani klik pada tombol Learn More untuk membuka tautan di browser
        binding.learnMoreButton.setOnClickListener {
            // Membuka tautan yang terkait dengan rempah di browser
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(rempah.link)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
