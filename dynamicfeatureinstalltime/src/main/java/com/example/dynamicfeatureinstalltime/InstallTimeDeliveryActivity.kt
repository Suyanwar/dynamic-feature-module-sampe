package com.example.dynamicfeatureinstalltime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeatureinstalltime.databinding.ActivityInstallTimeDeliveryBinding

/**
 * Created by Suyanwar on 13/07/22.
 */
class InstallTimeDeliveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstallTimeDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstallTimeDeliveryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}