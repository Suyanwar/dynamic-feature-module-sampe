package com.example.dynamicfeatureondemand

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeatureondemand.databinding.ActivityOnDemandDeliveryBinding
import com.google.android.play.core.splitcompat.SplitCompat

/**
 * Created by Suyanwar on 13/07/22.
 */
class OnDemandDeliveryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOnDemandDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnDemandDeliveryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}