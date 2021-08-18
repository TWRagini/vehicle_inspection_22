package com.melayer.vehicleftprnew.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melayer.vehicleftprnew.BuildConfig;
import com.melayer.vehicleftprnew.EmailAPI;
import com.melayer.vehicleftprnew.Main2Activity;
import com.melayer.vehicleftprnew.MyLogger;
import com.melayer.vehicleftprnew.R;
import com.melayer.vehicleftprnew.TWsimpleMailSender;
import com.melayer.vehicleftprnew.activity.MainActivity;
import com.melayer.vehicleftprnew.database.repository.RepoImplLogin;
import com.melayer.vehicleftprnew.database.repository.RepoImplRegisterVehicle;
import com.melayer.vehicleftprnew.database.repository.RepoLogin;
import com.melayer.vehicleftprnew.database.repository.RepoRegisterVehicle;
import com.melayer.vehicleftprnew.domain.Module;
import com.melayer.vehicleftprnew.imageCompression.MeTaskImageCompression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 25/8/16.
 */
public class RemarksDialogFragment extends DialogFragment {
    public final static Integer CAMERA_REQUEST_CHECKPOINTS = 102;
    private String imagePath;
    private OnDismissListener dismissListener;
    public static final int MY_PERMISSIONS_REQUESTS = 6;
    String bodyDetails, subjectline;
    android.content.Context Context;
    private Uri fileUri;

    static final int CAPTURE_IMAGE_REQUEST = 1;
    String odometerpicturePath1;
    //String PATH1;


    public static RemarksDialogFragment getInstance() {
        RemarksDialogFragment dialogFragment = new RemarksDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    public void setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_remarks, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initImageView(rootView);
        initTextView(rootView);

        getDialog().setCanceledOnTouchOutside(false);
        if (getTag().equals("Helmet Condition"))
            ((EditText) rootView.findViewById(R.id.edtRemarks)).setHint(getResources().getString(R.string.remark));
        else
            ((EditText) rootView.findViewById(R.id.edtRemarks)).setHint(getResources().getString(R.string.remarks));
        initButton(rootView);
        return rootView;
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

    private void initButton(final View rootView) {
        rootView.findViewById(R.id.btnCloseDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTag().equals("Helmet Condition") && isRemarksFilled()) {
                    dismiss();
                }
                //if (isRemarksFilled() && isImageCapture()) {
                if (isRemarksFilled()) {
                    dismiss();
                } else {
                    if (!isImageCapture() && !getTag().equals("Helmet Condition"))
                        getParent().snack(rootView, getResources().getString(R.string.imageError));
                    if (!isRemarksFilled())
                        getParent().snack(rootView, getResources().getString(R.string.remarksError));
//                    if (!isImageCapture() && !isRemarksFilled() && !getTag().equals("Helmet Condition"))
//                        getParent().snack(rootView, "Image and remarks required!!");
                }
                String SENDMAIL = " image:" + imagePath;
                new message1(SENDMAIL).execute();
                //new MailAction().execute();
                Log.e("SUbmit ", "Called1");
                //new MailAction().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,hostpop3, portpop3, userNamepop3, passwordpop3, subpop3);
                // new MailAction().execute(hostpop3, portpop3, userNamepop3, passwordpop3, subpop3);
                Log.e("b5555", "validation");

            }
        });
    }

    private boolean isRemarksFilled() {
        return (!((EditText) getView().findViewById(R.id.edtRemarks)).getText().toString().isEmpty());
    }

    private boolean isImageCapture() {
        return imagePath != null;
    }

    private void initTextView(View rootView) {
        ((TextView) rootView.findViewById(R.id.textHeader)).setText(getTag());
    }

    private void initImageView(View rootView) {
        rootView.findViewById(R.id.imgAddPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //askCameraPermissions();
                openImageIntent();
               // selectImageforServicebill();
                Log.e("checkpint1", "check1");
            }
        });


        rootView.findViewById(R.id.imageClosedTagAddPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = null;
                ((ImageView) getView().findViewById(R.id.imageViewAddPic)).setImageBitmap(null);
                getView().findViewById(R.id.imageViewAddPic).setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.imageClosedTagAddPic).setVisibility(View.GONE);
                getView().findViewById(R.id.imgAddPhoto).setVisibility(View.VISIBLE);
            }
        });
    }
    private void openImageIntent() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CHECKPOINTS);
            return;

        }
        //selectImageforServicebill();
        selectImageforCheckpoint();
        Log.e("lota", "lata");


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CHECKPOINTS) {
            if (grantResults.length > 0) {
                // checkCameraPresent("1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CHECKPOINTS);
                    Log.e("permi", "perm13");
                }
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss(getTag(), (imagePath != null ? imagePath : ""),
                    ((TextView) getView().findViewById(R.id.edtRemarks)).getText().toString());
        } else {
            getParent().snack(getView(), getResources().getString(R.string.remarksError));
        }
    }
    private void selectImageforCheckpoint() {
        final CharSequence[] options = {"Take Photo"};

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "FTPR" + timeStamp + "_";
                    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera");
                    File image = null;
                    try {
                        image = File.createTempFile(
                                imageFileName,
                                ".png",
                                storageDir
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
                    Log.e("File Capture ", ":" + imagePath);
                    startActivityForResult(takePictureIntent, 1);


                }

    private void takePictureIntentforcheckpoint() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getParent().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            Log.e("filepro", "filepro");
            try {
                photoFile = createImageFileforcheckpoint();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("filepro", "filepro1");
                //Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFileforcheckpoint());
                Log.e("filepro", "filepro2");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
                Log.e("filepro", "filepro2");
            }
        }
    }
    private File createImageFileforcheckpoint() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        Log.e("File Capture ", ":" + imagePath);
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                Bundle extras = data.getExtras();
                // Sample data cast to  thumbnail
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri1(getContext(), imageBitmap);
                //CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI1(tempUri));
                //iv_add_imageodometer.setImageBitmap(imageBitmap);
                getView().findViewById(R.id.imageViewAddPic).setVisibility(View.VISIBLE);
                ((ImageView) getView().findViewById(R.id.imageViewAddPic)).setImageBitmap(imageBitmap);
                Toast.makeText(getContext(), "Image Captured for FTPR", Toast.LENGTH_SHORT).show();
//                Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
//                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
//                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
//                Matrix matrix = new Matrix();
//                matrix.postRotate(90);
//                Bitmap rotatedBitmap = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);
//                getView().findViewById(R.id.imageViewAddPic).setVisibility(View.VISIBLE);
//             ((ImageView) getView().findViewById(R.id.imageViewAddPic)).setImageBitmap(rotatedBitmap);
//                Toast.makeText(getContext(), "Image Captured for FTPR", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Uri getImageUri1(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI1(Uri uri) {
        String path = "";
        if (getParent().getContentResolver() != null) {
            Cursor cursor = getParent().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                imagePath=path;
                Log.e("Servicing",imagePath);
                cursor.close();
            }
        }
        return path;
    }

    public interface OnDismissListener {
        void onDismiss(String inspectionTag, String path, String reason);
    }
    public class message1 extends AsyncTask
        {

            String MAIL;
            public message1(String MAIL)
            {

                this.MAIL = MAIL;
            }

            @Override
            protected Object doInBackground(Object[] params)

            {
                subjectline = "FTPR Checkpoints" ;
               // subjectline = "FTPR Checkpoints:" + " " + imagePath ;
//                bodyDetails = name.trim() + ","
//                        + vehicleId.trim() + ","
//                        + vehicleNo.trim() + ","
//                        + checkPointId.trim() + ","
//                        + checkPointName.trim() + "\n";
//                Log.e("bodydetails", bodyDetails);
//                Log.e("Subjectline", subjectline);

                TWsimpleMailSender mTWsimpleMailSender = new TWsimpleMailSender(Context, "75302", "transworld", "a.mobileeye.in", "2525");

                try
                {

                   // boolean flag = mTWsimpleMailSender.sendMail(subjectline, MAIL, "8805", "ddmyinputsfwd@twtech.in", imagePath);
                    boolean flag = mTWsimpleMailSender.sendMail(subjectline, MAIL, "8805", "ftprimages@twtech.in", imagePath);
                    Log.e("Main2", "doInBackground: "+flag);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Main2", "Insidecatch: "+e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPreExecute()
            {



                Log.i("DD","IN THE PRE EXCECUTE ");

                super.onPreExecute();
            }



    }
}








