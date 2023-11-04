/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.android.wallpaper.model;

import android.app.WallpaperInfo;

import java.util.List;

/**
 * Lightweight wrapper for user-facing wallpaper metadata.
 */
public class WallpaperMetadata {

    private final List<String> mAttributions;
    private final String mActionUrl;
    private final String mCollectionId;
    protected final android.app.WallpaperInfo mWallpaperComponent;

    public WallpaperMetadata(List<String> attributions, String actionUrl, String collectionId,
                             android.app.WallpaperInfo wallpaperComponent) {
        mAttributions = attributions;
        mActionUrl = actionUrl;
        mCollectionId = collectionId;
        mWallpaperComponent = wallpaperComponent;
    }

    /**
     * Returns wallpaper's attributions.
     */
    public List<String> getAttributions() {
        return mAttributions;
    }

    /**
     * Returns the wallpaper's action URL or null if there is none.
     */
    public String getActionUrl() {
        return mActionUrl;
    }

    /**
     * Returns the wallpaper's collection ID or null if there is none.
     */
    public String getCollectionId() {
        return mCollectionId;
    }

    /**
     * Returns the {@link android.app.WallpaperInfo} if a live wallpaper, or null if the metadata
     * describes an image wallpaper.
     */
    public WallpaperInfo getWallpaperComponent() {
        throw new UnsupportedOperationException("Not implemented for static wallpapers");
    }
}
