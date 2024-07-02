package com.mobile.desinuas

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobile.desinuas.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton(binding.btnDataset, DatasetActivity::class.java)
        setupButton(binding.btnFeature, FiturActivity::class.java)
        setupButton(binding.btnModel, ModelActivity::class.java)
        setupButton(binding.btnSimulation, SimActivity::class.java)
        setupButton(binding.btnAbt, TentangActivity::class.java)
        setupButton(binding.btnHome, MainActivity::class.java)
    }

    private fun setupButton(button: View, activityClass: Class<*>) {
        button.setOnClickListener {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }
}
