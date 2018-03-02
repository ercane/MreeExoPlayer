package mree.exo.player.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by eercan on 18.09.2015.
 */
public class DbTableService {

    public static SQLiteOpenHelper sqoh;
    public static String TAG = "DATABASE";

    private static SQLiteDatabase getDB() {
        SQLiteDatabase database = Database.getDatabase();
        if (database == null || !database.isOpen()) {
            database = Database.getSqoh().getWritableDatabase();
        }
        return database;
    }

    public static void createAudioTable() {
        try {

            String createTable = "CREATE TABLE IF NOT EXISTS " + DbConstants.AUDIO_TABLE_NAME + "("
                    + DbConstants.AUDIO_ID + " TEXT PRIMARY KEY,"
                    + DbConstants.AUDIO_ACCOUNT_ID + " TEXT,"
                    + DbConstants.AUDIO_ALBUM + " TEXT ,"
                    + DbConstants.AUDIO_ALBUM_ARTIST + " TEXT,"
                    + DbConstants.AUDIO_ARTIST + " TEXT,"
                    + DbConstants.AUDIO_BITRATE + " NUMERIC,"
                    + DbConstants.AUDIO_COMPOSERS + " TEXT,"
                    + DbConstants.AUDIO_COPYRIGHT + " TEXT,"
                    + DbConstants.AUDIO_DISC + " NUMERIC,"
                    + DbConstants.AUDIO_DISC_COUNT + " NUMERIC,"
                    + DbConstants.AUDIO_DOWNLOAD_URL + " TEXT,"
                    + DbConstants.AUDIO_DURATION + " NUMERIC,"
                    + DbConstants.AUDIO_FILENAME + " TEXT,"
                    + DbConstants.AUDIO_GENRE + " TEXT,"
                    + DbConstants.AUDIO_HAS_DRM + " TEXT,"
                    + DbConstants.AUDIO_IS_VARIABLE_BITRATE + " TEXT,"
                    + DbConstants.AUDIO_PATH + " TEXT,"
                    + DbConstants.AUDIO_SHARE_URL + " TEXT,"
                    + DbConstants.AUDIO_SOURCE_TYPE + " NUMERIC,"
                    + DbConstants.AUDIO_STATUS + " NUMERIC,"
                    + DbConstants.AUDIO_THUMBNAIL + " TEXT,"
                    + DbConstants.AUDIO_TITLE + " TEXT,"
                    + DbConstants.AUDIO_TRACK + " NUMERIC,"
                    + DbConstants.AUDIO_TRACK_COUNT + " NUMERIC,"
                    + DbConstants.AUDIO_YEAR + " NUMERIC,"
                    + DbConstants.AUDIO_PARENT_ID + " TEXT)";
            getDB().execSQL(createTable);
            getDB().close();
            Log.e(TAG, DbConstants.AUDIO_TABLE_NAME + " table created. ");
        } catch (Exception e) {
            Log.e(TAG, DbConstants.AUDIO_TABLE_NAME + " table cannot be created. Exception: " + e
                    .getMessage());
        }
    }


    public static void dropTable(String tableName) {
        try {
            String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
            getDB().execSQL(DROP_TABLE);
            getDB().close();
            Log.e(TAG, "Table '" + tableName + "' dropped");
        } catch (SQLException e) {
            Log.e(TAG, "Table '" + tableName + "'cannot be dropped. Exception: " + e.getMessage());
        }
    }
}
