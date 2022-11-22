package com.example.nmedia.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nmedia.R
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.auth.OPEN_REGISTER_FRAGMENT_KEY
import com.example.nmedia.auth.OPEN_REGISTER_FRAGMENT_VALUE
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.viewModels.AuthViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: AppAuth

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        authViewModel.data.observe(this) {
            invalidateMenu()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == AuthFragment.fragmentId) {

            menu.let {
                it?.setGroupVisible(R.id.authenticated_true, false)
                it?.setGroupVisible(R.id.authenticated_false, false)
            }

        } else {
            menu.let {
                it?.setGroupVisible(R.id.authenticated_true, authViewModel.authenticated)
                it?.setGroupVisible(R.id.authenticated_false, !authViewModel.authenticated)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signIn -> {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_mainFragment_to_authFragment,
                )
                true
            }
            R.id.signUp -> {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_mainFragment_to_authFragment,
                    Bundle().apply {
                        putString(OPEN_REGISTER_FRAGMENT_KEY, OPEN_REGISTER_FRAGMENT_VALUE)
                    })
                true
            }
            R.id.signOut -> {
                if (findNavController(R.id.nav_host_fragment).currentDestination?.id == CreatePostFragment.fragmentId) {
                    AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton(
                            "Yes"
                        ) { dialog, which ->
                            findNavController(R.id.nav_host_fragment).navigateUp()
                            auth.removeAuth()
                        }
                        .setNegativeButton("Back")
                        { dialog, wich ->
                            return@setNegativeButton
                        }
                        .create()
                        .show()
                } else auth.removeAuth()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}