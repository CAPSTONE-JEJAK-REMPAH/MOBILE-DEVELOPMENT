package com.example.jejakrempah.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jejakrempah.R
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class NuSpiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nu_spice, container, false)

        val editTextPrompt = view.findViewById<EditText>(R.id.editTextPrompt)
        val textView = view.findViewById<TextView>(R.id.textView)
        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val userInput = editTextPrompt.text.toString()
            if (userInput.isNotBlank()) {
                buttonGeminiAPI(userInput, textView)
            } else {
                textView.text = "Please enter a prompt."
            }
        }

        return view
    }

    private fun buttonGeminiAPI(prompt: String, textView: TextView) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyCEwEvkOdAV-90U9rr3RPBUDpSXtj6AZmY"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                textView.text = "Generating content..."
                val response = generativeModel.generateContent(prompt)
                textView.text = response.text
            } catch (e: Exception) {
                textView.text = "Error: ${e.message}"
            }
        }
    }
}
