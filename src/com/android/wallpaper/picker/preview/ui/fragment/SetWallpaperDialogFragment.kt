/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wallpaper.picker.preview.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.wallpaper.R
import com.android.wallpaper.picker.di.modules.MainDispatcher
import com.android.wallpaper.picker.preview.ui.binder.SetWallpaperDialogBinder
import com.android.wallpaper.picker.preview.ui.viewmodel.WallpaperPreviewViewModel
import com.android.wallpaper.util.DisplayUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

/** Shows LS/HS previews and confirmation to set as wallpaper for HS, LS or both. */
@AndroidEntryPoint(DialogFragment::class)
class SetWallpaperDialogFragment : Hilt_SetWallpaperDialogFragment() {

    @Inject lateinit var displayUtils: DisplayUtils
    @Inject @MainDispatcher lateinit var mainScope: CoroutineScope

    private val wallpaperPreviewViewModel by activityViewModels<WallpaperPreviewViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.set_wallpaper_dialog, null)
        val dialog =
            AlertDialog.Builder(requireContext(), R.style.SetWallpaperPreviewDialogTheme)
                .setView(layout)
                .create()
        SetWallpaperDialogBinder.bind(
            layout,
            wallpaperPreviewViewModel,
            displayUtils.hasMultiInternalDisplays(),
            displayUtils.getRealSize(displayUtils.getWallpaperDisplay()),
            lifecycleOwner = this,
            mainScope,
            onFinishActivity = {
                val activity = activity ?: return@bind
                Toast.makeText(
                        activity,
                        R.string.wallpaper_set_successfully_message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
                activity.setResult(Activity.RESULT_OK)
                activity.finish()
            },
            onDismissDialog = { findNavController().popBackStack() },
        )

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        wallpaperPreviewViewModel.dismissSetWallpaperDialog()
    }
}
