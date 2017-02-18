package com.andro.jk.metisandroid1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Saikiran on 8/10/2016.
 */
public class AboutActivity extends AppCompatActivity {

    Button supportUs;
    ProgressDialog loading;
    private InterstitialAd mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_fragment);

        supportUs = (Button) findViewById(R.id.supportUs);

        supportUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ProgressDialog(AboutActivity.this);

                loading.setMessage("Loding Ad");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.setIndeterminate(true);
                loading.show();

                mInterstitial = new InterstitialAd(getApplicationContext());
                mInterstitial.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
                mInterstitial.setAdListener(new ToastAdListener(getApplicationContext()) {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        loading.dismiss();

                        AdRequest ar = new AdRequest.Builder().build();
                        mInterstitial.loadAd(ar);

                        mInterstitial.show();

                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        Toast.makeText(getApplicationContext(),getErrorReason(),Toast.LENGTH_SHORT).show();
                    }
                });

                AdRequest ar = new AdRequest.Builder().build();
                mInterstitial.loadAd(ar);
            }
        });

    }

}
