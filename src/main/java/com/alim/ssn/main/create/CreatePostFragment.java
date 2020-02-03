/*
package com.alim.ssn.main.create;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alim.ssn.R;
import com.alim.ssn.studentProperties.Stid;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePostFragment extends Fragment {

    private static final int MULTIPLE_PERMISSIONS = 5132;
    private static final int ACTION_PICK_REQUEST_CODE = 1231;
    private static final int PICKFILE_REQUEST_CODE = 1232;
    @BindView(R.id.et_create_post_title)
    EditText titleEt;
    @BindView(R.id.et_create_post_content)
    EditText descEt;
*/
/*    @BindView(R.id.et_create_post_field)
    EditText majorEt;
    @BindView(R.id.et_create_post_grade)
    EditText gradeEt;*//*

    @BindView(R.id.tags_input)
    ChipsInput chipsInput;
    private PdfCreatorImpl pdfCreator;
    private List<byte[]> bytes;
    private List<String> tags;
    private String[] premissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_post, container, false);
        ButterKnife.bind(this, view);
        pdfCreator = new PdfCreatorImpl();
        tags = new ArrayList<>();
        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                tags.add(chip.getLabel());
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                for (int i = 0; i < tags.size(); i++) {
                    if (tags.get(i).equals(chip.getLabel())) {
                        tags.remove(i);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence text) {
                if (text.length() > 1) {
                    if (text.charAt(text.length() - 1) == ' ') {
                        if (text.length() > 3) {
                            for (int i = 0; i < tags.size(); i++) {
                                if (tags.get(i).equals(text.toString())) {
                                    chipsInput.addChip("", "");
                                    chipsInput.removeChipByLabel("");
                                    Toast.makeText(getContext(), "این تگ را قبلا وارد کرده اید.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            chipsInput.addChip(text.toString(), "");
                        } else {
                            Toast.makeText(getContext(), "هر تگ حداقل باید از سه حرف تشکیل شده باشد.", Toast.LENGTH_SHORT).show();
                            chipsInput.addChip(text.toString(), "");
                            chipsInput.removeChipByLabel(text.toString());
                        }


                    }
                }
            }

        });
        return view;
    }

    @OnClick(R.id.save_post)
    public void savePost() {
        String title = titleEt.getText().toString();
        String desc = descEt.getText().toString();

        if (!title.isEmpty() && !desc.isEmpty() && bytes != null) {
            CreatePostPresenterImpl createPostPresenter = new CreatePostPresenterImpl();
            File file = pdfCreator.createPdf(bytes);
            int size=(int)file.length();

            int stId = ((Stid) Objects.requireNonNull(getActivity()).getApplication()).getStId();
            String  token= ((Stid) Objects.requireNonNull(getActivity()).getApplication()).getToken();
            String tagsText="";
            for (int i = 0; i < tags.size(); i++) {
                tagsText+=tags.get(i);

            }
            createPostPresenter.savePostInServer(token, getContext(), title, desc, stId,size,  file,tagsText,  new CreatePostPresenterImpl.OnSavePostComplete() {
                @Override
                public void onSuccess() {
                    titleEt.getText().clear();
                    descEt.getText().clear();
     */
/*               majorEt.getText().clear();
                    gradeEt.getText().clear();*//*

                    tags.clear();
                    chipsInput.removeChipByInfo("");
                    bytes.clear();
                }
            });

        }

    }

    @OnClick(R.id.btn_create_post_upload_image)
    void onUploadClicked() {
        displayListDialog();



    }

    private void displayListDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        TextView title=new TextView(getContext());
        title.setText("انتخاب کنید");
        title.setPadding(10,10,10,10);
        title.setGravity(Gravity.RIGHT);
        builder.setCustomTitle(title);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(), R.layout.layout_dialog_list);
        adapter.add("عکس");
        adapter.add("فایل");
        builder.setCancelable(true);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        uploadImage();
                        break;
                    case 1:
                        uploadFile();
                }
            }
        });

    }

    private void uploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    private void uploadImage(){
        if (checkPermissions()) {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(pick, ACTION_PICK_REQUEST_CODE);
        }

    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : premissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS
                && grantResults.length > 0) {
            String permissionDenied = "";
            for (String per : permissions) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionDenied += "\n" + per;
                }
            }
            if (permissionDenied.length() > 0) {
                Toast.makeText(getContext(), "permission " + permissionDenied + " denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            java.util.List<Uri> uriList = new ArrayList<>();
            bytes = new ArrayList<>();
            if (data.getClipData() != null) {
                Uri uri;

                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    uri = data.getClipData().getItemAt(i).getUri();
                    uriList.add(uri);
                    byte[] byteArray = null;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        //How to load tiles from a large bitmap

                        //convet bitmap to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();


                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byteArray = stream.toByteArray();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bytes.add(byteArray);

                }
            } else {
                Uri uri = data.getData();
                byte[] byteArray = null;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    //How to load tiles from a large bitmap

                    //convet bitmap to byte[]
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byteArray = stream.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes.add(byteArray);

            }






        */
/*    Uri uri = data.getData();
            if (uri == null) {
                Log.e(TAG, "onActivityResult: uri is null");
            }
            file = new File(getRealPathFromUrl(uri));
        }*//*

        }

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri=data.getData();
            String realPath=getRealPathFromUrl(uri);
        }


    }
    private String getRealPathFromUrl(Uri uri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Context applicationContext = getActivity().getApplicationContext();
        Cursor cursor = applicationContext.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}
*/
