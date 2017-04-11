package com.example.karl.mobilefinal;

/**
 * Created by Karl Everett Kniel on 4/11/17.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PatientView extends RecyclerView
        .Adapter<PatientView.PatientObjectHolder>{

    private static String LOG_TAG = "PatientData";
    private ArrayList<PatientInfo> mDataset;
    private static MyClickListener myClickListener;

    public static class PatientObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView patientName;
        TextView patientRoom;
        TextView requestReason;

        public PatientObjectHolder(View itemView) {
            super(itemView);
            patientName = (TextView) itemView.findViewById(R.id.textView);
            patientRoom = (TextView) itemView.findViewById(R.id.textView2);
            requestReason = (TextView) itemView.findViewById(R.id.textView3);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PatientView(ArrayList<PatientInfo> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public PatientObjectHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_card, parent, false);

        PatientObjectHolder patientObjectHolder = new PatientObjectHolder(view);
        return patientObjectHolder;
    }

    @Override
    public void onBindViewHolder(PatientObjectHolder holder, int position) {
        holder.patientName.setText(mDataset.get(position).get_mname());
        holder.patientRoom.setText(mDataset.get(position).get_mroom());
        holder.requestReason.setText(mDataset.get(position).get_mhelp());
    }

    public void addItem(PatientInfo dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }




}