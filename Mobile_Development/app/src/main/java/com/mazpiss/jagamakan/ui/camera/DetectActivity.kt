package com.mazpiss.jagamakan.ui.camera

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.data.remote.response.DetectResponse
import com.mazpiss.jagamakan.data.remote.retorofit.ApiConfig
import com.mazpiss.jagamakan.databinding.ActivityDetectBinding
import com.mazpiss.jagamakan.utils.rotateBitmap
import java.io.File

class DetectActivity : AppCompatActivity() {

    private var openCamera: Boolean = true
    private lateinit var binding: ActivityDetectBinding
    private lateinit var viewModel: DetectViewModel
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetectViewModel::class.java)

        setupAction()

        viewModel.detectImage.observe(this) { file ->
            getFile = file
            if (getFile != null) {
                binding.imgShow.setImageBitmap(
                    rotateBitmap(
                        BitmapFactory.decodeFile(getFile?.path), openCamera
                    )
                )
            } else {
                binding.imgShow.setImageResource(R.drawable.ic_preview)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun setupAction() {
        binding.apply {
            buttonCamera.setOnClickListener { startCamera() }
            buttonX.setOnClickListener { deleteImage() }
            buttonDetect.setOnClickListener { detectAction() }
        }
    }

    private fun detectAction() {
        val apiService = ApiConfig.getApiService()
        val label = ""
        val value = 0
        showLoading(true)

        viewModel.detectImage(apiService, label, value).observe(this) { detectResponse ->
            if (detectResponse != null) {
                detectResult(detectResponse)
                showLoading(false)
                viewModel.clearDetect()
            } else {
                showLoading(false)

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Scan Ulang Makananmu")
                    .setPositiveButton("OK") { dialog, which ->
                    }.setCancelable(false).show()
            }

        }
    }

    private fun detectResult(detectResponse: DetectResponse) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_detect_result)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvNamaMakan = dialog.findViewById<TextView>(R.id.tvNamaMakanan)
        val tvKaloriMakanan = dialog.findViewById<TextView>(R.id.tvKaloriMakanan)
        val imgDetectResult = dialog.findViewById<ImageView>(R.id.imgDetectResult)
        val actionButton = dialog.findViewById<Button>(R.id.actionButton)

        tvNamaMakan.text = labelName(label = detectResponse.combinedName)
        tvKaloriMakanan.text = "${detectResponse.totalCalories} Kalori"

        Glide.with(applicationContext).load(detectResponse.imageUrl).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDetectResult)

        actionButton.setOnClickListener {
            dialog.dismiss()
        }

        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM or Gravity.CENTER
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams

        dialog.show()
    }

    private fun labelName(label: String): String {
        return when (label) {
            "nasi" -> getString(R.string.Nasi)
            "mangkuk_bakso" -> getString(R.string.Bakso)
            "tahu_goreng" -> getString(R.string.tahu)
            "tempe" -> getString(R.string.tempe)
            "telur_balado" -> getString(R.string.telurBalado)
            "nasi, mangkuk_bakso" -> getString(R.string.nasiMangkukBakso)
            "nasi, tahu_goreng" -> getString(R.string.nasiTahudanGoreng)
            "nasi, tempe" -> getString(R.string.nasiTempe)
            "nasi, telur_balado" -> getString(R.string.nasiTelurBalado)
            "mangkuk_bakso, tahu_goreng" -> getString(R.string.baksoTahudanGoreng)
            "mangkuk_bakso, tempe" -> getString(R.string.baksoTempe)
            "mangkuk_bakso, telur_balado" -> getString(R.string.baksoTelurBalado)
            "tahu_goreng, tempe" -> getString(R.string.tahuGorengTempe)
            "tahu_goreng, telur_balado" -> getString(R.string.tahuGorengTelurBalado)
            "tempe, telur_balado" -> getString(R.string.tempeTelurBalado)
            "nasi, mangkuk_bakso, tahu_goreng" -> getString(R.string.nasiMangkukBaksoTahudanGoreng)
            "nasi, mangkuk_bakso, tempe" -> getString(R.string.nasiMangkukBaksoTempe)
            "nasi, mangkuk_bakso, telur_balado" -> getString(R.string.nasiMangkukBaksoTelurBalado)
            "nasi, tahu_goreng, tempe" -> getString(R.string.nasiTahudanGorengTempe)
            "nasi, tahu_goreng, telur_balado" -> getString(R.string.nasiTahudanGorengTelurBalado)
            "nasi, tempe, telur_balado" -> getString(R.string.nasiTempeTelurBalado)
            "mangkuk_bakso, tahu_goreng, tempe" -> getString(R.string.baksoTahudanGorengTempe)
            "mangkuk_bakso, tahu_goreng, telur_balado" -> getString(R.string.baksoTahudanGorengTelurBalado)
            "mangkuk_bakso, tempe, telur_balado" -> getString(R.string.baksoTempeTelurBalado)
            "tahu_goreng, tempe, telur_balado" -> getString(R.string.tahuGorengTempeTelurBalado)
            else -> getString(R.string.nothing)
        }
    }

    private fun deleteImage() {
        if (getFile == null) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Tidak ada gambar").setPositiveButton("OK") { dialog, which ->
                }.setCancelable(false).show()
        } else {
            getFile = null
            viewModel.clearDetect()
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = it.data?.getSerializableExtra("picture") as File
                openCamera = it.data?.getBooleanExtra("backCamera", true) as Boolean

                viewModel.setScannedImage(myFile)
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS && !allPermissionsGranted()) {
            Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
