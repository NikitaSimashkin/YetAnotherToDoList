package com.example.yetanothertodolist.ui.view.listFragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.ChangeThemeDialogBinding
import com.example.yetanothertodolist.di.ListFragmentComponentScope
import com.example.yetanothertodolist.other.ConstValues
import javax.inject.Inject

@ListFragmentComponentScope
class ThemeSelector @Inject constructor(
    private val sharedPreference: SharedPreferences,
    context: Context,
    listFragment: ListFragment
) {
    private var dialog: Dialog? = null
    private lateinit var binding: ChangeThemeDialogBinding

    init{
        createDialog(context, listFragment)
        setButtons()
    }

    fun showDialog() {
        dialog!!.show()
    }

    private fun createDialog(context: Context, listFragment: ListFragment) {
        dialog = Dialog(context)
        binding = ChangeThemeDialogBinding.bind(
            dialog!!.layoutInflater.inflate(
                R.layout.change_theme_dialog,
                dialog!!.findViewById(R.id.root_dialog)
            )
        )
        dialog!!.setContentView(binding.root)
    }

    private fun setButtons() {
        binding.systemLayout.setOnClickListener {
            sharedPreference.edit { putString(ConstValues.START_THEME, "system") }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            dialog!!.dismiss()
        }
        binding.darkLayout.setOnClickListener {
            sharedPreference.edit { putString(ConstValues.START_THEME, "dark") }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            dialog!!.dismiss()
        }
        binding.lightLayout.setOnClickListener {
            sharedPreference.edit { putString(ConstValues.START_THEME, "light") }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            dialog!!.dismiss()
        }
    }

}