package com.example.dynamicfeaturemoduleexample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeaturemoduleexample.ActivityConstant.InstallTimeDelivery
import com.example.dynamicfeaturemoduleexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.run {
            acbInstallTimeDelivery.setOnClickListener {
                InstallTimeDelivery.loadClassOrNull<AppCompatActivity>()?.let {
                    Intent(this@MainActivity, it)
                }?.also {
                    startActivity(it)
                }
            }
            acbOnDemandDelivery.setOnClickListener {
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
            }
            acbConditionalDelivery.setOnClickListener {
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
            }
            acbInstantDelivery.setOnClickListener {
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
            }
        }
    }
}