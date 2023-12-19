package com.mazpiss.jagamakan.ui.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mazpiss.jagamakan.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var image = 0

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
            description = it.getString(DESC)
            image = it.getInt(IMG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        with(binding) {
            textOnboardingTitle.text = title
            textOnboardingDescription.text = description
            imageOnboarding.setImageResource(image)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TITLE = "title"
        private const val DESC = "desc"
        private const val IMG = "img"

        fun newInstance(
            title: String?,
            description: String?,
            imageResource: Int
        ): OnBoardingFragment = OnBoardingFragment().apply {
            arguments = Bundle().apply {
                putString(TITLE, title)
                putString(DESC, description)
                putInt(IMG, imageResource)
            }
        }
    }
}
