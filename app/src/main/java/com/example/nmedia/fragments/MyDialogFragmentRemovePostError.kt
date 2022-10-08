package com.example.nmedia.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.viewModels.PostViewModel

class MyDialogFragmentRemovePostError(id: Int) : DialogFragment() {
    private val postId = id
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Deletion error")
                .setMessage("Try again ?")
                .setIcon(R.drawable.ic_error_server)
                .setPositiveButton("Continue") { dialog, _ ->
                    dialog.cancel()
                }
                .setNegativeButton("Try again") { dialog, id ->
                    viewModel.remove(postId,parentFragmentManager)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}