package com.mosio.cashree_demo_integration;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.core.api.upi.CFUPI;
import com.cashfree.pg.core.api.upi.CFUPIPayment;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;

public class MainActivity extends AppCompatActivity {

    Button btn_make_payment;
    String phone,email,amount;
    //changes
    SurroundCardView google_pay,amazon_pay,phone_pay,bhim,paytm,mobikwik;
    String package_name;
    long oId;
    Dialog dialog;
    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone="9301982112";
        email="omyadav04352@gmail.com";
        amount="1";

        findViewById(R.id.collect).setOnClickListener(v->{
            httpCall_collect();
        });

        google_pay=findViewById(R.id.google_pay);
        amazon_pay=findViewById(R.id.amazon_pay);
        phone_pay=findViewById(R.id.phone_pay);
        bhim=findViewById(R.id.bhim);
        paytm=findViewById(R.id.paytm);
        mobikwik=findViewById(R.id.mobikwik);

        google_pay.setOnClickListener(v->{
            if(!google_pay.isCardSurrounded()) {
                package_name="com.google.android.apps.nbu.paisa.user";
                google_pay.setSurroundStrokeWidth(R.dimen.width_card);
                google_pay.surround();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        amazon_pay.setOnClickListener(v->{
            if(!amazon_pay.isCardSurrounded()) {
                package_name="in.amazon.mShop.android.shopping";
                amazon_pay.setSurroundStrokeWidth(R.dimen.width_card);
                amazon_pay.surround();
                google_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone_pay.setOnClickListener(v->{
            if(!phone_pay.isCardSurrounded()) {
                package_name="com.phonepe.app";
                phone_pay.setSurroundStrokeWidth(R.dimen.width_card);
                phone_pay.surround();
                google_pay.release();
                amazon_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bhim.setOnClickListener(v->{
            if(!bhim.isCardSurrounded()) {
                package_name="in.org.npci.upiapp";
                bhim.setSurroundStrokeWidth(R.dimen.width_card);
                bhim.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                paytm.release();
                mobikwik.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        paytm.setOnClickListener(v->{
            if(!paytm.isCardSurrounded()) {
                package_name="net.one97.paytm";
                paytm.setSurroundStrokeWidth(R.dimen.width_card);
                paytm.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                mobikwik.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mobikwik.setOnClickListener(v->{
            if(!mobikwik.isCardSurrounded()) {
                package_name="com.mobikwik_new";
                mobikwik.setSurroundStrokeWidth(R.dimen.width_card);
                mobikwik.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                if(checkApplication(package_name))
                    httpCall_intent(package_name);
                else{
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void httpCall_collect() {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        oId=System.currentTimeMillis();
        StringBuilder input1 = new StringBuilder();
        input1.append(oId);
        input1.reverse();

        JSONObject jsonBody = new JSONObject();
      try
      {
          jsonBody.put("order_amount", amount);
          jsonBody.put("customer_id", input1+"");
          jsonBody.put("order_id", oId+"");
          jsonBody.put("order_note", "Subscription");
          jsonBody.put("customer_email", email);
          jsonBody.put("customer_phone", phone);
          Log.d("body", "httpCall_collect: "+jsonBody);
      }
      catch (Exception e)
      {
        Log.e("Error","JSON ERROR");
      }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cashfree-server-production.up.railway.app/api/create-order";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        dialog.dismiss();
                        String token=response.optJSONObject("response").optString("order_token");

                        Log.e("response", response.optJSONObject("response").optString("order_token"));
                        Log.e("response", response.toString());

                        try {
                            CFSession cfSession = new CFSession.CFSessionBuilder()
                                    .setEnvironment(cfEnvironment)
                                    .setOrderToken(token)
                                    .setOrderId(oId+"")
                                    .build();
                            CFPaymentComponent cfPaymentComponent = new CFPaymentComponent.CFPaymentComponentBuilder()
                                    // Shows only Card and UPI modes
                                    .add(CFPaymentComponent.CFPaymentModes.CARD)
                                    .add(CFPaymentComponent.CFPaymentModes.UPI)
                                    .add(CFPaymentComponent.CFPaymentModes.WALLET)
                                    .add(CFPaymentComponent.CFPaymentModes.PAY_LATER)
                                    .add(CFPaymentComponent.CFPaymentModes.EMI)
                                    .add(CFPaymentComponent.CFPaymentModes.PAYPAL)
                                    .add(CFPaymentComponent.CFPaymentModes.NB)
                                    .build();
                            // Replace with your application's theme colors
                            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                                    .setNavigationBarBackgroundColor("#2D796D")
                                    .setNavigationBarTextColor("#ffffff")
                                    .setButtonBackgroundColor("#2D796D")
                                    .setButtonTextColor("#ffffff")
                                    .setPrimaryTextColor("#000000")
                                    .setSecondaryTextColor("#000000")
                                    .build();
                            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                                    .setSession(cfSession)
                                    .setCFUIPaymentModes(cfPaymentComponent)
                                    .setCFNativeCheckoutUITheme(cfTheme)
                                    .build();
                            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
                            gatewayService.doPayment(MainActivity.this, cfDropCheckoutPayment);
                            gatewayService.setCheckoutCallback(new CFCheckoutResponseCallback() {
                                @Override
                                public void onPaymentVerify(String s) {
                                    Log.e("sunna bhai hogya",s+" hogya");
                                }

                                @Override
                                public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
                                    Log.e("dekhle bhai ",s+"");
                                    Log.e("ok bhai ",cfErrorResponse.getCode()+"");
                                }
                            });
                        } catch (CFException exception) {
                            exception.printStackTrace();
                        }

                        Log.e("response",token);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ","Wrong");
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);
    }
    public void httpCall_intent(String package_name) {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        oId=System.currentTimeMillis();
        StringBuilder input1 = new StringBuilder();

        // append a string into StringBuilder input1
        input1.append(oId+"");

        // reverse StringBuilder input1
        input1.reverse();
        JSONObject jsonBody = new JSONObject();
        try
        {
            jsonBody.put("order_amount", "1");
            jsonBody.put("customer_id", input1+"");
            jsonBody.put("order_id", oId+"");
            jsonBody.put("order_note", "Subscription");
            jsonBody.put("customer_email", "omyadav04352@gmail.com");
            jsonBody.put("customer_phone", "9301982112");
            Log.d("body", "httpCall_collect: "+jsonBody);
        }
        catch (Exception e)
        {

        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cashfree-server-production.up.railway.app/api/create-order";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        // enjoy your response
                        String token=response.optJSONObject("response").optString("order_token");

                        Log.e("response", response.optJSONObject("response").optString("order_token"));
                        Log.e("response", response.toString());
                        try {
                            CFSession cfSession = new CFSession.CFSessionBuilder()
                                    .setEnvironment(cfEnvironment)
                                    .setOrderToken(token)
                                    .setOrderId(oId+"")
                                    .build();
                            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                                    .setMode(CFUPI.Mode.INTENT)
                                    .setUPIID(package_name) //Google Pay's package name = "com.google.android.apps.nbu.paisa.user"
                                    .build();
                            CFUPIPayment cfupiPayment = new CFUPIPayment.CFUPIPaymentBuilder()
                                    .setSession(cfSession)
                                    .setCfUPI(cfupi)
                                    .build();
                            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
                            gatewayService.doPayment(MainActivity.this, cfupiPayment);
                            gatewayService.setCheckoutCallback(new CFCheckoutResponseCallback() {
                                @Override
                                public void onPaymentVerify(String s) {
                                    Log.e("sunna bhai hogya",s+" hogya");
                                }

                                @Override
                                public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
                                    Log.e("dekhle bhai ",s+"");
                                    Log.e("ok bhai ",cfErrorResponse.getCode()+"");
                                }
                            });
                        } catch (CFException exception) {
                            exception.printStackTrace();
                        }

                        Log.e("response",token);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ","Wrong");
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) {
            }
        });
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);
    }
    public boolean checkApplication(String packageName) {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null) {
            // not installed
            return false;
        } else {
            return true;
            // Installed
        }
    }
}