package com.example.yetanothertodolist

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.databinding.AddFragmentBinding

class AddFragment: Fragment(R.layout.add_fragment) {

    private lateinit var binding: AddFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = AddFragmentBinding.bind(view)

        binding.close.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        val arrayAdapter: ArrayAdapter<CharSequence>? = context?.let { ArrayAdapter.createFromResource(it, R.array.importance, R.layout.importance_item) }
        arrayAdapter?.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter
    }
}