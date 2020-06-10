package com.myoptimind.g8_app.features.uploadtimeslip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myoptimind.g8_app.R;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.models.TimeSlip;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class UploadSlipFragment extends Fragment {

    private static final int TAKE_IMAGE = 500;
    private static final int SELECT_FROM_GALLERY = 600;

    private String imageFromTakeAPicture = "";

    UploadSlipViewModel timeSlipViewModel;

    public static UploadSlipFragment newInstance() {

        Bundle args = new Bundle();

        UploadSlipFragment fragment = new UploadSlipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upload_slip,container,false);

        timeSlipViewModel = new ViewModelProvider(getActivity()).get(UploadSlipViewModel.class);

        View boxTakeAPhoto        = view.findViewById(R.id.box_take_a_photo);
        View boxUploadFromGallery = view.findViewById(R.id.box_upload_from_gallery);
        ImageView imgUpload            = view.findViewById(R.id.img_upload_from_gallery);
        ImageView imgTakeAPhoto        = view.findViewById(R.id.img_take_a_photo);
        TextView lblUpload            = view.findViewById(R.id.label_upload_timeslip);
        TextView lblTakePhoto         = view.findViewById(R.id.label_take_a_photo);

        timeSlipViewModel.getActiveTimeSlip().observe(this.getViewLifecycleOwner(), new Observer<TimeSlip>() {
            @Override
            public void onChanged(TimeSlip timeSlip) {
                if(timeSlip != null){
                    boxTakeAPhoto.setEnabled(false);
                    boxUploadFromGallery.setEnabled(false);
                    imgUpload.setEnabled(false);
                    imgTakeAPhoto.setEnabled(false);
                    lblUpload.setEnabled(false);
                    lblTakePhoto.setEnabled(false);
                }
            }
        });

        boxTakeAPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    File photofile = null;
                    DateTime dt = new DateTime();

                    try{
                        photofile = createImageFile(SharedPref.getInstance(getActivity()).getIdLoggedIn(),dt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(photofile != null){
                        Uri photoUri = FileProvider.getUriForFile(getActivity(),
                                "com.myoptimind.g8_app.fileprovider",
                                photofile
                        );

                        imageFromTakeAPicture = photofile.getAbsolutePath();

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);

                        startActivityForResult(takePictureIntent,TAKE_IMAGE);
                    }
                }
            }
        });

        boxUploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, SELECT_FROM_GALLERY);
                }
            }
        });


        return view;
    }

    private File createImageFile(String userId,DateTime dt){

        String time = dt.toString("MM_dd_yyyy");

        String imageFileName = userId + "_" + time + "_timeslip.jpg";

        File dir = new File(getActivity().getFilesDir(),"time_slip");

        if(!dir.exists()){
            dir.mkdirs();
        }

        return new File(dir, imageFileName);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FROM_GALLERY) {

            if(resultCode == RESULT_OK){
                Bitmap bitmap = null;
                Uri fullPhotoUri = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fullPhotoUri);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

                DateTime dt = new DateTime();
                File imageName = createImageFile(SharedPref.getInstance(getActivity()).getIdLoggedIn(),dt);
                try (FileOutputStream out = new FileOutputStream(imageName)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                } catch (IOException e) {
                    e.printStackTrace();
                }

                timeSlipViewModel.insertTimeSlip(imageName.getAbsolutePath());
                showAlertSuccess();

            }


        }else if(requestCode == TAKE_IMAGE){
            if(resultCode == RESULT_OK){
                timeSlipViewModel.insertTimeSlip(imageFromTakeAPicture);
                showAlertSuccess();
            }
        }
    }

    private void showAlertSuccess(){
        new AlertDialog.Builder(getActivity())
                .setMessage("Timeslip uploaded successfully.")
                .setPositiveButton("Ok",null)
                .create()
                .show();
    }
}
