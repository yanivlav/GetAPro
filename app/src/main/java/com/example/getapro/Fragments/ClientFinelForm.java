package com.example.getapro.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getapro.Helpers.FormAdapter;
import com.example.getapro.MyObjects.Form;
import com.example.getapro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClientFinelForm extends Fragment {

    EditText descET;
    TextView tiltleTV, explTV;
    ImageButton galIB, camIB;
    Button spetzBtn;
    ImageView resultIv;
    File photo;
    String path;

    int SELECT_PICTURE = 200;
    final int CAMERA_REQUEST = 1;
    final int WRITE_PERMISSION_REQUEST = 1;

    private String username;
    private String category;
    private String address;
    private String district;

//    FirebaseStorage firebaseStore = FirebaseStorage.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //name of the instance of the Forms table
    DatabaseReference forms_fire = database.getReference("Forms");
    ArrayList<Form> forms_local = new ArrayList<>();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    StorageReference problemImagesRef;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            category= getArguments().getString("category");
            address = getArguments().getString("address");
            district = getArguments().getString("district");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.client_finel_form, container, false);
        descET = view.findViewById(R.id.decriptionET);
        resultIv = view.findViewById(R.id.resultImage);
        tiltleTV = view.findViewById(R.id.nearly_TV);
        explTV = view.findViewById(R.id.expTV);
        galIB = view.findViewById(R.id.galleryBtn);
        camIB = view.findViewById(R.id.camBtn);
        spetzBtn = view.findViewById(R.id.spetzlistBtn);


        forms_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                forms_local.clear();

                if (dataSnapshot.exists()) {
                    path = user.getUid()+"_Form_Number_"+dataSnapshot.getChildrenCount()+".jpg";
                }
                else  {
                    path = user.getUid()+"_Form_Number_0.jpg";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        problemImagesRef = storageReference.child("Problems/" + path);

        spetzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("address",address);
                bundle.putString("category",category);
                bundle.putString("district",district);
                bundle.putString("desc",descET.getText().toString());
                bundle.putInt("pic",2131230849);
                bundle.putString("realImage","Problems/" + path);
                Navigation.findNavController(view).navigate(R.id.action_clientFinelForm_to_spetzList,bundle);

            }
        });

        galIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        camIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                photo = new File(Environment.getExternalStorageDirectory(),user.getUid()+"Form_Number"+(openformes+".jpg"));
//                Uri imageUri = FileProvider.getUriForFile(getActivity(),
//                        "com.example.getapro.provider", //(use your app signature + ".provider" )
//                        photo);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent,CAMERA_REQUEST);
//                path = user.getUid()+"Form_Number"+openformes+".jpg";
//                problemImagesRef = storageReference.child("Problems/" + path);

                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                 Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, CAMERA_REQUEST);
            }
        });

        if(Build.VERSION.SDK_INT>=23) {
            int hasWritePermission = getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWritePermission!= PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION_REQUEST);
            }
            else camIB.setVisibility(View.VISIBLE);
        }
        else camIB.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        StorageReference problemImage = storageReference.child("problemImage.jpg");
        problemImagesRef = storageReference.child("Problems/"+path);


        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
//             bitmap = (Bitmap)data.getExtras().get("data");
//            resultIv.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));

            Bitmap image = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            galIB.setImageResource(R.drawable.gallery_icon);
            camIB.setImageResource(R.drawable.check_icon);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            problemImagesRef.putBytes(byteArray);


            resultIv.setImageBitmap(image);


        }

        if (requestCode == SELECT_PICTURE && resultCode == getActivity().RESULT_OK) {
            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            path = getRealPathFromURI(selectedImageUri);
            if (null != selectedImageUri) {

                // update the preview image in the layout
//                    resultIv.setImageURI(selectedImageUri);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camIB.setImageResource(R.drawable.cam_icon);
                galIB.setImageResource(R.drawable.check_icon);
                resultIv.setImageBitmap(bitmap);

                problemImagesRef.putFile(selectedImageUri);

            }
        }

//        final FirebaseUser user = firebaseAuth.getCurrentUser();

    }

    public String getRealPathFromURI(Uri uri){
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}