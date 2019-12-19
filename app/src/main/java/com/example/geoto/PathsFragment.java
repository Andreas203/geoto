package com.example.geoto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Fragment where old paths can be selected to be displayed
 */
public class PathsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private PathsAdapter mAdapter;
    private int sort = 0;

    /**
     * Creates a PathsFragment instance
     * @param index indicates which fragment this is
     * @return fragment to main view
     */
    public static PathsFragment newInstance(int index) {
        PathsFragment fragment = new PathsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * Run when created from the Main View
     * adds the fragment to the View Model
     * @param savedInstanceState contains the current state of the application
     */
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

    /**
     * Creates view and initialises path retrieval from database
     * Creates a recycler view and assigns it an adapter
     * @param inflater inflate the view to fill app
     * @param container the container containing the fragment
     * @param savedInstanceState contains the current state of the application
     * @return the root containing the inflated view and container
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.paths_main, container, false);
        final RecyclerView mRecyclerView = root.findViewById(R.id.paths_recycler_view);
        final TextView emptyView = root.findViewById(R.id.empty_paths);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAdapter = new PathsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        pageViewModel.getPathDataToDisplay().observe(this, new Observer<List<PathData>>(){
            @Override
            public void onChanged(@Nullable final List<PathData> pathList) {
                if (pathList.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                mAdapter.setPathItems(pathList);
                mAdapter.sortPaths(sort);
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

    /**
     * Option menu at the top right
     * @param menu drop down menu
     * @param inflater inflater making view fill the application screen
     *
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main, menu);
        //hide item (sort)
        menu.findItem(R.id.action_sort).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Reacts if options are clicked at top right and gives options for sorting by date
     * @param item items to be placed into the list
     * @return the items in the list
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            //do your function here
////            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
////            Toast.makeText(getContext(), "The settings button has been pressed!", Toast.LENGTH_SHORT).show();
//        }

        if (id == R.id.action_sort) {
//            Toast.makeText(getContext(), "The sort button has been pressed!", Toast.LENGTH_SHORT).show();
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a sorting style");
            // add a radio button list
            String[] animals = {"Date: Old to New", "Date: New to Old"};
            final int checkedItem = sort; // cow
            builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sort = which;
                }
            });

            // add OK and Cancel buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user clicked OK
                    mAdapter.sortPaths(sort);
                    mAdapter.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("Cancel", null);
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

}