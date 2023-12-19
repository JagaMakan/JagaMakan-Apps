package com.mazpiss.jagamakan.ui.nav.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.adapter.ListArtikelAdapter
import com.mazpiss.jagamakan.data.local.Artikel
import com.mazpiss.jagamakan.data.local.Calculator
import com.mazpiss.jagamakan.databinding.FragmentHomeBinding
import com.mazpiss.jagamakan.ui.calculator.CalculatorActivity
import com.mazpiss.jagamakan.ui.welcome.WelcomeActivity
import com.mazpiss.jagamakan.utils.UserPreferences

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var mUserPreference: UserPreferences
    private lateinit var cekKebutuhanTextView: TextView
    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var rvArticle: RecyclerView
    private val list = ArrayList<Artikel>()
    private var isPreferenceEmpty = false
    private lateinit var calculator: Calculator
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.data != null && result.resultCode == CalculatorActivity.RESULT_CODE) {
            calculator =
                result.data?.getParcelableExtra<Calculator>(CalculatorActivity.EXTRA_RESULT) as Calculator
            populateView(calculator)
            cekKebutuhanTextView.text = calculator.globalBMR.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        rvArticle = binding.rvArticle
        rvArticle.setHasFixedSize(true)

        list.addAll(getListArticle())
        showArticel()
        return binding.root
    }

    private fun showArticel() {
        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        val listArticle = ListArtikelAdapter(list)
        rvArticle.adapter = listArticle
    }

    private fun getListArticle(): Collection<Artikel> {
        val photo = resources.obtainTypedArray(R.array.img)
        val dataName = resources.getStringArray(R.array.data_judul)
        val dataDescription = resources.getStringArray(R.array.data_pengertian)

        val listArticle = ArrayList<Artikel>()
        val limit = minOf(dataName.size, 3)

        for (i in 0 until limit) {
            val article = Artikel(photo.getResourceId(i, 0), dataName[i], dataDescription[i])
            listArticle.add(article)
        }

        return listArticle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.toolbar.logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        binding.btnCek.setOnClickListener(this)
        mUserPreference = UserPreferences(requireContext())
        cekKebutuhanTextView = view.findViewById(R.id.cekKebutuhan)
        showExistingPreference()
        cekKebutuhanTextView.text = calculator.globalBMR.toString()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCek -> {
                val intent = Intent(requireContext(), CalculatorActivity::class.java)
                when {
                    isPreferenceEmpty -> {
                        intent.putExtra(
                            CalculatorActivity.EXTRA_TYPE_FORM,
                            CalculatorActivity.TYPE_ADD
                        )
                        intent.putExtra("CALCULATE", calculator)
                    }

                    else -> {
                        intent.putExtra(
                            CalculatorActivity.EXTRA_TYPE_FORM,
                            CalculatorActivity.TYPE_EDIT
                        )
                        intent.putExtra("CALCULATE", calculator)
                    }
                }
                resultLauncher.launch(intent)
            }

            R.id.logout -> {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showExistingPreference() {
        calculator = mUserPreference.getUser()
        calculator.globalBMR = mUserPreference.getGlobalBMR()
        populateView(calculator)
        cekKebutuhanTextView.text = "${calculator.globalBMR}/Kal"
    }

    private fun populateView(calculator: Calculator) {
        binding.beratBadan.text = calculator.weight.toString()
        binding.tinggiBadan.text = calculator.height.toString()
        binding.umur.text = calculator.age.toString()
        binding.cekKebutuhan.text = calculator.calory.toString()
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                mUserPreference.clearUser()
                mUserPreference.clearGlobalBMR()
                mUserPreference.clearActivity()
                firebaseAuth.signOut()
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No") { _, _ ->
            }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
            }
    }
}
