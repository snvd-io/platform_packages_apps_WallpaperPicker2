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
package com.android.wallpaper.picker.preview.ui.fragment.smallpreview

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.android.wallpaper.R
import com.android.wallpaper.picker.preview.ui.binder.SmallPreviewBinder
import com.android.wallpaper.picker.preview.ui.fragment.smallpreview.adapters.SingleAndDualPreviewPagerAdapter
import com.android.wallpaper.picker.wallpaper.utils.DualDisplayAspectRatioLayout
import com.android.wallpaper.util.DisplayUtils
import kotlinx.coroutines.CoroutineScope

/**
 * This binder binds the data and view models for the dual preview collection on the small preview
 * screen
 */
object DualPreviewPagerBinder {

    fun bind(
        dualPreviewView: ViewPager2,
        homeScreenPreviewViewModel: SingleAndDualPreviewPagerAdapter.DualPreviewPagerViewModel,
        lockScreenPreviewViewModel: SingleAndDualPreviewPagerAdapter.DualPreviewPagerViewModel,
        applicationContext: Context,
        isSingleDisplayOrUnfoldedHorizontalHinge: Boolean,
        viewLifecycleOwner: LifecycleOwner,
        isRtl: Boolean,
        mainScope: CoroutineScope,
        displayUtils: DisplayUtils
    ) {
        dualPreviewView.adapter =
            SingleAndDualPreviewPagerAdapter(/* isDualPreview= */ true) { viewHolder, position ->
                val dualDisplayAspectRatioLayout: DualDisplayAspectRatioLayout =
                    viewHolder.itemView.requireViewById(R.id.dual_preview)
                val previewDisplays =
                    mapOf(
                        DualDisplayAspectRatioLayout.Companion.PreviewView.FOLDED to
                            displayUtils.getSmallerDisplay(),
                        DualDisplayAspectRatioLayout.Companion.PreviewView.UNFOLDED to
                            displayUtils.getWallpaperDisplay(),
                    )

                dualDisplayAspectRatioLayout.setDisplaySizes(
                    previewDisplays.mapValues { displayUtils.getRealSize(it.value) }
                )

                val dualPreviewPagerViewModel =
                    if (position == LOCK_PREVIEW_POSITION) homeScreenPreviewViewModel
                    else lockScreenPreviewViewModel
                DualDisplayAspectRatioLayout.Companion.PreviewView.entries.stream().forEach {
                    previewView ->
                    previewView.viewId.let { id ->
                        SmallPreviewBinder.bind(
                            applicationContext = applicationContext,
                            view = dualDisplayAspectRatioLayout.requireViewById(id),
                            viewModel = dualPreviewPagerViewModel.viewModel,
                            mainScope = mainScope,
                            viewLifecycleOwner = viewLifecycleOwner,
                            isSingleDisplayOrUnfoldedHorizontalHinge =
                                isSingleDisplayOrUnfoldedHorizontalHinge,
                            isRtl = isRtl,
                            previewDisplaySize =
                                checkNotNull(
                                    dualDisplayAspectRatioLayout.getPreviewDisplaySize(previewView)
                                ),
                            previewDisplayId = checkNotNull(previewDisplays[previewView]).displayId,
                            previewUtils = dualPreviewPagerViewModel.previewUtils,
                            navigate = { dualPreviewPagerViewModel.navigate?.let { it() } },
                        )
                    }
                }
            }
    }

    private const val LOCK_PREVIEW_POSITION = 0
    private const val HOME_PREVIEW_POSITION = 1
}
