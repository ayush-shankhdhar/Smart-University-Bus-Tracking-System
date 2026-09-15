package com.example.bustracking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityChatBoxBinding

class ChatBox : AppCompatActivity() {
    private lateinit var binding: ActivityChatBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Chat Box"

        binding.btnSendChat.setOnClickListener {
            val msg = binding.etChatMessage.text.toString().trim()
            if (msg.isNotEmpty()) {
                Toast.makeText(this, "Message sent to transit dispatch", Toast.LENGTH_SHORT).show()
                binding.etChatMessage.setText("")
            }
        }
    }
}