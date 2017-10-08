package com.sehalsein.universityportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.universityportal.Activity.NotificationActivity;
import com.sehalsein.universityportal.Model.NotificationDetail;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversityViewAllNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sehalsein on 01/10/17.
 */

public class NotificationAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NotificationDetail> notificationDetailList;
    private Context context;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String portal;
    private static final String TITLE_KEY = "title";
    private static final String MESSAGE_KEY = "message";
    private static final String PORTAL_KEY = "portal";



    public NotificationAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification_content, parent, false);
        return new NotificationContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final NotificationDetail notificationDetail = notificationDetailList.get(position);
        NotificationContentViewHolder notificationContentViewHolder = (NotificationContentViewHolder) holder;
        notificationContentViewHolder.setTitle(notificationDetail.getTitle());
        try {
            notificationContentViewHolder.setTimeStamp(printDifference(simpleDateFormat.parse(notificationDetail.getTimeStamp()),simpleDateFormat.parse(getCurrentTimeStamp())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notificationContentViewHolder.setMessage(notificationDetail.getMessage());

        if(portal.equals("Faculty")){
            notificationContentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeToast("Forward Notification");
                    sendNotification(notificationDetail.getTitle(),notificationDetail.getMessage());
                }
            });
        }
    }

    private void sendNotification(String title,String message){
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(MESSAGE_KEY, message);
        intent.putExtra(PORTAL_KEY, "Faculty");
        context.startActivity(intent);
    }

    private void makeToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return notificationDetailList.size();
    }

    public NotificationAdapter(List<NotificationDetail> notificationDetailList, Context context,String portal) {
        this.notificationDetailList = notificationDetailList;
        this.context = context;
        this.portal = portal;
    }


    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays > 0){
            return elapsedDays + " days ago";
        }else if(elapsedHours > 0){
            return elapsedHours + "h ago";
        }else if(elapsedMinutes > 0){
            return elapsedMinutes + "m ago";
        }else{
            return elapsedSeconds + "sec ago";
        }

    }

    public class NotificationContentViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView message;
        private TextView timeStamp;

        public NotificationContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            message = (TextView) itemView.findViewById(R.id.message);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setMessage(String message) {
            this.message.setText(message);
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp.setText(timeStamp);
        }
    }
}
