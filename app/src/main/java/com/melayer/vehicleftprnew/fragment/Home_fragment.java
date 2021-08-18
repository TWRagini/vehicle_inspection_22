package com.melayer.vehicleftprnew.fragment;

import android.app.AlertDialog;
import android.app.Person;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

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
import com.melayer.vehicleftprnew.DatabaseOperation;
import com.melayer.vehicleftprnew.InspectionActivity;
import com.melayer.vehicleftprnew.MyLogger;
import com.melayer.vehicleftprnew.R;
import com.melayer.vehicleftprnew.prefs.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.DataTruncation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home_fragment extends Fragment implements AdapterView.OnItemSelectedListener{
    Button button;

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
    String vehicleCode = "";

    Map<String, String> peopleByForename ;
    CheckListFragment fr;

    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";


    FrameLayout frameLayout;




    DatabaseOperation databaseOperation;

    public static Home_fragment newInstance() {
       Home_fragment  fragment = new Home_fragment();
        Bundle args = new Bundle();
        args.putString(KEY_FRAGMENT_NAME, "Home_Fragment");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      View myview=  inflater.inflate(R.layout.fragment_home,container,false);

        requestQueue= Volley.newRequestQueue(getContext());
        peopleByForename = new HashMap<>();
        pd = new ProgressDialog(getContext());
        pd.setTitle("We are validating to user");
        pd.setMessage("Please wait....");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(cal.getTime());

        tv1 = (TextView) myview.findViewById(R.id.tv1);
        tv2 = (TextView) myview.findViewById(R.id.tv2);
        tv3 = (TextView) myview.findViewById(R.id.tv3);
        tv4 = (TextView) myview.findViewById(R.id.tv4);
        btn2= (Button) myview.findViewById(R.id.btn2);

       // frameLayout = (FrameLayout) findViewById(R.id.frameMainContainer);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date current = format.parse(currentDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        Log.e("Inspection", "date: " + currentDate );

        databaseOperation = new DatabaseOperation(getContext());

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


        spinner= (Spinner) myview.findViewById(R.id.spinner);
        editText= (EditText) myview.findViewById(R.id.ed_text);
        btn= (Button) myview.findViewById(R.id.btn);

        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,retrieveData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
      //  Prefs.saveVehicleNo(getContext(),str);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ed_text=editText.getText().toString().trim();
                vehicleCode=databaseOperation.retriveVehicleCode(str);
                ////////////////////////
                String urlToInspection = ("http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VMS,"+str+","+vehicleCode+","+ed_text+"&format=json").replaceAll(" ","%20");

                //pd.show();
                try {
                  //  String urlToInspectionX = urlToInspection.replaceAll(" ", "%20");
                  // String urlToInspectionX =  urlToInspection.replaceAll("\\s+","");
                   // urlToInspection = urlToInspection.trim();
                    jsonarrayRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            //"http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VMS,Ragini%20Tomar,13171,MH123456789-09&format=json",
                           // "http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VMS,"+str+",13171,"+ed_text+"&format=json",
                            urlToInspection,
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
                                            tv1.setVisibility(View.VISIBLE);
                                            tv2.setVisibility(View.VISIBLE);
                                            tv3.setVisibility(View.VISIBLE);
                                            tv4.setVisibility(View.VISIBLE);



                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date dateCurrent = format.parse(currentDate);
                                                Date licenseDate = format.parse(licenseexpdate);
                                                Date pucDate = format.parse(PUCvaliditydate);
                                                Date insuranceDate = format.parse(insurancevaliditydate);
                                                Date rcDate = format.parse(RCvaliditydate);


                                                /////////////
                                                 // populate it
                                                 //List<Person> people = new ArrayList<>(); people.add(new Person("Bob Smith")); people.add(new Person("Bob Jones")); peopleByForename.put("Bob", people);
                                                ///////////

                                                if (dateCurrent.after(licenseDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                    //Toast.makeText(getContext(), "Invalid license", Toast.LENGTH_SHORT).show();
                                                   // tv1.setVisibility(View.VISIBLE);
                                                    tv1.setText("Invalid License");

                                                    peopleByForename.put("License Validity","No");

                                                }
                                                else {
                                                    //tv1.setVisibility(View.VISIBLE);
                                                    tv1.setTextColor(Color.GREEN);
                                                    tv1.setText("Valid License");
                                                    peopleByForename.put("License Validity","Yes");
                                                }
                                                if (dateCurrent.after(pucDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                   // Toast.makeText(getContext(), "Invalid license", Toast.LENGTH_SHORT).show();
                                                   // tv2.setVisibility(View.VISIBLE);
                                                    tv2.setText("Invalid PUC");

                                                    peopleByForename.put("PUC Validity","No");

                                                }
                                                else {
                                                   // tv2.setVisibility(View.VISIBLE);
                                                    tv2.setTextColor(Color.GREEN);
                                                    tv2.setText("Valid PUC");
                                                    peopleByForename.put("PUC Validity","Yes");
                                                }
                                                if (dateCurrent.after(insuranceDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                   // Toast.makeText(getContext(), "Invalid license", Toast.LENGTH_SHORT).show();
                                                    //tv3.setVisibility(View.VISIBLE);
                                                    tv3.setText("Invalid Insurance");

                                                    peopleByForename.put("Insurance Validity","No");

                                                }
                                                else {
                                                   // tv3.setVisibility(View.VISIBLE);
                                                    tv3.setTextColor(Color.GREEN);
                                                    tv3.setText("Valid Insurance");
                                                    peopleByForename.put("Insurance Validity","Yes");
                                                }

                                                if (dateCurrent.after(rcDate))
                                                {
                                                    Log.e("Date status", licenseexpdate);
                                                   // Toast.makeText(getContext(), "Invalid license", Toast.LENGTH_SHORT).show();
                                                   // tv4.setVisibility(View.VISIBLE);
                                                    tv4.setText("Invalid RC");

                                                    peopleByForename.put("RC Validity","No");

                                                }
                                                else
                                                {
                                                   // tv4.setVisibility(View.VISIBLE);
                                                    tv4.setTextColor(Color.GREEN);
                                                    tv4.setText("Valid RC");
                                                    peopleByForename.put("RC Validity","Yes");
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }



                                        }

                                        btn2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                //////////
                                               // Replace with your Fragment class
                                                fr = new CheckListFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("hashmap", (Serializable) peopleByForename);
                                                fr.setArguments(bundle);
                                                Log.e("Bundle data1 ", peopleByForename.toString() );
                                                Log.e("Bundle data2 ", bundle.toString() );

                                                //Log.e("Bundle data3", String.valueOf(fr.builder));
                                                /////////

                                                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMainContainer,fr).commit();



//                                                Fragment fragment = new Fragment(); // replace your custom fragment class
//                                                Bundle b = new Bundle();
//                                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                                                bundle.putString("key","value"); // use as per your need
//                                                fragment.setArguments(b);
//                                                fragmentTransaction.addToBackStack(null);
//                                                fragmentTransaction.replace(viewID,fragment);
//                                                fragmentTransaction.commit();

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
                                    Log.e("Exception while 2", "Sending data to server : " + error.toString());
                                    new MyLogger().storeMassage("getSentUserData","Exception : "+error.toString());
                                    //Toast.makeText(getApplicationContext(), "Response REcieved @@ ", Toast.LENGTH_SHORT).show();
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        Toast.makeText(getContext(), "Error Network Timeout", Toast.LENGTH_SHORT).show();
                                        Log.e("Time Out Error", String.valueOf(error instanceof TimeoutError));
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                        Toast.makeText(getContext(), "Authentication Failure Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ServerError) {
                                        //TODO
                                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof NetworkError) {
                                        //TODO
                                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                    } else if (error instanceof ParseError) {
                                        //T
                                        Toast.makeText(getContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                /////////////////////
                Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
                Prefs.saveVehicleNo(getContext(),str);

                 // vehicleCode=databaseOperation.retriveVehicleCode(str);
                  Prefs.saveVehicleCode(getContext(),vehicleCode);

                Log.e("HomeFrag", "Vehicle name, vehicle Code ..." + str +","+vehicleCode );

            }
        });



return myview;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        str=retrieveData[position];


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getContext(), "Nothing is select", Toast.LENGTH_SHORT).show();
    }

}
