package com.example.nmedia.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.viewModels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDialogFragmentCreatePostError : DialogFragment() {
    val viewModel: PostViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Server error")
                .setMessage("Try again ??")
                .setIcon(R.drawable.ic_error_server)
                .setPositiveButton("Continue") { dialog, _ ->
                    viewModel.clearSharedPref()
                    dialog.cancel()
                }
                .setNegativeButton("Try again") { dialog, id ->
                    findNavController().navigate(R.id.action_mainFragment_to_createPostFragment)
                }
            viewModel.errorCreateFragmentLiveData.value = false
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}