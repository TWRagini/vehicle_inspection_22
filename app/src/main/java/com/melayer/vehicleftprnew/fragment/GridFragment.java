package com.melayer.vehicleftprnew.fragment;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.melayer.vehicleftprnew.DatabaseOperation;
import com.melayer.vehicleftprnew.InspectionActivity;
import com.melayer.vehicleftprnew.MyLogger;
import com.melayer.vehicleftprnew.R;
import com.melayer.vehicleftprnew.activity.MainActivity;
import com.melayer.vehicleftprnew.database.repository.RepoImplLogin;
import com.melayer.vehicleftprnew.database.repository.RepoImplModule;
import com.melayer.vehicleftprnew.database.repository.RepoImplRegisterVehicle;
import com.melayer.vehicleftprnew.database.repository.RepoLogin;
import com.melayer.vehicleftprnew.database.repository.RepoModule;
import com.melayer.vehicleftprnew.database.repository.RepoRegisterVehicle;
import com.melayer.vehicleftprnew.domain.Module;
import com.melayer.vehicleftprnew.domain.VehicleRegistration;
import com.melayer.vehicleftprnew.prefs.Prefs;
import com.melayer.vehicleftprnew.web.Connectable;
import com.melayer.vehicleftprnew.web.JsonMan;
import com.melayer.vehicleftprnew.web.NetworkUtils;
import com.melayer.vehicleftprnew.web.Url;
import com.melayer.vehicleftprnew.web.Ws;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

import javax.security.auth.login.LoginException;

/**
 * Created by root on 10/9/16.
 */
public class GridFragment extends Fragment {

    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    private UploadDataServer uploadDataServer;
    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;
    ProgressDialog pd;
    DatabaseOperation databaseOperation;
    String userName;
    int i = 0;
    String outPut = "";

    String text;
    //EditText editText;
    String vDB = Environment.getExternalStorageDirectory().getPath() + "/VehicleFTPRDB/VehicleInfo.db";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = NetworkUtils.getConnectivityStatusResult(context);
            Toast.makeText(context, status, Toast.LENGTH_SHORT);
            if (getParent().isNetworkAvailable())
                uploadData();
        }
    };

    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putString(KEY_FRAGMENT_NAME, "GridFragment");
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadDataServer = new UploadDataServer();

    }


    @Override
    public void onResume() {
        super.onResume();
        getParent().registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        getParent().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_gird_frag, container, false);
        pd = new ProgressDialog(getContext());
        pd.setTitle("We are validating to user");
        pd.setMessage("Please wait....");


        databaseOperation = new DatabaseOperation(getContext());
        File fileDB = new File(vDB);
        if (fileDB.exists()) {
            databaseOperation.deleteUser(fileDB);
        }

        // editText = rootView.findViewById(R.id.et);
        databaseOperation = new DatabaseOperation(getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        initLinearLayout(rootView);
        getParent().hideKeyboard(rootView);
        uploadData();
        return rootView;
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

    private void uploadData() {
        RepoModule repoModule = new RepoImplModule(getContext(), getParent().getDbHelper());
        try {
            Integer count = repoModule.checkDataInspections();
            RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());

            Log.i(MainActivity.TAG, "count - " + count + "Prefs.getKeyVehicleNo(getContext()) : " + Prefs.getKeyVehicleNo(getContext()));
            if (getParent() != null) {
                if (getParent().isNetworkAvailable()) {

                    uploadDataServer.registerVehicle();
                    if (count > 0) {//needs to check
                        MeTaskUploadVehicleData taskUploader = new MeTaskUploadVehicleData(getContext());
                        taskUploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initLinearLayout(final View rootView) {
        requestQueue = Volley.newRequestQueue(getContext());
        rootView.findViewById(R.id.layoutInspection).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //text = editText.getText().toString();
                userName = databaseOperation.retrieveUser();
                Log.e("InspectionText", "initLinearLayout: " + userName);
                // String urlToInspection ="http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VD,salman@twtech.in&format=json";
                //String urlToInspection = "http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VD,r_rajawat@twtech.in&format=json";
                String urlToInspection = "http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VD," + userName + "&format=json";
                urlToInspection = urlToInspection.replaceAll(" ","%20");
                ///////////////////
                pd.show();
                try {
                    jsonarrayRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            //"http://twtech.in:8080/MyInput/rest/MyInputWebService?Data=VD," + userName + "&format=json",
                            urlToInspection,
                            new JSONArray(),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        pd.dismiss();
                                        Log.e("Respooonse", String.valueOf(response));
                                        for (int i = 0; i < response.length(); i++) {
                                            // new MyLogger().storeMassage("Registration URL ","http://twtech.in:8080/GPSStatusMobileeye/rest/UserRegistration?FirstName=" + urlUsername + "&LastName=" + urlLastName + "&Username=" + urlUsername + urlLastName + "&EmailId=" + urlEmail + "&MobileNo=" + urlMobile + "&Address=" + urlAddress + "&CompanyCode=" + urlCompCode + "&imeiNo=" + imeiNo + "&OTP=" + OTP + "&format=json");
                                            JSONObject getDatabject = response.getJSONObject(i);
                                            String vehicleDetails = getDatabject.getString("VehRegno,VehCode");
                                            String[] parts = vehicleDetails.split(",");
                                            String vehicleNo = parts[0];
                                            String vehicleCode = parts[1];
                                            Log.e("VehiclDetails", "onResponse: " + vehicleDetails);
                                            Log.e("VehiclNo", vehicleNo);
                                            Log.e("VehiclCode", vehicleCode);
                                            try {
                                                // File databaseExist = getContext().getDatabasePath(vDB);
//                                                File fileNm = new File(vDB);
//                                                if (fileNm.exists()) {
//
//                                                    new DatabaseOperation(getContext()).updateOTP2(vehicleNo,vehicleCode);
//                                                    Log.e("vehicleDeatils1", vehicleCode+","+vehicleCode);
//                                                } else {
                                                new DatabaseOperation(getContext()).vehicleDetail(vehicleNo, vehicleCode);
                                                Log.e("vehicleDeatils2", vehicleNo + "," + vehicleCode);
                                                //}
                                            } catch (Exception e) {
                                                Log.e("Exception ", "While Inserting data  : " + e.getMessage());

                                            }
                                            // getParent().runFragmentTransaction(R.id.frameMainContainer, VehicleRegisterFragment.newInstance());
                                            getParent().runFragmentTransaction(R.id.frameMainContainer, Home_fragment.newInstance());
//                                            Intent intent = new Intent(getContext(), InspectionActivity.class);
//                                            startActivity(intent);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Exception while 2", "Sending data to server : ");
                                    new MyLogger().storeMassage("getSentUserData", "Exception : " + error.toString());
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
                ////////////////////////


            }
        });

        ImageView imageView = (ImageView) getParent().findViewById(R.id.lgt);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  RepoLogin repoLogin = new RepoImplLogin(getParent().getDbHelper());
                SharedPreferences sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Sure to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                getParent().runFragmentTransaction(R.id.frameMainContainer, LoginFragment.newInstance());
                                // dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.dismiss();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();

            }
        });
        rootView.findViewById(R.id.layoutUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.TAG, "Uploading Starts");
                RepoModule repoModule = new RepoImplModule(getContext(), getParent().getDbHelper());
                try {
                    Integer count = repoModule.checkDataInspections();
                    Log.i(MainActivity.TAG, "count - " + count + "Prefs.getKeyVehicleNo(getContext()) : " + Prefs.getKeyVehicleNo(getContext()));
                    if (getParent().isNetworkAvailable()) {
                        if (count > 0) {
                            uploadDataServer.registerVehicle();
                        } else {
                            getParent().snack(rootView, "No Data available to Upload:(");
                        }
                    } else {
                        getParent().snack(rootView, "Unable to connect to the server:(");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class UploadDataServer implements Connectable {
        private void registerVehicle() {
            final JSONObject obj = new JSONObject();
            RepoLogin repoLogin = new RepoImplLogin(getParent().getDbHelper());
            RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());
            String vehicleNo = new String();
            List<VehicleRegistration> registrationList = new ArrayList<>();
            try {
                registrationList = repoRegisterVehicle.uploadVehicleDataToServer(repoLogin.getUserId());//Change from upload method to selectAll
                Log.i(MainActivity.TAG, "registrationList);" + registrationList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (VehicleRegistration registration : registrationList) {
                if (registration != null) {
                    vehicleNo = registration.getVehicleNo();
                    String urlToUploadVehicle = Url.URL_REGISTER_VEHICLE
                            + "?vehicleId=" + registration.getVehicleId().trim().replace(" ", "%20")
                            + "&vehicleNo=" + registration.getVehicleNo().trim().replace(" ", "%20")
                            + "&psrName=" + registration.getPsrName().trim().replace(" ", "%20")
                            + "&cellNumber=" + registration.getCellNumber().trim().replace(" ", "%20")
                            + "&startPlace=" + registration.getStartPlace().trim().replace(" ", "%20")
                            + "&endPlace=" + registration.getEndPlace().trim().replace(" ", "%20")
                            + "&licenceNo=" + registration.getLicenceNo().trim().replace(" ", "%20")
                            + "&licenceValidity=" + registration.getLicenceValidity().trim().replace(" ", "%20")
                            + "&tdmAsmName=" + registration.getTdmAsmName().trim().replace(" ", "%20")
                            + "&tdmAsm=" + registration.getTdmAsm().trim().replace(" ", "%20")
                            + "&entryDate=" + registration.getEntryDate().trim().replace(" ", "%20")
                            + "&distributor=" + registration.getDistributor().trim().replace(" ", "%20")
                            + "&userId=" + registration.getUserId();
                    Log.i(MainActivity.TAG, "registration url - " + urlToUploadVehicle);
                    final String finalVehicleNo = vehicleNo;
                    Ws.getQueue(getContext()).add(new JsonObjectRequest(com.android.volley.Request.Method.POST, urlToUploadVehicle,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response != null) {
                                        if (getParent() != null) {
                                            RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());
                                            try {
                                                repoRegisterVehicle.updateFlag(finalVehicleNo);
                                                repoRegisterVehicle.deleteTableData();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            //uploadModuleDataToServer();
                                            //second web service
                                            // getParent().runFragmentTransaction(R.id.frameMainContainer, CheckListFragment.newInstance());
                                        }
                                    }
                                }
                            },
                            new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(MainActivity.TAG, error.toString());
                                    if (getView() != null)
                                        getParent().snack(getView(), "Internet not available!!");
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        {
                            setRetryPolicy(new RetryPolicy() {
                                @Override
                                public int getCurrentTimeout() {
                                    return DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48;
                                }

                                @Override
                                public int getCurrentRetryCount() {
                                    return 0;
                                }

                                @Override
                                public void retry(VolleyError error) throws VolleyError {
                                    //if (getView() != null)
                                    //getParent().snack(getView(), "Internet not available!");
                                }
                            });
                        }
                    });
                }


            }
        }
    }

    public class MeTaskUploadVehicleData extends AsyncTask<Void, Void, Void> {
        private Context context;
        private ProgressDialog progressDialog;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        int id = 1;
        private UploadAllModule uploadAllModule;

        public MeTaskUploadVehicleData(Context context) {
            this.context = context;
            uploadAllModule = new UploadAllModule();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressDialog = ProgressDialog.show(parentActivity, "Inspection Data", "saving inspection data");
            if (getView() != null)
                getView().findViewById(R.id.layoutUpload).setClickable(false);
            mNotifyManager = (NotificationManager) getParent().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getParent());
            mBuilder.setContentTitle("Upload Transworld Data")
                    .setContentText("Upload in progress")
                    .setSmallIcon(R.drawable.ic_file_upload);
            // new MeTaskUploader().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // uploadModule();
                uploadAllModule.saveVehicleDataToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressDialog.dismiss();

            if (getView() != null)
                getView().findViewById(R.id.layoutUpload).setClickable(true);
            mBuilder.setContentText("Upload complete");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        private final class UploadAllModule implements Connectable {
            public Map<String, Object> saveVehicleDataToServer() throws Exception {
                Map<String, Object> mapEntity = new HashMap<>();
                // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(uploadContactToServer()));
                String vehicleNo = null;
                String chkPtName = null;

                Set<Module> moduleList = uploadModuleDataToServer();
                for (Module module : moduleList) {
                    String checkPointUrl = ("http://103.241.181.36:8080/FTPREntry/rest/FTPREntry?entryDate=" + module.getEntryDate() + "&vehicleId=" + module.getVehicleId() + "&checkPointId=" + module.getCheckPointId() + "&userId=" + module.getUserId() + "&unit=" + module.getUnit() + "&vehicleNo=" + module.getVehicleNo() + "&checkPointStatus=" + module.getCheckPointStatus() + "&checkPointName=" + module.getCheckPointName() + "&flag=yes&imagePath=null&remarks=" + module.getRemarks() + "&finalinspectionstatus=" + module.getFinalInspectionStatus() + "&format=json").replaceAll(" ","%20");
                    Log.e("checkPointUrl", " "+ checkPointUrl );


                    //  "103.241.181.36:8080/FTPREntry/rest/FTPREntry?entryDate="+module.getEntryDate()+"&vehicleId="+module.getVehicleId()+"&checkPointId="+module.getCheckPointId()+"&userId="+module.getUserId()+"&unit="+module.getUnit()+"&vehicleNo="+module.getVehicleNo()+"&checkPointStatus="+module.getCheckPointStatus()+"&checkPointName="+module.getCheckPointName()+"&flag=yes&imagePath=null&remarks="+module.getRemarks()+"&finalinspectionstatus="+module.getFinalInspectionStatus()+"&format=json"


                    //"103.241.181.36:8080/FTPREntry/rest/FTPREntry?entryDate=2020-01-01&vehicleId=120777&checkPointId=7&userId=8&unit=test&vehicleNo=MH12mk4587&checkPointStatus=yes&checkPointName=test&flag=yes&imagePath=hghghg&remarks=hhh&finalinspectionstatus=fail&&format=json"

                    try {
                        final JsonArrayRequest jsonarrayRequest = new JsonArrayRequest(
                                Request.Method.GET,
                                //"http://twtech.in:8080/VehSummary/rest/VehicleDetails?Username=ubaidullahkhan@bddhalla.com&Password=1gjoQspE&VehCode=11437&LastRec=20&format=json\n",
                                //"http://103.241.181.36:8080/FTPREntry/rest/FTPREntry?entryDate=" + module.getEntryDate() + "&vehicleId=" + module.getVehicleId() + "&checkPointId=" + module.getCheckPointId() + "&userId=" + module.getUserId() + "&unit=" + module.getUnit() + "&vehicleNo=" + module.getVehicleNo() + "&checkPointStatus=" + module.getCheckPointStatus() + "&checkPointName=" + module.getCheckPointName() + "&flag=yes&imagePath=null&remarks=" + module.getRemarks() + "&finalinspectionstatus=" + module.getFinalInspectionStatus() + "&format=json",
                                checkPointUrl,
                                new JSONArray(),
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        //new MyLogger().storeMassage(Tag + " : Url -", "http://twtech.in:8080/VehSummary/rest/VehicleDetails?Username=" + emailId + "&Password=" + mobileNo + "&VehCode=" + vehCode + "&LastRec=" + dttt + "&format=json\n");
                                        try {
                                            for (int i = 0; i < response.length(); i++) {
                                                JSONObject objectjsn = response.getJSONObject(i);
                                                outPut = objectjsn.getString("output");
                                                Log.e("Response", "onResponse: " + outPut );
                                            }
                                        } catch (JSONException e) {
                                            Log.e("Exception ", " : " + e.getMessage());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Exception ", " : " + error.getMessage());
                                    }
                                }
                        );
                        requestQueue.add(jsonarrayRequest);

                    } catch (Exception e) {
                        //new MyLogger().storeMassage(Tag,"Exception while updating data "+e.getMessage());
                        Log.e("Exception ", " : " + e.getMessage());
                    }


//                    Log.i(MainActivity.TAG, "Uploading module Data : " + module.toString());
//                    MultipartBody.Builder builder = new MultipartBody.Builder();
//                    vehicleNo = module.getVehicleNo();
//                    builder.addFormDataPart("vehicleId", "" + module.getVehicleId());
//                    builder.addFormDataPart("vehicleNo", module.getVehicleNo());
//                    builder.addFormDataPart("unit", "" + module.getUnit());
//                    builder.addFormDataPart("checkPointId", "" + module.getCheckPointId());
//                    builder.addFormDataPart("checkPointName", module.getCheckPointName());
//                    builder.addFormDataPart("checkPointStatus", module.getCheckPointStatus());
//                    Log.e("login", "saveVehicleDataToServer: "+ module.getCheckPointId()+module.getCheckPointName()+module.getCheckPointStatus() );
//                    builder.addFormDataPart("finalStatus", module.getFinalInspectionStatus());
//                    builder.addFormDataPart("remarks", module.getRemarks()!=null ? module.getRemarks() : "");
//                    List<File> imgList = new ArrayList<>();
//                    Log.i(MainActivity.TAG, "ImagePath in builder - " + module.getImage().toString());
//                    if (module.getImage() != null) {
//                        String[] str = module.getImage().toString().split("/");
//                        String imagePath = str[str.length - 1];
//                        Log.e("ImagePath", "Inside If: "+imagePath);
//                        imgList.add(module.getImage());
//                        builder.addFormDataPart("imagePath", imagePath);
//                        Log.i(MainActivity.TAG, "ImagePath" + imagePath);
//                    } else {
//                        builder.addFormDataPart("imagePath", "");
//                        Log.e("ImagePath", "Inside else: ");
//                    }
//                    for (File file : imgList) {
//                        if (file.exists()) {
//                            builder.addFormDataPart("multipartFile", "" + (file.getName()), RequestBody.create(MediaType.parse("image"), file));
//                        }
//                    }
//                    builder.addFormDataPart("entryDate", module.getEntryDate());
//                    RepoLogin repoLogin = new RepoImplLogin(getParent().getDbHelper());
//                    builder.addFormDataPart("userId", "" + repoLogin.getUserId());
//                    builder.setType(MultipartBody.FORM);
//                    RequestBody body = builder.build();
//                    OkHttpClient client = new OkHttpClient();
//                    okhttp3.Request request = new okhttp3.Request.Builder()
//                            .url(Url.URL_UPLOAD_MODULE)
//                            .post(body)
//                            .addHeader("Accept", "application/json")
//                            .build();
//                    okhttp3.Response response = client.newCall(request).execute();
//                    String responseJson = response.body().string();
                    RepoModule repoModule = new RepoImplModule(getContext(), getParent().getDbHelper());
//                    mapEntity = JsonMan.parseAnything(responseJson, new TypeReference<Map<String, Object>>() {
//                    });
                    mapEntity.put("output", outPut);
                    Log.e("CheckPointResponse", "saveVehicleDataToServer: " + outPut );
                    Log.i(MainActivity.TAG, "Response Entity - " + mapEntity);
                    // String status = mapEntity.get("status").toString();
                    if (outPut.equals("Record Already Available") || outPut.equals("Data insertion Successful")) {
                        // RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());
                        try {
                            repoModule.updateFlag(module.getVehicleNo(), module.getCheckPointName());
                            Log.e("CheckPoint", "InsideTry: "+ module.getVehicleNo() + ","+ module.getCheckPointStatus());
                            repoModule.deleteTableData();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("CheckPoint", "InsideCatch: "+ e.getMessage() );
                        }
                    }
                    }
                    return mapEntity;
                }
            }

            /* public void dialogExitApp() {
                 final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                 builder.setMessage("Sure to Logout?")
                         .setCancelable(false)
                         .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                             public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                  dialog.dismiss();
                             }
                         })
                         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                 dialog.dismiss();
                             }
                         });
                 final AlertDialog alert = builder.create();
                 alert.show();
             }
     */
        /*   //Volley Call showing exception MultipartRequest.getBody: IOException writing to ByteArrayOutputStream
           private void uploadModule() {
               List<Module> moduleList = uploadModuleDataToServer();
               for (Module module : moduleList) {
                   final String vehicleNo = module.getVehicleNo();
                   Ws.getQueue(getParent()).add(new MultipartRequest(Url.URL_UPLOAD_MODULE, module, new com.android.volley.Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           //    pd.dismiss();
                           RepoModule repoModule = new RepoImplModule(getContext(),getParent().getDbHelper());
                           RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());
                           try {
                               repoModule.updateFlag(vehicleNo, module.getCheckPointName());
                               repoRegisterVehicle.deleteTableData();
                               repoModule.deleteTableData();
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                           Log.i(MainActivity.TAG, "Response Json - " + response);
                       }
                   }, new com.android.volley.Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           VolleyLog.v(error.getMessage());
                       }
                   }));
               }
           }
   */
            private Set<Module> uploadModuleDataToServer() {
                Set<Module> moduleList = new HashSet<>();
                RepoModule repoModule = new RepoImplModule(getContext(), getParent().getDbHelper());
                RepoLogin repoLogin = new RepoImplLogin(getParent().getDbHelper());
                try {
                    moduleList = repoModule.uploadModuleData(repoLogin.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return moduleList;
            }
        }
    }
//}