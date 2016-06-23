package com.example.marwaadel.shopownertablet;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marwaadel.shopownertablet.mPicasso.PicassoClient;
import com.example.marwaadel.shopownertablet.model.OfferDataModel;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by Marwa Adel on 6/18/2016.
 */
public class offerAdapter extends FirebaseListAdapter<OfferDataModel> {
    Activity activity;

    public offerAdapter(Activity activity, Class<OfferDataModel> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        Log.d("ddd", ref.toString());
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }

    @Override
    protected void populateView(View v, final OfferDataModel model, final int position) {
//        double num1, num2, result;
       // ImageView detailImg;
        ImageView detailImg=(ImageView) v.findViewById(R.id.detailBtn);
//        LinearLayout deleteBtn = (LinearLayout) v.findViewById(R.id.deleteLayout);
//        LinearLayout editBtn = (LinearLayout) v.findViewById(R.id.editLayout);
        ImageView imgOffer = (ImageView) v.findViewById(R.id.offerImg);
        TextView title = (TextView) v.findViewById(R.id.titlteoffer);
        TextView description = (TextView) v.findViewById(R.id.descriptionoffer);
        TextView after = (TextView) v.findViewById(R.id.priceoffer);
      //  TextView amount=(TextView)  v.findViewById(R.id.amount_detail);
        Log.d("data", "populateView " + model.getDescription());

        PicassoClient.downloadImg(activity, model.getOfferImage(), imgOffer);
        title.setText(model.getTitle());
        description.setText(model.getDescription());

        after.setText(model.getDiscountAfter());
      //  amount.setText(model.getAmount());

    }
}
