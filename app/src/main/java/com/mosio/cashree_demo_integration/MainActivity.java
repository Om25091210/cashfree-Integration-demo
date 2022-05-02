package com.mosio.cashree_demo_integration;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

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
    EditText phone,email,amount;
    long oId;
    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone=findViewById(R.id.editTextTextPersonName);
        email=findViewById(R.id.editTextTextPersonName2);
        amount=findViewById(R.id.editTextNumber);

        btn_make_payment=findViewById(R.id.make_payment);
        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initiatepayment();
                httpCall_collect();
            }
        });
        findViewById(R.id.make_payment_upi).setOnClickListener(v->{
            httpCall_intent();
        });

    }

    private void httpCall_collect() {
        oId=System.currentTimeMillis();
        StringBuilder input1 = new StringBuilder();
        input1.append(oId);
        input1.reverse();

        JSONObject jsonBody = new JSONObject();
      try
      {
          jsonBody.put("order_amount", amount.getText().toString());
          jsonBody.put("customer_id", input1+"");
          jsonBody.put("order_id", oId+"");
          jsonBody.put("order_note", "Subscription");
          jsonBody.put("customer_email", email.getText().toString());
          jsonBody.put("customer_phone", phone.getText().toString());
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
    public void httpCall_intent() {
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
                        // enjoy your response
                        String token=response.optJSONObject("response").optString("order_token");

                        Log.e("response", response.optJSONObject("response").optString("order_token"));
                        Log.e("response", response.toString());
                        checkApplication("net.one97.paytm");
                        try {
                            CFSession cfSession = new CFSession.CFSessionBuilder()
                                    .setEnvironment(cfEnvironment)
                                    .setOrderToken(token)
                                    .setOrderId(oId+"")
                                    .build();
                            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                                    .setMode(CFUPI.Mode.INTENT)
                                    .setUPIID("com.google.android.apps.nbu.paisa.user") //Google Pay's package name = "com.google.android.apps.nbu.paisa.user"
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
    public void checkApplication(String packageName) {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null) {
            // not installed
            Log.e("Not installed","NOT");
        } else {
            Log.e("installed","Yes");
            // Installed
        }
    }
}