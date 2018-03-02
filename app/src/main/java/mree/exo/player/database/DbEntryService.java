package mree.exo.player.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by eercan on 18.09.2015.
 */
public class DbEntryService {

    public static String TAG = "DbEntryService";


    public static SQLiteDatabase getDB() {
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    /*Save operations
        *******************************
        */


    public static void saveAudio(SongInfo songInfo) {

        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_ID, songInfo.getId());
            values.put(DbConstants.AUDIO_ACCOUNT_ID, songInfo.getAccountId());
            values.put(DbConstants.AUDIO_ALBUM, songInfo.getAlbum());
            values.put(DbConstants.AUDIO_ALBUM_ARTIST, songInfo.getAlbumArtist());
            values.put(DbConstants.AUDIO_ARTIST, songInfo.getArtist());
            values.put(DbConstants.AUDIO_BITRATE, songInfo.getBitrate());
            values.put(DbConstants.AUDIO_COMPOSERS, songInfo.getComposers());
            values.put(DbConstants.AUDIO_COPYRIGHT, songInfo.getCopyright());
            values.put(DbConstants.AUDIO_DISC, songInfo.getDisc());
            values.put(DbConstants.AUDIO_DISC_COUNT, songInfo.getDiscCount());
            values.put(DbConstants.AUDIO_DOWNLOAD_URL, songInfo.getDownloadUrl());
            values.put(DbConstants.AUDIO_DURATION, songInfo.getDuration());
            values.put(DbConstants.AUDIO_GENRE, songInfo.getGenre());
            values.put(DbConstants.AUDIO_HAS_DRM, songInfo.getHasDrm());
            values.put(DbConstants.AUDIO_IS_VARIABLE_BITRATE, songInfo.getIsVariableBitrate());
            values.put(DbConstants.AUDIO_PATH, songInfo.getPath());
            values.put(DbConstants.AUDIO_SHARE_URL, songInfo.getShareUrl());
            values.put(DbConstants.AUDIO_SOURCE_TYPE, songInfo.getSourceType().getCode());
            values.put(DbConstants.AUDIO_STATUS, songInfo.getStatus().getCode());
            values.put(DbConstants.AUDIO_TITLE, songInfo.getTitle());
            values.put(DbConstants.AUDIO_THUMBNAIL, songInfo.getThumbnail());
            values.put(DbConstants.AUDIO_TRACK, songInfo.getTrack());
            values.put(DbConstants.AUDIO_TRACK_COUNT, songInfo.getTrackCount());
            values.put(DbConstants.AUDIO_YEAR, songInfo.getYear());
            values.put(DbConstants.AUDIO_PARENT_ID, songInfo.getParentId());


            getDB().insert(DbConstants.AUDIO_TABLE_NAME, null, values);
            //Database Bağlantısını kapattık*/
            Log.e(TAG, "Audio saved to database. Id: " + songInfo.getId());
        } catch (Exception e) {
            Log.e(TAG, "Audio cannot be saved to database. Exception: " + e.getMessage());
        }
    }


    public static SongInfo getAudioInfo(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        SongInfo songInfo = new SongInfo();
        songInfo.setId(id);
        try {
            String selectQuery = "SELECT * FROM " +
                    DbConstants.AUDIO_TABLE_NAME +
                    " where " + DbConstants.AUDIO_ID + " = '" + id + "'";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        String value = cursor.getString(i);
                        if (value != null) {
                            switch (cursor.getColumnName(i)) {
                                case DbConstants.AUDIO_ACCOUNT_ID:
                                    songInfo.setAccountId(value);
                                    break;
                                case DbConstants.AUDIO_ALBUM:
                                    songInfo.setAlbum(value);
                                    break;
                                case DbConstants.AUDIO_ARTIST:
                                    songInfo.setArtist(value);
                                    break;
                                case DbConstants.AUDIO_DURATION:
                                    songInfo.setDuration(Long.parseLong(value));
                                    break;
                                case DbConstants.AUDIO_SOURCE_TYPE:
                                    songInfo.setSourceType(SourceType.get(Integer.parseInt(value)));
                                    break;
                                case DbConstants.AUDIO_STATUS:
                                    songInfo.setStatus(AudioStatus.get(Integer.parseInt(value)));
                                    break;
                                case DbConstants.AUDIO_TITLE:
                                    songInfo.setTitle(value);
                                    break;
                                case DbConstants.AUDIO_THUMBNAIL:
                                    songInfo.setThumbnail(value);
                                    break;
                                case DbConstants.AUDIO_PARENT_ID:
                                    songInfo.setParentId(value);
                                    break;
                                case DbConstants.AUDIO_PATH:
                                    songInfo.setPath(value);
                                    break;
                                case DbConstants.AUDIO_DOWNLOAD_URL:
                                    songInfo.setDownloadUrl(value);
                                    break;
                            }
                        }
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return songInfo;

    }


    public static ArrayList<HashMap<String, String>> getAllAudios() {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();


        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME + " order by " +
                    DbConstants.AUDIO_TITLE + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        map.put(cursor.getColumnName(i), cursor.getString(i));

                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }

    public static ArrayList<HashMap<String, String>> getAudiosByAccAndParentId(String accId,
                                                                               String parentId) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();


        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " +
                    DbConstants.AUDIO_ACCOUNT_ID + "='" + accId + "' AND " +
                    DbConstants.AUDIO_PARENT_ID + " = '" + parentId + "' " +
                    " order by " + DbConstants.AUDIO_TITLE + " asc";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        map.put(cursor.getColumnName(i), cursor.getString(i));

                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }


    public static ArrayList<HashMap<String, String>> getAudiosOfAlbum(String album, String artist) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();

        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ALBUM + " = ?  ";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{album});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

            // return kitap liste
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAudiosOfArtist(String artist) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ALBUM_ARTIST + " = ? OR " +
                    DbConstants.AUDIO_ARTIST + " = ?";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{artist, artist});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;

    }

    public static ArrayList<HashMap<String, String>> getAudiosOfAccount(String accId) {

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap
        // arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        ArrayList<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME +
                    " WHERE " + DbConstants.AUDIO_ACCOUNT_ID + " = ? " +
                    " order by " + DbConstants.AUDIO_TITLE + " asc";

            Cursor cursor = getDB().rawQuery(selectQuery, new String[]{accId});
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }

                    messageList.add(map);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return kitap liste
        return messageList;

    }


    public static List<String> getThumbnailsByAcc(String id) {
        try {
            List<String> thumbs = new ArrayList<>();
            String selectQuery = "SELECT " + DbConstants.AUDIO_THUMBNAIL + " FROM " + DbConstants
                    .AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_ACCOUNT_ID + " = '" + id + "' )";
            Cursor cursor = getDB().rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        thumbs.add(cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }


            return thumbs;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<String>();
        }
    }


    public static HashMap<String, String> getAudioById(String id) {
        try {
            String selectQuery = "SELECT * FROM " + DbConstants.AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_ID + " = '" + id + "' )";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            HashMap<String, String> chat = new HashMap<String, String>();

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        chat.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                } while (cursor.moveToNext());
            }


            return chat;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new HashMap<String, String>();
        }
    }

    public static List<String> getAudiosByStatus(AudioStatus status) {
        try {
            String selectQuery = "SELECT " + DbConstants.AUDIO_ID + " FROM " + DbConstants
                    .AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_STATUS + " = '" + status.getCode() + "' and " + DbConstants
                    .AUDIO_SOURCE_TYPE + " != '" + SourceType.LOCAL.getCode() + "' )";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            List<String> list = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (!list.contains(cursor.getString(i))) {
                            list.add(cursor.getString(i));
                        }
                    }
                } while (cursor.moveToNext());
            }


            return list;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<>();
        }
    }

    public static List<String> getAudiosByAccountAndStatus(String accId, AudioStatus status) {
        try {
            String selectQuery = "SELECT " + DbConstants.AUDIO_ID + " FROM " + DbConstants
                    .AUDIO_TABLE_NAME + " WHERE (" +
                    DbConstants.AUDIO_STATUS + " = '" + status.getCode() + "' and " + DbConstants
                    .AUDIO_SOURCE_TYPE + " != '" + SourceType.LOCAL.getCode() + "' and " +
                    DbConstants.AUDIO_ACCOUNT_ID + " = '" + accId + "')";
            Cursor cursor = getDB().rawQuery(selectQuery, null);
            List<String> list = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (!list.contains(cursor.getString(i))) {
                            list.add(cursor.getString(i));
                        }
                    }
                } while (cursor.moveToNext());
            }


            return list;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return new ArrayList<>();
        }
    }

    public static int getAudioRowCount() {
        try {
            String countQuery = "SELECT  * FROM " + DbConstants.AUDIO_TABLE_NAME;
            Cursor cursor = getDB().rawQuery(countQuery, null);
            int rowCount = cursor.getCount();

            cursor.close();
            // return row count
            return rowCount;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            return 0;
        }
    }



    /*Remove operations
    *******************************************************************
     */


    public static void updateAudioThumbnails(String id, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_THUMBNAIL, value);
            int update = getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants
                    .AUDIO_ID + "='" +
                    id + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public static void updateAudioTitle(String id, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_TITLE, value);
            int update = getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants
                    .AUDIO_ID + "='" +
                    id + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public static void updateAudioAlbum(String id, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_ALBUM, value);
            int update = getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants
                    .AUDIO_ID + "='" +
                    id + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public static void updateAudioArtist(String id, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_ARTIST, value);
            int update = getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants
                    .AUDIO_ID + "='" +
                    id + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public static void updateAudioDisk(String id, int value) {
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.AUDIO_DISC, value);
            int update = getDB().update(DbConstants.AUDIO_TABLE_NAME, values, DbConstants
                    .AUDIO_ID + "='" +
                    id + "'", null);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }
}
