package com.example.jejakrempah.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.jejakrempah.R
import com.example.jejakrempah.databinding.FragmentDetectionResultBinding

class DetectionResultFragment : Fragment() {

    private lateinit var rempahName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetectionResultBinding.inflate(inflater, container, false)

        // Mengambil argumen dari CameraFragment
        rempahName = arguments?.getString("rempahName") ?: "Unknown Spice"

        // Menampilkan nama rempah
        binding.rempahNameTextView.text = rempahName

        return binding.root
    }

    companion object {
        // Method untuk membuat instance dengan argumen
        fun newInstance(rempahName: String): DetectionResultFragment {
            val fragment = DetectionResultFragment()
            val bundle = Bundle()
            bundle.putString("rempahName", rempahName)
            fragment.arguments = bundle
            return fragment
        }
    }
}

