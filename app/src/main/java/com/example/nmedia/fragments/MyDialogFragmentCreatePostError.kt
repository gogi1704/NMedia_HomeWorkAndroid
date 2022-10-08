package com.example.nmedia.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R

class MyDialogFragmentCreatePostError : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Server error")
                .setMessage("Try again ??")
                .setIcon(R.drawable.ic_error_server)
                .setPositiveButton("Continue") { dialog, _ ->
                    dialog.cancel()
                }
                .setNegativeButton("Try again") { dialog, id ->
                    findNavController().navigate(R.id.action_mainFragment_to_createPostFragment)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}