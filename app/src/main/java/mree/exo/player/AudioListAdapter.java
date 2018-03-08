package mree.exo.player;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.common.model.SongInfo;

/**
 * Created by mree on 24.01.2016.
 */
public class AudioListAdapter extends ArrayAdapter<SongInfo> implements Filterable {


    //public ImageLoaderTask lazyListImageLoader= ImageUtils.getLazyImageLoaderTask();
    private int layoutResource;
    private LayoutInflater inflater;
    private ListView listView;
    private SparseBooleanArray mSelectedItemsIds;
    private Context context;
    private List<SongInfo> songs;
    private List<SongInfo> filteredSongs;
    private Filter filter;

    public AudioListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.songs = new ArrayList<>();
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void add(SongInfo object) {
        super.add(object);
        songs.add(object);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_audio_row, parent, false);
        }


        final SongInfo song = getItem(position);
        TextView title = (TextView) row.findViewById(R.id.tvTitle);
        TextView album = (TextView) row.findViewById(R.id.tvAlbum);
        TextView artist = (TextView) row.findViewById(R.id.tvArtist);

        title.setText(song.getTitle());

        if (song.getAlbum() != null) {
            album.setText(song.getAlbum());
        } else {
            album.setText("UnknownAlbum");
        }

        if (song.getArtist() != null) {
            artist.setText(song.getArtist());
        } else {
            artist.setText("UnknownArtist");
        }

        row.setBackgroundResource(R.drawable.audio_row_bckgrnd);
        return row;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public List<SongInfo> getSongs() {
        return songs;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                AudioListAdapter.this.clear();
                filteredSongs = (ArrayList<SongInfo>) results.values;
                AudioListAdapter.this.addAll(filteredSongs);
                AudioListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SongInfo> FilteredArrayNames = new ArrayList<SongInfo>();

                // perform your search here using the searchConstraint String.

                String selectedStatus = constraint.toString();
                for (int i = 0; i < songs.size(); i++) {
                    String aCode = "A" + songs.get(i).getStatus().getCode().toString();
                    String sCode = "S" + songs.get(i).getSourceType().getCode().toString();
                    if (selectedStatus.contains(aCode) && selectedStatus.contains(sCode)) {
                        FilteredArrayNames.add(songs.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        Filter titleFilter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                AudioListAdapter.this.clear();
                filteredSongs = (ArrayList<SongInfo>) results.values;
                AudioListAdapter.this.addAll(filteredSongs);
                AudioListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SongInfo> FilteredArrayNames = new ArrayList<SongInfo>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < songs.size(); i++) {
                    String dataNames = songs.get(i).getTitle();
                    if (!TextUtils.isEmpty(dataNames) &&
                            dataNames.toLowerCase().contains(constraint.toString())) {
                        FilteredArrayNames.add(songs.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return titleFilter;
    }

    public Filter getViewFilter() {

        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                AudioListAdapter.this.clear();
                filteredSongs = (ArrayList<SongInfo>) results.values;
                AudioListAdapter.this.addAll(filteredSongs);
                AudioListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SongInfo> FilteredArrayNames = new ArrayList<SongInfo>();

                // perform your search here using the searchConstraint String.

                String selectedStatus = constraint.toString();
                for (int i = 0; i < songs.size(); i++) {
                    String aCode = "A" + songs.get(i).getStatus().getCode().toString();
                    String sCode = "S" + songs.get(i).getSourceType().getCode().toString();
                    if (selectedStatus.contains(aCode) && selectedStatus.contains(sCode)) {
                        FilteredArrayNames.add(songs.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };
        return filter;
    }


    @Override
    public void clear() {
        super.clear();
    }
}
