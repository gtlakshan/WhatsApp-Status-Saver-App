package com.infinitystock.statussaver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.infinitystock.statussaver.R;
import com.infinitystock.statussaver.databinding.ActivityLikeeBinding;
import com.infinitystock.statussaver.util.AdsUtils;
import com.infinitystock.statussaver.util.AppLangSessionManager;

public class LikeeActivity extends AppCompatActivity {

    private MySession session;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    ActivityLikeeBinding binding;
    LikeeActivity activity;
    AppLangSessionManager appLangSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_likee);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);

        //AdsUtils.showAdMobInterstitialAds(activity);
        AdsUtils.showGoogleBannerAd(activity,binding.adView);
        session = new MySession(this);

        if(session.isUserPurchased())
        {
            findViewById(R.id.adView).setVisibility(View.GONE); //sure nethi then
        }
        else
        {
            findViewById(R.id.adView).setVisibility(View.VISIBLE);   //sure nethi then

            bp = new BillingProcessor(this, AppConfig.LICENSE_KEY, null, new BillingProcessor.IBillingHandler() {
                @Override
                public void onProductPurchased(String productId, TransactionDetails details) {

                    session.setUserPurchase(true);
                    updateAds();

                }

                @Override
                public void onPurchaseHistoryRestored() {
                    Log.d("TAG","onPurchaseHistoryRestored");
                    for (String sku : bp.listOwnedProducts()){
                        if (sku.equals("com.haroonstudios.inapppurchasesproject")){
                            session.setUserPurchase(true);
                            Log.d("TAG","Owned Managed Product" + sku);
                        }

                    }
                    updateAds();

                }


                @Override
                public void onBillingError(int errorCode, Throwable error) {
                    Log.d("TAG","onBillingError:" + Integer.toString(errorCode));
                    session.setUserPurchase(false);

                }

                @Override
                public void onBillingInitialized() {
                    readyToPurchase = true;
                    updateAds();

                }
            });
        }







    }

    private void updateAds() {

        if (!session.isUserPurchased())
        {
            AdsUtils.showGoogleBannerAd(activity,binding.adView);   //Sure nethi then
        } else {
            findViewById(R.id.adView).setVisibility(View.GONE);       //Sure nethi then
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (bp.handleActivityResult(requestCode, resultCode, data)) {
            updateAds();
            return;
        }
    }

    public void onButton(View view)
    {
        bp.purchase(this,"premiumproduct");
    }


}