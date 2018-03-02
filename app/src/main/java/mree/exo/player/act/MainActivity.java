package mree.exo.player.act;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mree.cloud.music.player.common.model.AudioList;
import mree.cloud.music.player.common.model.SongInfo;
import mree.exo.player.AudioListAdapter;
import mree.exo.player.FillAudioInfo;
import mree.exo.player.R;
import mree.exo.player.database.Database;
import mree.exo.player.database.DbEntryService;
import mree.exo.player.service.MusicService;

public class MainActivity extends AppCompatActivity {
    public static final String FOLDER_INFO = "FOLDER_INFO";
    public static final String AUDIO_INFO = "AUDIO_INFO";
    public static final String FIRST = "FIRST";
    public static String AUDIO_LIST = "audio_list";
    public static String AUDIO_POSN = "audio_posn";
    public static boolean running;
    private static ThreadPoolExecutor downloadExecutor, scanExecutor;
    private static Database db;
    private static Context context;
    private static ServiceConnection musicConnection;
    private static MusicService musicSrv;
    private static boolean musicBound;
    private static boolean initialState = true;
    private static Handler addHandler, finishHandler, networkHandler;
    private static Handler refreshListHandler;
    private static ThreadPoolExecutor threadPoolExecutor;
    private static Intent playIntent;
    private AudioListAdapter adapter;
    private ListView listView;

    public static boolean startMusicService() {
        if (playIntent == null) {
            AudioList listObj = new AudioList();
            playIntent = new Intent(context, MusicService.class);
            musicConnection = getMusicConnection(listObj);
            context.bindService(playIntent, musicConnection, Context
                    .BIND_ADJUST_WITH_ACTIVITY);
            context.startService(playIntent);
            initialState = false;
            return false;
        } else {
            return true;
        }
    }

    public static ServiceConnection getMusicConnection(final AudioList audioList) {
        if (musicConnection == null) {
            musicConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                    //get service
                    musicSrv = binder.getService();
                    //pass listView
                    musicSrv.setPlaylist(audioList);
                    musicBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    musicBound = false;
                }
            };
        } else {
            musicSrv.setPlaylist(audioList);
            musicBound = true;
        }

        return musicConnection;
    }

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPoolExecutor == null) {
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            int corePoolSize = 10;
            int maximumPoolSize = 10;
            LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
            threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,       // Initial pool size
                    maximumPoolSize,       // Max pool size
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    workQueue);
        }
        return threadPoolExecutor;
    }

    public static void songPicked(Integer position) {
        if (musicSrv != null) {
            musicSrv.setSongPos(position);
            musicSrv.playSong();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMusicService();

    }

    private void fillAudioList() {
        ArrayList<HashMap<String, String>> allAudios = DbEntryService.getAllAudios();


        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        //listView.setItemsCanFocus(true);
        //listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.isEnabled(position)) {
                    Bundle b = new Bundle();
                    List<SongInfo> songs = new ArrayList<SongInfo>();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        songs.add(adapter.getItem(i));
                    }
                    AudioList listObj = new AudioList();
                    listObj.setList(songs);

                    if (musicSrv != null) {
                        musicSrv.setPlaylist(listObj);
                    }

                    songPicked(position);

                } else {
                    listView.getChildAt(position).setEnabled(false);
                }

            }
        });
        //listView.setOnItemClickListener(downloadListener());

        getThreadPool().submit(new FillAudioInfo(addHandler, finishHandler, allAudios));
    }

}
