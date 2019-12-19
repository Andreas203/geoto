package com.example.geoto;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.database.LocationData;
import com.example.geoto.database.PathData;
import com.example.geoto.database.PhotoData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A class to define how the paths view displays a list of path data
 */
public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.View_Holder> {
    static private Context context;
    private static List<PathData> pathItems;
    private static List<LocationData> locItems;
    private static List<PhotoData> photoItems;

    public PathsAdapter(List<PathData> items) {
        this.pathItems = items;
    }

    /**
     * A constructor to initialise the adapter items
     */
    public PathsAdapter() {
        super();
        pathItems = new ArrayList<>();
        locItems = new ArrayList<>();
        photoItems = new ArrayList<>();
    }

    /**
     * Assigns a view to the view holder
     * @param parent the parent view
     * @param viewType
     * @return the updated holder
     */
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_path,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context = parent.getContext();
        return holder;
    }

    /**
     * Populates the view with adapted path items
     * @param holder a single path holder
     * @param position the position in the grid
     */
    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        Date date = pathItems.get(position).startDate;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String newDate = df.format(date);


        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && pathItems.get(position)!=null) {
            holder.title.setText(pathItems.get(position).title);
            holder.date.setText(newDate);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowPathActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * Get the number of paths displayed
     * @return the number of paths
     */
    @Override
    public int getItemCount() {
        return pathItems.size();
    }

    /**
     * An inner class to represent each displayed path
     */
    public class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;

        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.path_title);
            date = (TextView) itemView.findViewById(R.id.start_date);
        }
    }

    /**
     * Convenience method for getting data at click position
     */
    PathData getPath(int id) {
        return pathItems.get(id);
    }

    /**
     * Returns the path data list
     * @return pathItems
     */
    public static List<PathData> getPathItems() {
        return pathItems;
    }
    /**
     * Sets the path data list
     * @param items
     */
    public static void setPathItems(List<PathData> items) {
        PathsAdapter.pathItems = items;
    }

    /**
     * Returns the location data list
     * @return locItems
     */
    public static List<LocationData> getLocationItems() {
        return locItems;
    }
    /**
     * Sets the location data items
     * @param items
     */
    public static void setLocationItems(List<LocationData> items) {
        PathsAdapter.locItems = items;
    }

    /**
     * Returns the photo data list
     * @return photoItems
     */
    public static List<PhotoData> getPhotoItems() {
        return photoItems;
    }
    /**
     * Sets the photo data list
     * @param items
     */
    public static void setPhotoItems(List<PhotoData> items) {
        PathsAdapter.photoItems = items;
    }

    /**
     * Sorts the path items based on date
     * @param sortCode the direction of the sort
     */
    public void sortPaths(int sortCode) {
        if ((pathItems.size()) > 0) {
            if (sortCode == 0) {
                Collections.sort(pathItems);
            }
            if (sortCode == 1) {
                Collections.sort(pathItems, Collections.reverseOrder());
            }
        }
    }
}
