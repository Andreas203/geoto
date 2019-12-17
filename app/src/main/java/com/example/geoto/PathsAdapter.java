package com.example.geoto;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.geoto.database.PathData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.View_Holder>{
    static private Context context;
    private static List<PathData> items;
    private PageViewModel pageViewModel;

    public PathsAdapter(List<PathData> items) {
        this.items = items;
    }

    public PathsAdapter(Context cont, List<PathData> items) {
        super();
        this.items = items;
        context = cont;
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
        Date date = items.get(position).startDate;

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String newDate = df.format(date);


        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {
            holder.title.setText(items.get(position).title);
            //holder.description.setText(items.get(position).description);
            holder.date.setText(newDate);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Date sdate = items.get(position).startDate;
                    //Date edate = items.get(position).endDate;


                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                    //String startDate = df.format(sdate);
                    //String endDate = df.format(edate);

                    Intent intent = new Intent(context, ShowPathActivity.class);
                    intent.putExtra("position", position);

                    context.startActivity(intent);
                }
            });
        }
        //animate(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;

        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.path_title);
            description = (TextView) itemView.findViewById(R.id.path_description);
            date = (TextView) itemView.findViewById(R.id.start_date);
        }
    }

    // convenience method for getting data at click position
    PathData getPath(int id) {
        return items.get(id);
    }

    public static List<PathData> getItems() {
        return items;
    }

    public static void setItems(List<PathData> items) {
        PathsAdapter.items = items;
    }
}
