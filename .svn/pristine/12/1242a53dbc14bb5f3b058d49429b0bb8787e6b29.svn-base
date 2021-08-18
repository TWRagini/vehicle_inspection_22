package com.melayer.vehicleftp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.melayer.vehicleftp.R;
import com.melayer.vehicleftp.activity.MainActivity;
import com.melayer.vehicleftp.imageCompression.MeTaskImageCompression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 25/8/16.
 */
public class RemarksDialogFragment extends DialogFragment {
    public final static Integer CAMERA_REQUEST_CHECKPOINTS = 1001;
    private String imagePath;
    private OnDismissListener dismissListener;
    public static final int MY_PERMISSIONS_REQUESTS = 6;

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
                if (getTag().equals("Helmet Condition") && isImageCapture()) {
                    dismiss();
                }
                if (isRemarksFilled() && isImageCapture()) {
                    dismiss();
                } else {
                    if (!isImageCapture())
                        getParent().snack(rootView, getResources().getString(R.string.imageError));
                    if (!isRemarksFilled() && !getTag().equals("Helmet Condition"))
                        getParent().snack(rootView, getResources().getString(R.string.remarksError));
                    if (!isImageCapture() && !isRemarksFilled() && !getTag().equals("Helmet Condition"))
                        getParent().snack(rootView, "Image and remarks required!!");
                }
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
                openImageIntent();
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
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA_REQUEST_CHECKPOINTS);
            return;
        }
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CHECKPOINTS);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CHECKPOINTS) {
            //isCamera(data);
            if (resultCode == Activity.RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getParent().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                imagePath = cursor.getString(column_index_data);
                Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);
                getView().findViewById(R.id.imageViewAddPic).setVisibility(View.VISIBLE);
                ((ImageView) getView().findViewById(R.id.imageViewAddPic)).setImageBitmap(rotatedBitmap);
                Log.i(MainActivity.TAG, "Image : " + imagePath);
                MeTaskImageCompression taskImageCompression = new MeTaskImageCompression(getActivity(),imagePath);
                taskImageCompression.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,imagePath);
                getView().findViewById(R.id.imgAddPhoto).setVisibility(View.GONE);
                getView().findViewById(R.id.imageViewAddPic).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.imageClosedTagAddPic).setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnDismissListener {
        void onDismiss(String inspectionTag, String path, String reason);
    }
}
