package com.example.dynamicfeaturemoduleexample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeaturemoduleexample.ActivityConstant.InstallTimeDelivery
import com.example.dynamicfeaturemoduleexample.ActivityConstant.OnDemandDelivery
import com.example.dynamicfeaturemoduleexample.databinding.ActivityMainBinding
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var splitInstallManager: SplitInstallManager

    private var sessionId: Int = 0
    private var downloadState: Int? = null
    @SuppressLint("SwitchIntDef")
    private val listener = SplitInstallStateUpdatedListener { splitInstallSessionState ->
        downloadState = splitInstallSessionState.status()
        if (splitInstallSessionState.status() == SplitInstallSessionStatus.FAILED) {
            println(splitInstallSessionState.errorCode())
            return@SplitInstallStateUpdatedListener
        }
        if (splitInstallSessionState.sessionId() == sessionId) {
            when (splitInstallSessionState.status()) {
                SplitInstallSessionStatus.INSTALLED -> {
                    if (VERSION.SDK_INT >= VERSION_CODES.O) {
                        SplitInstallHelper.updateAppInfo(this@MainActivity)
                    }
                    goToOnDemandDeliveryPage()
                }
                SplitInstallSessionStatus.DOWNLOADING -> {
                    println("Downloading!")
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    println("Installing!")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        splitInstallManager = SplitInstallManagerFactory.create(this@MainActivity)

        binding.run {
            acbInstallTimeDelivery.setOnClickListener {
                InstallTimeDelivery.loadClassOrNull<AppCompatActivity>()?.let {
                    Intent(this@MainActivity, it)
                }?.also {
                    startActivity(it)
                }
            }
            acbOnDemandDelivery.setOnClickListener {
                downloadOnDemandDeliveryModule()
            }
            acbConditionalDelivery.setOnClickListener {
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
            }
            acbInstantDelivery.setOnClickListener {
                Toast.makeText(this@MainActivity, "Soon to be develop", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        try {
            splitInstallManager.registerListener(listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onResume()
    }

    override fun onPause() {
        try {
            splitInstallManager.unregisterListener(listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onPause()
    }

    @SuppressLint("SwitchIntDef")
    private fun downloadOnDemandDeliveryModule() {
        println("Masuk sini")
        if (splitInstallManager.installedModules.contains(resources.getString(R.string.on_demand_delivery_module_name))) {
            goToOnDemandDeliveryPage()
            println("Masuk sini 1")
        } else {
            println("Masuk sini 2")
            if (sessionId != 0) {
                println("Masuk sini 3")
                downloadState = null
                splitInstallManager.cancelInstall(sessionId)
            }
            println("Masuk sini 4")
            val request =
                SplitInstallRequest.newBuilder().addModule(resources.getString(R.string.on_demand_delivery_module_name))
                    .build()
            splitInstallManager.startInstall(request).addOnFailureListener { exception ->
                when ((exception as SplitInstallException).errorCode) {
                    SplitInstallErrorCode.NETWORK_ERROR -> {
                        println(exception.errorCode)
                    }
                    SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {
                        println(exception.errorCode)
                        splitInstallManager.sessionStates.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Check for active sessions.
                                for (state in task.result!!) {
                                    if (state.status() == SplitInstallSessionStatus.DOWNLOADING) {
                                        splitInstallManager.cancelInstall(state.sessionId())
                                    }
                                }
                            }
                        }
                    }
                }
                println(exception.errorCode)
                println("Masuk sini 5")
            }.addOnSuccessListener { sessionIdModule ->
                println("Masuk sini 6")
                sessionId = sessionIdModule
            }
        }
    }

    private fun goToOnDemandDeliveryPage() {
        println("Masuk sini 7")
        OnDemandDelivery.loadClassOrNull<AppCompatActivity>()?.let {
            println("Masuk sini 8")
            Intent(this@MainActivity, it)
        }?.also {
            println("Masuk sini 9")
            startActivity(it)
        }
    }
}