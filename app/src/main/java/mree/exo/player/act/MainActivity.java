package mree.exo.player.act;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mree.cloud.music.player.common.model.AudioList;
import mree.cloud.music.player.common.model.SongInfo;
import mree.exo.player.AudioListAdapter;
import mree.exo.player.FillAudioInfo;
import mree.exo.player.LocalScan;
import mree.exo.player.R;
import mree.exo.player.database.Database;
import mree.exo.player.database.DbEntryService;
import mree.exo.player.database.DbTableService;
import mree.exo.player.service.BackgroundAudioService;

public class MainActivity extends AppCompatActivity {
    public static final String AUDIO_INFO = "AUDIO_INFO";
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private static Database db;
    private static Context context;
    private static Handler addHandler, finishHandler;
    private static ThreadPoolExecutor threadPoolExecutor;
    private AudioListAdapter adapter;
    private ListView listView;
    private int mCurrentState;

    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaControllerCompat mMediaControllerCompat;

    private Button mPlayPauseToggleButton;
    private MediaControllerCompat.Callback mMediaControllerCompatCallback = new
            MediaControllerCompat.Callback() {

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                    if (state == null) {
                        return;
                    }

                    switch (state.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING: {
                            mCurrentState = STATE_PLAYING;
                            break;
                        }
                        case PlaybackStateCompat.STATE_PAUSED: {
                            mCurrentState = STATE_PAUSED;
                            break;
                        }
                    }
                }
            };
    private MediaBrowserCompat.ConnectionCallback mMediaBrowserCompatConnectionCallback = new
            MediaBrowserCompat.ConnectionCallback() {

                @Override
                public void onConnected() {
                    super.onConnected();
                    try {
                        mMediaControllerCompat = new MediaControllerCompat(MainActivity.this,
                                mMediaBrowserCompat.getSessionToken());
                        mMediaControllerCompat.registerCallback(mMediaControllerCompatCallback);
                        MediaControllerCompat.setMediaController(MainActivity.this,
                                mMediaControllerCompat);
                   /*     MediaControllerCompat.getMediaController(MainActivity.this)
                                .getTransportControls().playFromMediaId(String
                                .valueOf
                                        (R.raw.warner_tautz_off_broadway), null);*/

                    } catch (RemoteException e) {

                    }
                }
            };

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

    public static Database getDb(Context context) {
        if (db == null) {
            db = new Database(context);
        }

        return db;
    }

    private void createTables() {
        DbTableService.createAudioTable();
    }

    public void songPicked(Integer position) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        getDb(getApplicationContext());
        addHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                SongInfo si = (SongInfo) data.getSerializable(AUDIO_INFO);
                adapter.add(si);
            }
        };

        finishHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                adapter.sort(new Comparator<SongInfo>() {
                    @Override
                    public int compare(SongInfo a, SongInfo b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
            }
        };
        fillAudioList();

        //mPlayPauseToggleButton = (Button) findViewById(R.id.button);

        mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this,
                BackgroundAudioService.class),
                mMediaBrowserCompatConnectionCallback, getIntent().getExtras());

        mMediaBrowserCompat.connect();

        mPlayPauseToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaControllerCompat mediaController = MediaControllerCompat.getMediaController
                        (MainActivity.this);
                if (mCurrentState == STATE_PAUSED) {
                    mediaController.getTransportControls().play();
                    mCurrentState = STATE_PLAYING;
                } else {
                    if (mediaController.getPlaybackState().getState() ==
                            PlaybackStateCompat.STATE_PLAYING) {
                        mediaController.getTransportControls().pause();
                    }

                    mCurrentState = STATE_PAUSED;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
            mediaController.getTransportControls().pause();
        }

        mMediaBrowserCompat.disconnect();
    }

    private void fillAudioList() {
        ArrayList<HashMap<String, String>> allAudios = DbEntryService.getAllAudios();
        if (allAudios.isEmpty()) {
            LocalScan scan = new LocalScan(getApplicationContext());
            getThreadPool().submit(scan.getScanThread());
        } else {

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

}
