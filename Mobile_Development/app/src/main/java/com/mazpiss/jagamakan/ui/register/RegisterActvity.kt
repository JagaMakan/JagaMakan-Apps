package com.mazpiss.jagamakan.ui.register

import android.content.Intent
import android.os.Bundle
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.mazpiss.jagamakan.ui.main.MainActivity
import com.mazpiss.jagamakan.databinding.ActivityRegisterBinding
import com.mazpiss.jagamakan.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {
            if (isNetworkAvailable()) {
                val email = binding.emailEt.text.toString()
                val pass = binding.passET.text.toString()
                val confirmPass = binding.confirmPassEt.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                    if (pass == confirmPass) {
                        if (pass.length > 7) {
                            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showSuccessDialog("Kamu berhasil daftar! \nsilahkan masuk")
                                    } else {
                                        if (task.exception is FirebaseAuthWeakPasswordException) {
                                            showErrorDialog("Kata sandi terlalu lemah. Silakan pilih kata sandi yang lebih kuat.")
                                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                            showErrorDialog("Format Email Salah!")
                                        } else if (task.exception is FirebaseAuthUserCollisionException) {
                                            showErrorDialog("Akun ini sudah terdaftar!")
                                        } else {
                                            showErrorDialog(task.exception?.message ?: "Registrasi Gagal")
                                        }
                                    }
                                }
                        } else {
                            showErrorDialog("Password must be at least 8 characters long.")
                        }
                    } else {
                        showErrorDialog("Password is not matching")
                    }
                } else {
                    showErrorDialog("Empty Fields Are not Allowed !!")
                }
            } else {
                showNoInternetDialog()
            }
        }
    }

    private fun showErrorDialog(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setCancelable(true)
            .show()
    }

    private fun showSuccessDialog(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun showNoInternetDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("No Internet Connection")
            .setMessage("Ups, sepertinya koneksi internetmu terputus. Silakan cek kembali.")
            .setPositiveButton("OK", null)
            .setCancelable(true)
            .show()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
