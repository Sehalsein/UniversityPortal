package com.sehalsein.universityportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.universityportal.Activity.NotificationActivity;
import com.sehalsein.universityportal.Model.MoreOption;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.UniversityPortalActivity.UniverstyAddCollege;

import java.util.List;

/**
 * Created by sehalsein on 28/09/17.
 */

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MoreOption> moreOptionList;
    private Context context;

    public MoreAdapter(List<MoreOption> moreOptionList, Context context) {
        this.moreOptionList = moreOptionList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_more_option, parent, false);
        return new MoreOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MoreOption item = moreOptionList.get(position);
        MoreOptionViewHolder moreOptionViewHolder = (MoreOptionViewHolder) holder;

        moreOptionViewHolder.setRow(item);

        moreOptionViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getTitle()){
                    case "Add College" :
                        context.startActivity(new Intent(context, UniverstyAddCollege.class));
                        break;
                    case "Places" :
                        //context.startActivity(new Intent(context, PlacesListActivity.class));
                        break;
                    case "Amenities" :
                        ///context.startActivity(new Intent(context, AmenitiesListActivity.class));
                        break;
                    case "Notification" :
                        context.startActivity(new Intent(context, NotificationActivity.class));
                        break;
                    default:
                        makeToast("Comming Soon");

                }
            }
        });


    }

    private void makeToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return moreOptionList.size();
    }

    public class MoreOptionViewHolder extends RecyclerView.ViewHolder{

        private TextView moreOptionTitle;
        private TextView moreOptionDescription;
        private ImageView moreOptionIcon;
        private View row;

        public MoreOptionViewHolder(View itemView) {
            super(itemView);
            moreOptionTitle = itemView.findViewById(R.id.more_option_title);
            moreOptionDescription = itemView.findViewById(R.id.more_option_description);
            moreOptionIcon = itemView.findViewById(R.id.more_option_icon_image_view);
            this.row = itemView;
        }

        public void setRow(MoreOption data){
            this.moreOptionTitle.setText(data.getTitle());
            if (data.getDescription().equals("")) {
                this.moreOptionDescription.setVisibility(View.GONE);
            }else {
                this.moreOptionDescription.setVisibility(View.VISIBLE);
                this.moreOptionDescription.setText(data.getDescription());
            }
            this.moreOptionIcon.setImageResource(data.getImage());

        }
    }
}
