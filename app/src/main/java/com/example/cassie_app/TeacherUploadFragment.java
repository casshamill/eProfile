package com.example.cassie_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class TeacherUploadFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_teacher_upload, container, false);
        Button TextUpButton = (Button) view.findViewById(R.id.buttontext);
        Button CameraButton = (Button) view.findViewById(R.id.buttoncamera);
        Button FileButton = (Button) view.findViewById(R.id.buttonfile);
        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        TextUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadText();
            }
        });
        FileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        return view;
    }

    public void openCamera(){
        Intent i = new Intent(getActivity(), PupilListActivity.class);
        i.putExtra("parent", "camera");
        TeacherUploadFragment.this.startActivity(i);
    }

    public void uploadText(){
        System.out.println("Upload clicked");
        //Intent i = new Intent(getActivity(), UploadText.class);
        Intent i = new Intent(getActivity(), PupilListActivity.class);
        i.putExtra("parent", "text");
        TeacherUploadFragment.this.startActivity(i);
    }

    public void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                System.out.println("Path to image: " + imgDecodableString);
                cursor.close();
                Intent i = new Intent(getActivity(), PupilListActivity.class);
                i.putExtra("parent", imgDecodableString);
                TeacherUploadFragment.this.startActivity(i);
            } else {
                Toast.makeText(getActivity(), "You haven't picked an Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
