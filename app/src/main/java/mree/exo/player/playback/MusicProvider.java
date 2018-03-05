/*
 * Copyright (C) 2014 The Android Open Source Project
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

package mree.exo.player.playback;

import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import mree.cloud.music.player.common.model.SongInfo;
import mree.exo.player.model.MutableMediaMetadata;
import mree.exo.player.utils.LogHelper;


/**
 * Simple data provider for music tracks. The actual metadata source is delegated to a
 * MusicProviderSource defined by a constructor argument of this class.
 */
public class MusicProvider {

    private static final String TAG = LogHelper.makeLogTag(MusicProvider.class);
    private final ConcurrentMap<String, MutableMediaMetadata> mMusicListById;
    private volatile State mCurrentState = State.NON_INITIALIZED;
    private ConcurrentMap<String, MediaBrowserCompat.MediaItem> mediaItemMap;

    public MusicProvider() {
        mMusicListById = new ConcurrentHashMap<>();
    }


    public void retrieveMediaAsync(final Callback callback) {
        LogHelper.d(TAG, "retrieveMediaAsync called");
        if (mCurrentState == State.INITIALIZED) {
            if (callback != null) {
                // Nothing to do, execute callback immediately
                callback.onMusicCatalogReady(true);
            }
            return;
        }

        // Asynchronously load the music catalog in a separate thread
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... params) {

                return mCurrentState;
            }

            @Override
            protected void onPostExecute(State current) {
                if (callback != null) {
                    callback.onMusicCatalogReady(current == State.INITIALIZED);
                }
            }
        }.execute();
    }

    public MutableMediaMetadata getMusic(String queueId) {
        return mMusicListById.get(queueId);
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }


    public List<MediaBrowserCompat.MediaItem> getMediaItemList() {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();


        for (String key : mMusicListById.keySet()) {
            MutableMediaMetadata mutableMediaMetadata = mMusicListById.get(key);
            MediaBrowserCompat.MediaItem mediaItem = createMediaItem(mutableMediaMetadata);
            mediaItems.add(mediaItem);
            mediaItemMap.put(mutableMediaMetadata.songInfo.getId(), mediaItem);
        }

        return mediaItems;
    }

    private MediaBrowserCompat.MediaItem createMediaItem(MutableMediaMetadata mutableMedia) {
        String genre = mutableMedia.metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
        MediaMetadataCompat copy = new MediaMetadataCompat.Builder(mutableMedia.metadata)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mutableMedia.songInfo.getId())
                .build();
        return new MediaBrowserCompat.MediaItem(copy.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);

    }

    public MediaBrowserCompat.MediaItem getMediaItemBySong(SongInfo songInfo) {
        return mediaItemMap.get(songInfo.getId());
    }

    public ConcurrentMap<String, MutableMediaMetadata> getmMusicListById() {
        return mMusicListById;
    }

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }
}
