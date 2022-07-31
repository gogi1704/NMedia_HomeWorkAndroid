package com.example.nmedia.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.db.PostsDbHelper
import com.example.nmedia.db.PostsDbHelper.PostColumns.DATA_BASE_NAME


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = PostsDbHelper(this , DATA_BASE_NAME , 1 , )
        db.writableDatabase
        db.writableDatabase

        navController = findNavController(R.id.nav_host_fragment)





    }


}