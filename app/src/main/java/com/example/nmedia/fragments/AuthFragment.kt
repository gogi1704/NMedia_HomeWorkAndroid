package com.example.nmedia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.*
import com.example.nmedia.auth.OPEN_REGISTER_FRAGMENT_KEY
import com.example.nmedia.databinding.FragmentAuthBinding
import com.example.nmedia.viewModels.LogInViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val viewModel: LogInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentId = findNavController().currentDestination?.id!!
        requireActivity().invalidateMenu()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)

        with(binding) {
            if (arguments != null && requireArguments().containsKey(
                    OPEN_REGISTER_FRAGMENT_KEY
                )
            ) {
                authTitle.setText(R.string.signUp)
                buttonSignIn.setText(R.string.signUp)
                inputName.visibility = View.VISIBLE
                inputRepeatPass.visibility = View.VISIBLE
                buttonSignIn.setOnClickListener {
                    AndroidUtils.hideKeyboard(it)
                    if (inputPassword.text.toString() == inputRepeatPass.text.toString()
                        && inputPassword.text.toString().isNotBlank()
                        && inputName.text.toString().isNotBlank()
                    ) {
                        viewModel.register(
                            inputLogin.text.toString().trim(),
                            inputPassword.text.toString().trim(),
                            inputName.text.toString().trim()
                        )
                    } else Toast.makeText(context, "Incorrect password or name", Toast.LENGTH_LONG)
                        .show()

                }

            } else {
                authTitle.setText(R.string.signIn)
                buttonSignIn.setText(R.string.signIn)
                buttonSignIn.setOnClickListener {
                    AndroidUtils.hideKeyboard(it)
                    viewModel.signIn(inputLogin.text.toString(), inputPassword.text.toString())
                }

            }

        }


        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ERROR_SIGN_IN -> {
                    Snackbar.make(
                        requireView(),
                        "User is not found", Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.error = null
                }
                ERROR_UNKNOWN -> {
                    Snackbar.make(
                        requireView(),
                        "Unknown error. Check internet connection.", Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.error = null

                }
            }

        }


        viewModel.authState.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.authState.value = false
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        fragmentId = 0
        super.onDestroy()
    }

    companion object {
        var fragmentId = 0

    }


}