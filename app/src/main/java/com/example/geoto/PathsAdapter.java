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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.View_Holder> {
    static private Context context;
    private static List<PathData> pathItems;
    private static List<LocationData> locItems;
    private PageViewModel pageViewModel;

    public PathsAdapter(List<PathData> items) {
        this.pathItems = items;
    }

    public PathsAdapter() {
        super();
        pathItems = new ArrayList<>();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_path,
                parent, false);
        View_Holder holder = new View_Holder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        Date date = pathItems.get(position).startDate;

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
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

    @Override
    public int getItemCount() {
        return pathItems.size();
    }


    public class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;

        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.path_title);
            date = (TextView) itemView.findViewById(R.id.start_date);
        }
    }

    // convenience method for getting data at click position
    PathData getPath(int id) {
        return pathItems.get(id);
    }

    public static List<PathData> getPathItems() {
        return pathItems;
    }

    public static List<LocationData> getLocationItems() {
        return locItems;
    }

    public static void setPathItems(List<PathData> items) {
        PathsAdapter.pathItems = items;
    }

    public static void setLocationItems(List<LocationData> items) {
        PathsAdapter.locItems = items;
    }
}
