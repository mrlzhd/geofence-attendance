package com.example.geofencing.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geofencing.R;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_className, tv_classLat, tv_classlong, tv_hisotryclassName, tv_hisotryDate, tv_hisotryTime;
    public Button btn_gotoMap;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_className = itemView.findViewById(R.id.tv_className);
        tv_classLat = itemView.findViewById(R.id.tv_classLat);
        tv_classlong = itemView.findViewById(R.id.tv_classLong);

        tv_hisotryclassName = itemView.findViewById(R.id.tv_hisotryclassName);
        tv_hisotryDate = itemView.findViewById(R.id.tv_hisotryDate);
        tv_hisotryTime = itemView.findViewById(R.id.tv_hisotryTime);


        btn_gotoMap = itemView.findViewById(R.id.btn_gotoMap);




    }
}
