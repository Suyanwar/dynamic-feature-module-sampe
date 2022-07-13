package com.example.dynamicfeaturemoduleexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
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