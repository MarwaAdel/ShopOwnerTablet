package com.example.marwaadel.shopownertablet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marwaadel.shopownertablet.mPicasso.PicassoClient;
import com.example.marwaadel.shopownertablet.model.OfferDataModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
   TextView tvTitle, tvDescription, tvBefore, tvAfter,tvAmount ,tvStartOffer,tvEndOffer;
    OfferDataModel offer;
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    static final String DETAIL_OFFER = "DETAIL_OFFER";
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
 if (arguments != null) {
             offer = (OfferDataModel) arguments.getSerializable(DetailActivityFragment.DETAIL_OFFER);


            // 3. get reference to offer textView
            ImageView Img=(ImageView)rootView.findViewById(R.id.btndetail);
            tvTitle = (TextView) rootView.findViewById(R.id.titlte_detail);
            tvDescription = (TextView) rootView.findViewById(R.id.description_detail);
            tvBefore = (TextView) rootView.findViewById(R.id.before_detail);
            tvAfter = (TextView) rootView.findViewById(R.id.after_detail);
            tvAmount=(TextView) rootView.findViewById(R.id.amount_detail);
            tvStartOffer=(TextView) rootView.findViewById(R.id.fromdate_detail);
            tvEndOffer=(TextView) rootView.findViewById(R.id.todate_detail);
            // 4. display details on textView
             PicassoClient.downloadImg(getActivity(),offer.getOfferImage(),Img);
        tvTitle.setText(offer.getTitle());
            tvDescription.setText(offer.getDescription());
             tvBefore.setText(offer.getDiscountBefore());
               tvAfter.setText(offer.getDiscountAfter());
tvAmount.setText(offer.getAmount());
            tvStartOffer.setText(offer.getDayStartOffer());
            tvEndOffer.setText(offer.getDayEndOffer());
        }


return  rootView;
    }
}
