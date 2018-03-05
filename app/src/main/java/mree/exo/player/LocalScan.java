package mree.exo.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.concurrent.Callable;

import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.exo.player.database.DbEntryService;

/**
 * Created by eercan on 22.12.2016.
 */

public class LocalScan {
    private Context context;
    private ContentResolver contentResolver;
    private Callable<String> scanThread;


    public LocalScan(Context context) {
        this.context = context;
    }



    public Callable<String> getScanThread() {
        if (scanThread == null) {
            scanThread = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        SongInfo si = new SongInfo();
                        contentResolver = context.getContentResolver();
                        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        Cursor cursor = contentResolver.query(uri, null, null, null, null);
                        if (cursor == null) {
                            // query failed, handle error.
                        } else if (!cursor.moveToFirst()) {
                            // no media on the device
                        } else {
                            int titleColumn = cursor.getColumnIndex(MediaStore.Audio
                                    .Media.TITLE);
                            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media
                                    .ALBUM_ID);
                            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums
                                    .ARTIST);
                            int yearColumn = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
                            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media
                                    .DURATION);


                            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media
                                    ._ID);
                            do {
                                Long id = cursor.getLong(idColumn);
                                String title = cursor.getString(titleColumn);
                                String artist = cursor.getString(artistColumn);
                                String album = cursor.getString(albumColumn);
                                String albumId = cursor.getString(albumIdColumn);
                                Long year = cursor.getLong(yearColumn);
                                Long duration = cursor.getLong(durationColumn);
                                si.setAccountId("1");
                                si.setId(id.toString());
                                si.setSourceType(SourceType.LOCAL);

                                if (TextUtils.isEmpty(artist) || artist.toLowerCase().contains
                                        ("unkn")) {
                                    si.setArtist("UnknownArtist");
                                } else {
                                    si.setArtist(artist);
                                }

                                if (TextUtils.isEmpty(album) || album.toLowerCase().contains
                                        ("unkn")) {
                                    si.setAlbum("UnknownAlbum");
                                } else {
                                    si.setAlbum(album);
                                }

                                si.setYear(year);

                                if (TextUtils.isEmpty(title) || title.toLowerCase().contains
                                        ("unkn")) {
                                    si.setTitle("");
                                } else {
                                    si.setTitle(title);
                                }

                                si.setDuration(duration);
                                //si.setThumbnail(getThumbnailByAlbumId(albumId));
                                //si.setThumbnail("true");
                                si.setDownloadUrl(cursor.getString(cursor.getColumnIndex(MediaStore
                                        .Audio
                                        .Media.DATA)));
                                si.setStatus(AudioStatus.OFFLINE);
                                File f = new File(si.getDownloadUrl());
                                if (f.exists() && f.getParentFile().exists()) {
                                    String parentId = f.getParentFile().getAbsolutePath();
                                    si.setParentId(parentId);
                                }
                                DbEntryService.saveAudio(si);

                            } while (cursor.moveToNext());
                        }

                    } catch (Exception e) {
                        Log.e("LOCALSCAN", e.getMessage() + "");
                    }
                    return null;
                }
            };
        }
        return scanThread;
    }


}
