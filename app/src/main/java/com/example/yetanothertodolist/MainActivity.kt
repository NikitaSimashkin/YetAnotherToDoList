package com.example.yetanothertodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.Adapters.Task
import com.example.yetanothertodolist.Adapters.TaskAdapter
import com.example.yetanothertodolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val deleteLetter = listOf(Task(false, 0, "123", "1123"))
        binding.recyclerView.adapter = TaskAdapter(deleteLetter)

    }
}