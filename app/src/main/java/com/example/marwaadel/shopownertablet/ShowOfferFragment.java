package com.example.marwaadel.shopownertablet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marwaadel.shopownertablet.mPicasso.PicassoClient;
import com.example.marwaadel.shopownertablet.model.OfferDataModel;
import com.example.marwaadel.shopownertablet.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowOfferFragment extends Fragment {
    ListView listview;
    FirebaseListAdapter<OfferDataModel> mOfferAdapter;
    offerAdapter OfferAdapter;

ImageView detailImg;
    ImageView FAB;
   // String mListId;
    TextView title;
    String mListId;

    LinearLayout linlaHeaderProgress;
    public ShowOfferFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.fragment_show_offer, container, false);
        if (isConnected(getActivity().getApplicationContext())) {
            linlaHeaderProgress = (LinearLayout) rootView.findViewById(R.id.linlaHeaderProgress);
            listview = (ListView) rootView.findViewById(R.id.mListView);
//            Firebase refListName = new Firebase(Constants.FIREBASE_URL_ACTIVE_LIST);
            Firebase listsRef = new Firebase(Constants.FIREBASE_URL);
            AuthData authData = listsRef.getAuth();
            Log.i("uidbgrb: " , authData.getUid());

            mListId = authData.getUid();
            Log.i("uidbgrb2: " , mListId);
            listsRef = new Firebase(Constants.FIREBASE_URL).child("OfferList").child(mListId);
            Query query = listsRef.orderByChild("status").equalTo("false");
            listsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("fief", "onDataChange: " + dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Log.d("kmjk", "onCreateView: " + listsRef.getKey());
            mOfferAdapter = new FirebaseListAdapter<OfferDataModel>(getActivity(), OfferDataModel.class, R.layout.offeritem, query) {
                @Override
                protected void populateView(View v, final OfferDataModel model, final int position) {

                    ImageView imgOffer = (ImageView) v.findViewById(R.id.offerImg);
                    title = (TextView) v.findViewById(R.id.titlteoffer);
                    TextView description = (TextView) v.findViewById(R.id.descriptionoffer);
                    TextView price = (TextView) v.findViewById(R.id.priceoffer);
                    ImageView detailImg=(ImageView) v.findViewById(R.id.detailBtn);


                    Log.d("data", "populateView " + model.getDescription());


                    detailImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("clicled", String.valueOf(position));
                Bundle arguments = new Bundle();
                arguments.putSerializable("DETAIL_OFFER", model);

                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(arguments);


                            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.offer_detail_container, fragment, DetailActivityFragment.TAG)
                        .commit();


                        }
                    });

                    PicassoClient.downloadImg(getActivity(), model.getOfferImage(), imgOffer);
                    title.setText(model.getTitle());
                    description.setText(model.getDescription());


                    price.setText(model.getDiscountAfter()+ " L.E");
                }
            };
            Log.e("ref", String.valueOf(listsRef));
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            listview.setAdapter(mOfferAdapter);
            mOfferAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
            });


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {


                    OfferDataModel offer=mOfferAdapter.getItem(position);
                   Log.d("clicked",offer.getTitle());


                }
            });


            FAB = (ImageView) rootView.findViewById(R.id.fab);
            //FAB.setBackgroundColor(Color.parseColor("#7d3971"));
//            FAB.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7d3971")));
            FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), addoffers.class);

                    startActivity(intent);
                    // getActivity().finish();



                }
            });


        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("No Internet connection.");
            alertDialog.setMessage("You have no internet connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        return rootView;
    }

}
