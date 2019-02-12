package com.mobiletechnologylab.com.samplemtechlabcontainerapp;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Baby Scale Params.
    private static final String BABY_SCALE_PACKAGE = "com.mobiletechnologylab.babyscale";
    private static final String BABY_SCALE_MEASUREMENT_ACTIVITY = "com.mobiletechnologylab.babyscale.measurement.ImageTargetsActivity";
    private static final String BABY_SCALE_ARG_FROM_EXTERNAL_APP_BOOL = "Arg:FromExternalApp";
    private static final String BABY_SCALE_RES_APP_MEASUREMENT_FLOAT = "Res:AppMeasurement";

    // Baby Height Params.
    // Coming Soon...

    // Baby MUAC Params.
    // Coming Soon...


    // Request codes.
    private static final int REQ_CODE_MEASURE_BABY_WEIGHT = 42;


    // UI refs
    private Button babyWeightBtn;
    private TextView babyWeightTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        babyWeightBtn = findViewById(R.id.babyWeightBtn);
        babyWeightTv = findViewById(R.id.babyWeightTv);

        babyWeightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent measureBabyWeight = new Intent();
                measureBabyWeight.setComponent(
                        new ComponentName(BABY_SCALE_PACKAGE, BABY_SCALE_MEASUREMENT_ACTIVITY));
                measureBabyWeight.putExtra(BABY_SCALE_ARG_FROM_EXTERNAL_APP_BOOL, true);
                try {
                    startActivityForResult(measureBabyWeight, REQ_CODE_MEASURE_BABY_WEIGHT);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found.", e);
                    installAppFromPlayStore(BABY_SCALE_PACKAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.w(TAG, "Ignoring cancelled activity: " + requestCode + " - res:" + resultCode);
            return;
        }
        switch (requestCode) {
            case REQ_CODE_MEASURE_BABY_WEIGHT: {
                Float measurement = data.getFloatExtra(BABY_SCALE_RES_APP_MEASUREMENT_FLOAT, -1f);
                babyWeightTv.setText(String.format("%.2f Kg", measurement));
            }
            default: {
                throw new RuntimeException("Unhandled request code: " + requestCode);
            }
        }
    }

    private void installAppFromPlayStore(final String packageName) {
        startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "market://details?id="
                                + packageName)));
    }
}
