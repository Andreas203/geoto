package com.example.geoto;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.PageViewModel;
import com.example.geoto.PathsAdapter;
import com.example.geoto.R;
import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;

import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PathsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private PathsAdapter mAdapter;

    public static PathsFragment newInstance(int index) {
        PathsFragment fragment = new PathsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 2;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main, menu);
        //hide item (sort)
        menu.findItem(R.id.action_sort).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.paths_main, container, false);
        final RecyclerView mRecyclerView = root.findViewById(R.id.paths_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAdapter = new PathsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        pageViewModel.getPathDataToDisplay().observe(this, new Observer<List<PathData>>(){
            @Override
            public void onChanged(@Nullable final List<PathData> pathList) {
                mAdapter.setPathItems(pathList);
                mAdapter.notifyDataSetChanged();
            }});

        pageViewModel.getLocationDataToDisplay().observe(this, new Observer<List<LocationData>>(){
            @Override
            public void onChanged(@Nullable final List<LocationData> pathList) {
                mAdapter.setLocationItems(pathList);
                mAdapter.notifyDataSetChanged();
            }});

        pageViewModel.getPhotoDataToDisplay().observe(this, new Observer<List<PhotoData>>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable final List<PhotoData> myPictureList) {
                mAdapter.setPhotoItems(myPictureList);
                mAdapter.notifyDataSetChanged();
            }});

        return root;
    }
}