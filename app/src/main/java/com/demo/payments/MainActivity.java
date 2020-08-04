package com.demo.payments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
Button tv,mobile,eb,internet,water,valid,cancel;
LinearLayout payment,buttons;
String state;
EditText amntET;
TextView type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        Checkout.preload(getApplicationContext());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.GONE);
                buttons.setVisibility(View.VISIBLE);
            }
        });

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,description,value;
                value = amntET.getText().toString().trim();

                switch (state){
                    case "tv":
                        name = "TV bill";
                        description = "TV  Payments";
                        startPayment(name,description,value);
                        break;
                    case "mobile":
                        name = "Mobile Recharge";
                        description = "Mobile Recharge Process";
                        startPayment(name, description, value);
                        break;
                    case "eb":
                        name = "Electricity Bill";
                        description = "EB Bill Payments";
                        startPayment(name, description, value);
                        break;
                    case "internet":
                        name = "Internet Recharge";
                        description = "Internet Recharge Process";
                        startPayment(name, description, value);
                        break;
                    case "water":
                        name = "Water Bill";
                        description = "Water Bill Payments";
                        startPayment(name, description, value);
                        break;
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.GONE);
                state = "tv";
                type.setText("TV Recharge");
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.GONE);
                state = "mobile";
                type.setText("Mobile Recharge");
            }
        });
        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.GONE);
                state = "internet";
                type.setText("Internet Recharge");
            }
        });
        eb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.GONE);
                state = "eb";
                type.setText("Electricity Bills");
            }
        });
        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.GONE);
                state = "water";
                type.setText("Water Bills");
            }
        });
    }
    public void startPayment(String name, String description, String value) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_NhnGcDw9JaXHvU");

        /**
         * Set your logo here
         */
//        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", description);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", value);//pass amount in currency subunits
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact","9988776655");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("MainActivity", "Error in starting Razorpay Checkout", e);
        }
    }
    private void initUI() {
        tv = findViewById(R.id.tv_btn);
        mobile = findViewById(R.id.mobile_btn);
        eb = findViewById(R.id.eb_btn);
        internet = findViewById(R.id.internet_btn);
        water = findViewById(R.id.water_btn);
        valid = findViewById(R.id.valid_btn);
        cancel = findViewById(R.id.cancel_btn);

        payment = findViewById(R.id.payment);
        buttons = findViewById(R.id.buttons);

        amntET = findViewById(R.id.amnt_val);
        type = findViewById(R.id.type);
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"Payment Successfull",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment Failed "+s,Toast.LENGTH_SHORT).show();
    }
}