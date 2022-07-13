package com.example.dynamicfeatureinstant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeatureinstant.databinding.ActivityInstantDeliveryBinding

/**
 * Created by Suyanwar on 13/07/22.
 */
class InstantDeliveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstantDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstantDeliveryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}