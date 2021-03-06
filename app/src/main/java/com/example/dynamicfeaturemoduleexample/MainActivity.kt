package com.example.dynamicfeaturemoduleexample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicfeaturemoduleexample.ActivityConstant.InstallTimeDelivery
import com.example.dynamicfeaturemoduleexample.ActivityConstant.InstantDelivery
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
                    println("Installed!")
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
                InstantDelivery.loadClassOrNull<AppCompatActivity>()?.let {
                    Intent(this@MainActivity, it)
                }?.also {
                    startActivity(it)
                }
            }

            // Only can be tested minimum on internal app sharing
            acbUninstallInstallTimeDelivery.setOnClickListener {
                if (splitInstallManager.installedModules.contains(resources.getString(R.string.install_time_delivery_module_name))) {
                    println("Uninstall module install time delivery")
                    splitInstallManager.deferredUninstall(listOf(resources.getString(R.string.install_time_delivery_module_name)))
                }
            }
            // Only can be tested minimum on internal app sharing
            acbUninstallOnDemandDelivery.setOnClickListener {
                if (splitInstallManager.installedModules.contains(resources.getString(R.string.on_demand_delivery_module_name))) {
                    println("Uninstall module on demand delivery")
                    splitInstallManager.deferredUninstall(listOf(resources.getString(R.string.on_demand_delivery_module_name)))
                }
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
        if (splitInstallManager.installedModules.contains(resources.getString(R.string.on_demand_delivery_module_name))) {
            println("Module is exist")
            goToOnDemandDeliveryPage()
        } else {
            println("Module doesn't exist")
            if (sessionId != 0) {
                downloadState = null
                splitInstallManager.cancelInstall(sessionId)
            }
            val request =
                SplitInstallRequest.newBuilder()
                    .addModule(resources.getString(R.string.on_demand_delivery_module_name))
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
            }.addOnSuccessListener { sessionIdModule ->
                sessionId = sessionIdModule
            }
        }
    }

    private fun goToOnDemandDeliveryPage() {
        OnDemandDelivery.loadClassOrNull<AppCompatActivity>()?.let {
            Intent(this@MainActivity, it)
        }?.also {
            startActivity(it)
        }
    }
}