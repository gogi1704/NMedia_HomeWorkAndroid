package com.example.nmedia.fragments


import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.AndroidUtils
import com.example.nmedia.DEFAULT_VALUE
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentCreatePostBinding
import com.example.nmedia.viewModels.PostViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    companion object{
        var fragmentId = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentId = findNavController().currentDestination?.id!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreatePostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by activityViewModels()

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        viewModel.changePhoto(uri, uri?.toFile())
                    }
                }

            }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val text = binding.textEdit.text.toString()
            if (text.isNotBlank()) {
                viewModel.putSharedPref(text)
            }
            findNavController().navigateUp()
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.create_post_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.save -> {
                        val text = binding.textEdit.text.toString()
                        if (binding.textEdit.text.isNotBlank()) {
                            viewModel.editContent(text)
                            viewModel.savePost()
                        }
                        true
                    }


                    else -> false
                }


        }, viewLifecycleOwner)

        with(binding) {
            if (viewModel.getSharedPref() != DEFAULT_VALUE) {
                textEdit.setText(viewModel.getSharedPref())
            }
            textEdit.requestFocus()
            AndroidUtils.showKeyboard(parentConstraint)

            takeImage.setOnClickListener {
                ImagePicker.with(this@CreatePostFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .galleryMimeTypes(
                        arrayOf(
                            "image/png",
                            "image/jpeg",
                        )
                    )
                    .createIntent {
                        pickPhotoLauncher.launch(it)
                    }
            }

            createImage.setOnClickListener {
                ImagePicker.with(this@CreatePostFragment)
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.CAMERA)
                    .createIntent {
                        pickPhotoLauncher.launch(it)
                    }
            }

            imageViewDelete.setOnClickListener {
                viewModel.changePhoto(null, null)
            }

        }


        viewModel.postCreated.observe(viewLifecycleOwner) {
            val text = binding.textEdit.text.toString()
            if (text.isNotBlank()) {
                viewModel.putSharedPref(text)
            }
            findNavController().navigateUp()
        }

        viewModel.photo.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.photoContainer.visibility = View.GONE
            } else
                binding.photoContainer.visibility = View.VISIBLE
            binding.image.setImageURI(it.uri)

        }

        return binding.root
    }

    override fun onDestroy() {
        fragmentId = 0
        super.onDestroy()
    }


}

