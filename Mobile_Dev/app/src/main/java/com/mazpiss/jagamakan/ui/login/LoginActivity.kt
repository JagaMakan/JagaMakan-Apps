package com.mazpiss.jagamakan.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.mazpiss.jagamakan.ui.main.MainActivity
import com.mazpiss.jagamakan.databinding.ActivityLoginBinding
import com.mazpiss.jagamakan.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {
            if (isNetworkAvailable()) {
                val email = binding.emailEt.text.toString()
                val pass = binding.passET.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Show success dialog
                            showSuccessDialog("Kamu berhasil login!")
                        } else {
                            // Check if it's an authentication issue
                            if (task.exception is FirebaseAuthInvalidUserException || task.exception is FirebaseAuthInvalidCredentialsException) {
                                // Show error dialog for authentication issues
                                showErrorDialog("Invalid email or password")
                            } else {
                                // Show a generic error dialog
                                showErrorDialog(task.exception?.message ?: "Authentication failed")
                            }
                        }
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
                // Go to MainActivity
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
