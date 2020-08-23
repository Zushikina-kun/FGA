package com.mathewsachin.fategrandautomata.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.mathewsachin.fategrandautomata.R
import com.mathewsachin.fategrandautomata.accessibility.ScriptRunnerService
import com.mathewsachin.fategrandautomata.accessibility.ServiceState
import kotlinx.android.synthetic.main.content_main.*

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.content_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service_toggle_btn.setOnClickListener { serviceToggleBtnOnClick() }
    }

    val startMediaProjection = registerForActivityResult(StartMediaProjection()) { intent ->
        if (intent == null) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            ScriptRunnerService.Instance?.notification?.hide()
        } else ScriptRunnerService.Instance?.start(intent)
    }

    private fun checkAccessibilityService(): Boolean {
        if (ScriptRunnerService.Instance != null)
            return true

        AlertDialog.Builder(requireContext())
            .setTitle("Accessibility Disabled")
            .setMessage("Turn on accessibility for this app from System settings. If it is already On, turn it OFF and start again.")
            .setPositiveButton("Go To Settings") { _, _ ->
                // Open Accessibility Settings
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                toggling = true
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()

        return false
    }

    private fun checkCanUseOverlays(): Boolean {
        val context = requireContext()

        if (!Settings.canDrawOverlays(context)) {
            val i = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            toggling = true
            startActivity(i)
            return false
        }

        return true
    }

    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.values.all { m -> m }) {
            serviceToggleBtnOnClick()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionsToCheck = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissionsToCheck
            .filter {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PermissionChecker.PERMISSION_GRANTED
            }
            .toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermission.launch(permissionsToRequest)

            return false
        }

        return true
    }

    private var toggling = false

    private fun serviceToggleBtnOnClick() {
        toggling = false

        if (!checkCanUseOverlays()
            || !checkPermissions()
            || !checkAccessibilityService()
        )
            return

        val instance = ScriptRunnerService.Instance
            ?: return

        when (instance.serviceState) {
            is ServiceState.Started -> instance.stop()
            is ServiceState.Stopped -> {
                if (instance.wantsMediaProjectionToken) {
                    instance.notification.show()

                    // This initiates a prompt dialog for the user to confirm screen projection.
                    startMediaProjection.launch()
                } else if (instance.start()) {
                    instance.notification.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (toggling) {
            serviceToggleBtnOnClick()
        }
    }
}
