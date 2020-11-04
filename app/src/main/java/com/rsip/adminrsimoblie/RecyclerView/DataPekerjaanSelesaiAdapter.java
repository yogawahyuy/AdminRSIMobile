package com.rsip.adminrsimoblie.RecyclerView;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.rsip.adminrsimoblie.R;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class DataPekerjaanSelesaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DataPekerjaanSelesaiModel> modelList;

    private OnItemClickListener mItemClickListener;


    public DataPekerjaanSelesaiAdapter(Context context, ArrayList<DataPekerjaanSelesaiModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<DataPekerjaanSelesaiModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_pekerjaan_selesai_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final DataPekerjaanSelesaiModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtTitle.setText(model.getNamaPekerjaan());
            genericViewHolder.itemTxtMessage.setText(model.getNamaPekerja());


        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private DataPekerjaanSelesaiModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataPekerjaanSelesaiModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

}

