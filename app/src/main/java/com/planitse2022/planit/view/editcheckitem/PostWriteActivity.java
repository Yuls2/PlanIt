package com.planitse2022.planit.view.editcheckitem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWriteActivity extends AppCompatActivity {
    private static final int FROM_CAMERA = 1, FROM_GALLERY = 2;
    private final int PERMISSIONS_REQUEST_CODE = 100;
    private int groupID;
    private String checklistJSON;
    private String strEncodedImage;
    private Uri cameraImageURI;
    private ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("오늘의 목표 달성 인증");

        strEncodedImage = "no image";
        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", -1);
        checklistJSON = intent.getStringExtra("chJson");

        //뷰 매핑
        ImageView imgPost;
        EditText edtComment;
        Button btnCancel, btnUpload;
        imgPost = findViewById(R.id.img_post);
        edtComment = findViewById(R.id.edt_comment);
        btnCancel = findViewById(R.id.btn_cancel);
        btnUpload = findViewById(R.id.btn_upload);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "작성을 취소할까요?", "작성중이던 내용은 저장되지 않습니다!", "이어서 쓸래요", "상관없어요");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        dialog.dissmissDailog();
                    }
                });
            }
        });

        //이미지 가져오기
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkPermission()) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                intent.setType("image/*

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageFile = null;
                    try {
                        imageFile = generateFile();
                    }catch (IOException e) {

                    }
                    if(imageFile != null) {
                        cameraImageURI = FileProvider.getUriForFile(PostWriteActivity.this, getPackageName() + ".fileprovider", imageFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageURI);
                    }
                    Intent chooseIntent = Intent.createChooser(galleryIntent, "이미지를 가져올 앱을 선택하세요");
                    chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{cameraIntent});

                    resultLauncher.launch(chooseIntent);
                }
            }
        });

//        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Unregister self right away
//                context.unregisterReceiver(this);
//
//                // Component will hold the package info of the app the user chose
//                ComponentName component = intent.getParcelableExtra<ComponentName>(Intent.EXTRA_CHOSEN_COMPONENT);
//                // Do something with component
//            }
//        }

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Bitmap bitmap = null;

                            Uri uri = null;
                            if(result.getData() != null && result.getData().getData() != null) {
                                Intent intent = result.getData();
                                uri = intent.getData();
                            }
                            else {
                                uri = cameraImageURI;
                            }
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .transform(new CenterCrop()
                                            , new RoundedCorners(DPConverter.dpToInt(getApplicationContext(), 10)))
                                    .override(250)
                                    .into(imgPost);
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //bitmap to String
                            if(bitmap != null) {
                                ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                                //bitmap = Bitmap.createScaledBitmap(bitmap, 100,100,true);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStreamObject);
                                byte[] byteArrayBar = byteArrayOutputStreamObject.toByteArray();
                                strEncodedImage = Base64.encodeToString(byteArrayBar, Base64.DEFAULT);
                            }
                        }
                    }
                });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strEncodedImage.equals("no image")) {
                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "업로드 실패", "인증샷을 넣어주셔야 합니다!");
                    dialog.show();
                }
                else {
                    TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "업로드 할까요?", "이대로 작성을 완료하시겠습니까?");
                    dialog.show();
                    dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UserData userData = UserData.getInstance();
                            RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                            retrofitAPI.uploadPost(groupID, checklistJSON, edtComment.getText().toString(), strEncodedImage).enqueue(new Callback<ResponseData<Integer>>() {
                                @Override
                                public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                    ResponseData<Integer> responseData = response.body();
                                    if (responseData.getResult() == 0) {
                                        finish();
                                    }
                                    else {
                                        OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "업로드 실패"
                                                , "오류 코드 " + responseData.getResult()
                                                + "\n" + responseData.getMessage());
                                        dialog.show();
                                    }
                                    dialog.dissmissDailog();
                                }

                                @Override
                                public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "업로드 실패", t.toString());
                                    dialog.show();
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private File generateFile() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String filename = "planitPicture_" + dateFormat.format(System.currentTimeMillis());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                filename,
                ".jpg",
                storageDir
        );

        return image;
    }

    public boolean chkPermission() {
        boolean mPermissionsGranted = false;
        // 권한 목록
        String[] mRequiredPermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 필수 권한을 가지고 있는지 확인한다.
            mPermissionsGranted = hasPermissions(mRequiredPermissions);
            // 권한 없으면
            if (!mPermissionsGranted) {
                // 권한 요청
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            mPermissionsGranted = true;
        }

        return mPermissionsGranted;
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSIONS_REQUEST_CODE == requestCode) {
            for (int i : grantResults) {
                //허용
                if (i == PackageManager.PERMISSION_GRANTED) {

                }
                //거부
                else {
                    OneBtnDialog dialog = new OneBtnDialog(PostWriteActivity.this, "사진 가져오기 실패", "권한을 허용해 주셔야 사진을 가져올 수 있습니다.");
                    dialog.show();
                }
            }
        }
    }
}
