package com.example.businessapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Paint
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentOnboardingBinding
class OnboardingFragment : Fragment(R.layout.fragment_onboarding), OnboardingAdapter.OnboardingClickListener {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OnboardingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOnboardingBinding.bind(view)

        val images = listOf(
            R.drawable.onboard1,
            R.drawable.onboard2,
            R.drawable.onboard3
        )

        val titles = listOf(
            "Quality",
            "Convenient",
            "Local"
        )

        val descs = listOf(
            "Sell your farm fresh products directly to consumers, cutting out the middleman and reducing emissions of the global supply chain. ",
            "Our team of delivery drivers will make sure your orders are picked up on time and promptly delivered to your customers.",
            "We love the earth and know you do too! Join us in reducing our local carbon footprint one order at a time. "
        )

        adapter = OnboardingAdapter(images, titles, descs, this)

        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    changeButtonColor(position)
                }
            }
        )
        binding.viewPager.setPageTransformer { page, position ->
            page.alpha = 0.25f + (1 - Math.abs(position))
        }
       /* binding.btnJoin.setOnClickListener {

            if (binding.viewPager.currentItem < 2) {
                binding.viewPager.currentItem =
                    binding.viewPager.currentItem + 1
            } else {

                // Navigate to Login
                findNavController().navigate(R.id.loginFragment)
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }*/
    }

    private fun changeButtonColor(position: Int) {

        when (position) {

            0 -> {
                binding.fragmain.setBackgroundColor(resources.getColor(R.color.green))
              /*  binding.btnJoin.setBackgroundColor(
                    resources.getColor(R.color.green)
                )*/
            }

            1 -> {
                binding.fragmain.setBackgroundColor(resources.getColor(R.color.orange))
               /* binding.btnJoin.setBackgroundColor(
                    resources.getColor(R.color.orange)
                )*/
            }
            2 -> {
                binding.fragmain.setBackgroundColor(resources.getColor(R.color.yellow))
                /*binding.btnJoin.setBackgroundColor(
                    resources.getColor(R.color.yellow)
                )*/
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoginClick() {
        findNavController().navigate(R.id.loginFragment)
    }
}