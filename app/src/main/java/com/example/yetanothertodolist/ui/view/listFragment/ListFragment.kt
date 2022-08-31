package com.example.yetanothertodolist.ui.view.listFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.di.ListFragmentComponent
import com.example.yetanothertodolist.di.ListFragmentComponentView
import com.example.yetanothertodolist.other.ConstValues
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout

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

        postponeEnterTransition()
        (view.parent as? View)?.doOnPreDraw {
            listFragmentViewComponent!!.listFragmentViewController().setUpScrolls()
            startPostponedEnterTransition()
        }
        listFragmentViewComponent =
            listFragmentComponent.listFragmentComponentView().create(binding)
        listFragmentViewComponent!!.listFragmentViewController().setUpView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val id = arguments?.getString(ConstValues.ID_TAG)
        if (view != null && id != null) {
            listFragmentComponent.listFragmentOpenCloseController().startAnimation()
        }
        val collapsingToolbarLayout = view?.findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbar)
        collapsingToolbarLayout?.isFocusableInTouchMode = true
        collapsingToolbarLayout?.requestFocus()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listFragmentViewComponent = null
    }
}