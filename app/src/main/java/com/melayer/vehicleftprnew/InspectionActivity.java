package com.melayer.vehicleftprnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.melayer.vehicleftprnew.activity.MainActivity;
import com.melayer.vehicleftprnew.fragment.CheckListFragment;
import com.melayer.vehicleftprnew.fragment.GridFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InspectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private  Spinner spinner;
    private EditText editText;
    private Button btn,btn2;
    TextView tv1,tv2,tv3,tv4;
    private String str="";
    ProgressDialog pd;
   // String[] country;
    String[] country = { "India", "USA", "China", "Japan", "Other"};
    String retrieveData[];
    String currentDate;

    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;


    FrameLayout frameLayout;




    DatabaseOperation databaseOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        pd = new ProgressDialog(getApplicationContext());
        pd.setTitle("We are validating to user");
        pd.setMessage("Please wait....");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(cal.getTime());

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        btn2= (Button) findViewById(R.id.btn2);

        frameLayout = (FrameLayout) findViewById(R.id.frameMainContainer);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date current = format.parse(currentDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        Log.e("Inspection", "date: " + currentDate );

        databaseOperation = new DatabaseOperation(InspectionActivity.this);

       // String retrieveData[] = databaseOperation.RetrieveRegData();
         retrieveData = databaseOperation.RetrieveRegData();
//       String[] string = retrieveData.split(" ");
//       for(int i=0;i<string.length;i++)
//       {
//           System.out.println(string[i]);
//           Log.e("Inspection", "onCreate: "+ string[i] );
//       }


        Log.e("Inspection", "onCreate: " + retrieveData[0] );
        Log.e("Inspection", "onCreate: " + country );


        spinner= (Spinner) findViewById(R.id.spinner);
        editText= (EditText) findViewById(R.id.ed_text);
        btn= (Button) findViewById(R.id.btn);

        spinner.setOnItemSelectedListener(InspectionActivity.this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,retrieveData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String ed_text=editText.getText().toString().trim();
                ////////////////////////

                //pd.show();
                try {
                    jsonarrayRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            "http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VMS,Ragini%20Tomar,13171,8172-NUH-09&format=json",
                            new JSONArray(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        pd.dismiss();
                                        Log.e("Respooonse", String.valueOf(response));
                                        btn2.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject getDatabject = response.getJSONObject(i);
                                            String licenseexpdate = getDatabject.getString("licenseexpdate");
                                            String PUCvaliditydate = getDatabject.getString("PUCvaliditydate");
                                            String insurancevaliditydate = getDatabject.getString("insurancevaliditydate");
                                            String RCvaliditydate = getDatabject.getString("RCvaliditydate");


                                            Log.e("Inspection", "onResponse: " + licenseexpdate );
                                            Log.e("Inspection", "onResponse: " + PUCvaliditydate );

                                            Log.e("Inspection", "onResponse: " + insurancevaliditydate );
                                            Log.e("Inspection", "onResponse: " + RCvaliditydate );

                                            btn.setVisibility(View.INVISIBLE);



                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date dateCurrent = format.parse(currentDate);
                                                Date licenseDate = format.parse(licenseexpdate);
                                                Date pucDate = format.parse(PUCvaliditydate);
                                                Date insuranceDate = format.parse(insurancevaliditydate);
                                                Date rcDate = format.parse(RCvaliditydate);

                                                if (dateCurrent.after(licenseDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                    Toast.makeText(InspectionActivity.this, "Invalid license", Toast.LENGTH_SHORT).show();
                                                    tv1.setVisibility(View.VISIBLE);
                                                    tv1.setText("Invalid License");

                                                }
                                                if (dateCurrent.after(pucDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                    Toast.makeText(InspectionActivity.this, "Invalid license", Toast.LENGTH_SHORT).show();
                                                    tv2.setVisibility(View.VISIBLE);
                                                    tv2.setText("Invalid PUC");

                                                }
                                                if (dateCurrent.after(insuranceDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                    Toast.makeText(InspectionActivity.this, "Invalid license", Toast.LENGTH_SHORT).show();
                                                    tv3.setVisibility(View.VISIBLE);
                                                    tv3.setText("Invalid Insurance");

                                                }
                                                if (dateCurrent.after(rcDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                    Toast.makeText(InspectionActivity.this, "Invalid license", Toast.LENGTH_SHORT).show();
                                                    tv4.setVisibility(View.VISIBLE);
                                                    tv4.setText("Invalid RC");

                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                Toast.makeText(InspectionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }



                                        }

                                        btn2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                //getParent().runFragmentTransactio(R.id.frameMainContainer, GridFragment.newInstance());
                                               // getFragmentManager().beginTransaction().replace(R.id.frameMainContainer, new GridFragment.commit());
                                               // getParent().run .replace(android.R.id.frameMainContainer,GridFragment.newInstance()).commit();
                                                Toast.makeText(InspectionActivity.this, "done", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Exception while 2", "Sending data to server : ");
                                    new MyLogger().storeMassage("getSentUserData","Exception : "+error.toString());
                                    //Toast.makeText(getApplicationContext(), "Response REcieved @@ ", Toast.LENGTH_SHORT).show();
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        Toast.makeText(getApplicationContext(), "Error Network Timeout", Toast.LENGTH_SHORT).show();
                                        Log.e("Time Out Error", String.valueOf(error instanceof TimeoutError));
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                        Toast.makeText(getApplicationContext(), "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ServerError) {
                                        //TODO
                                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof NetworkError) {
                                        //TODO
                                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ParseError) {
                                        //T
                                        Toast.makeText(getApplicationContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
                                        Log.e("Parsing Error", String.valueOf(error instanceof ParseError));
                                    }
                                    pd.dismiss();

                           /* jsonarrayRequest.setRetryPolicy(new RetryPolicy() {
                                @Override
                                public int getCurrentTimeout() {
                                    Log.e("getCurrentTimeout","called");
                                    return 50000;
                                }

                                @Override
                                public int getCurrentRetryCount() {

                                    Log.e("getCurrentTimeout1","called");
                                    return 50000;
                                }

                                @Override
                                public void retry(VolleyError error) throws VolleyError {
                                    Log.e("getCurrentTimeout2","called");
                                }
                            });*/
                                }

                            }
                    );
                    jsonarrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                            50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(jsonarrayRequest);
                } catch (Exception e) {
                    Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                /////////////////////
                Toast.makeText(InspectionActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        str=retrieveData[position];


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing is select", Toast.LENGTH_SHORT).show();
    }

//     MainActivity getParent()
//
//    {
//
//        return (MainActivity) getActivity();
//    }
}