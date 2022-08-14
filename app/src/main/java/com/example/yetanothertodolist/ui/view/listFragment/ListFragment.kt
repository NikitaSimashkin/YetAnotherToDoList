package com.example.yetanothertodolist.ui.view.listFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.di.ListFragmentComponent
import com.example.yetanothertodolist.di.ListFragmentComponentView
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity

/**
 * Фрагмент, на котором отображается список заданий
 */
class ListFragment : Fragment(R.layout.list_fragment) {

    private val mainActivityComponent by lazy { (requireActivity() as MainActivity).mainActivityComponent }

    private val listFragmentComponent: ListFragmentComponent by lazy {
        mainActivityComponent.listFragmentComponent().create(this)
    }

    private var listFragmentViewComponent: ListFragmentComponentView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = ListFragmentBinding.bind(view)
        listFragmentViewComponent =
            listFragmentComponent.listFragmentComponentView().create(binding)
        listFragmentViewComponent!!.listFragmentViewController.setUpView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listFragmentViewComponent = null
    }
}