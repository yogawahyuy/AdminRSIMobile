package com.rsip.adminrsimoblie.View;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.rsip.adminrsimoblie.R;

import pl.droidsonroids.gif.GifImageView;

public class CustomDialogView {

    Activity activity;
    Dialog dialog;

    public CustomDialogView(Activity activity){
        this.activity=activity;
    }

    public void showDialog(){
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progres_dialog);
        GifImageView gifImageView=dialog.findViewById(R.id.gifloading);
        Glide.with(activity).load(R.drawable.loading2).centerCrop().into(gifImageView);
        dialog.show();
    }
    public void hideDialog(){
        dialog.dismiss();
    }
}
