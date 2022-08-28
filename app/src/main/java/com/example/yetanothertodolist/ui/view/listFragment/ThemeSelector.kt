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
    private val listFragmentOpenCloseController: ListFragmentOpenCloseController
) {
    private var dialog: Dialog? = null
    private lateinit var binding: ChangeThemeDialogBinding

    init {
        createDialog(context)
        setButtons()
    }

    fun showDialog() {
        dialog!!.show()
    }

    private fun createDialog(context: Context) {
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
            changeTheme(Theme.System)
        }
        binding.darkLayout.setOnClickListener {
            changeTheme(Theme.Dark)
        }
        binding.lightLayout.setOnClickListener {
            changeTheme(Theme.Light)
        }
    }

    private fun changeTheme(theme: Theme) {
        val mode = when (theme) {
            Theme.System -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            Theme.Light -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            Theme.Dark -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        }
        sharedPreference.edit { putString(ConstValues.START_THEME, theme.toString().lowercase()) }
        listFragmentOpenCloseController.saveScrollPosition()
        AppCompatDelegate.setDefaultNightMode(mode)
        dialog!!.dismiss()
    }

}

enum class Theme {
    Light, Dark, System
}