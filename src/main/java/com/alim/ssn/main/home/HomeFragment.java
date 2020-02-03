package com.alim.ssn.main.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alim.ssn.R;

import com.alim.ssn.main.create.CreatePost;
import com.alim.ssn.main.create.CreatePostPresenterImpl;
import com.alim.ssn.main.create.JozvaCreatePostDialog;

import com.alim.ssn.main.create.OnJozvaDialogActionClicked;
import com.alim.ssn.main.create.PdfCreatorImpl;
import com.alim.ssn.model.Post;

import com.alim.ssn.studentProperties.Stid;
import com.alim.ssn.webService.ApiClient;
import com.alim.ssn.webService.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment implements MainContract.MainView {
    private static final int ACTION_PICK_REQUEST_CODE = 1231;
    private static final int PICKFILE_REQUEST_CODE = 1232;
    private static final int FILE_TYPE_IMAGE = 0;
    private static final int FILE_TYPE_FILE = 1;
    private static final int FILE_TYPE_NO_FILE = -1;
    private int fileType = FILE_TYPE_NO_FILE;//0->image , 1->file
    private List<byte[]> selectedImageBytes;
    private File selectedFile;
    private String[] premissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int MULTIPLE_PERMISSIONS = 5132;
    private Context mContext;
    private RecyclerView recyclerView1;
    @BindView(R.id.rv_home_posts_list)
    RecyclerView recyclerView;
    @BindView(R.id.fab_main_create_post)
    FloatingActionButton fab;
    @BindView(R.id.main_activity_pb)
    ProgressBar progressBar;
    @BindView(R.id.srl_home_ft)
    SwipeRefreshLayout swipeRefreshLayout;
    private PdfCreatorImpl pdfCreator;
    private JozvaCreatePostDialog dialog;


    public HomeFragment(Context context) {
        this.mContext = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        pdfCreator = new PdfCreatorImpl();

        recyclerView1 = view.findViewById(R.id.rv_home_posts_list);
        PresenterImpl presenter = new PresenterImpl(this, new GetNoticeIntractorImpl());
        presenter.requestGetData(getContext());
        swipeRefreshLayout.setOnRefreshListener(new SwipeListener());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.fab_main_create_post)
     void setFab() {
      /*  Intent intent=new Intent(getActivity(), CreatePost.class);
        startActivity(intent);*/
         dialog = new JozvaCreatePostDialog(getActivity(), new OnJozvaDialogActionClicked() {
            @Override
            public void onImageClicked() {
                Intent pick = new Intent(Intent.ACTION_PICK);
                pick.setType("image/*");
                pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(pick, ACTION_PICK_REQUEST_CODE);
            }

            @Override
            public void onFileClicked() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }

            @Override
            public void onPostClicked(String desc, List<String> tags) {
                savePost(desc, tags);
            }
        });
        if (checkPermissions()) {
            dialog.show();
        }


    }

    private void savePost(String desc, List<String> tags) {

        File fileForUpload = null;
        int size = 0;
        if (fileType == FILE_TYPE_IMAGE) {
            if (selectedImageBytes != null) {
                fileForUpload = pdfCreator.createPdf(selectedImageBytes);
                size = (int) fileForUpload.length();
            }
        } else if (fileType == FILE_TYPE_FILE) {
            fileForUpload = selectedFile;
            size = (int) fileForUpload.length();
        }
        if (!desc.isEmpty()) {
            CreatePostPresenterImpl createPostPresenter = new CreatePostPresenterImpl();

            int stId = ((Stid) Objects.requireNonNull(this).getContext().getApplicationContext()).getStId();
            String token = "Bearer " + ((Stid) Objects.requireNonNull(this).getContext().getApplicationContext()).getToken();
            String tagsText = "";
            for (int i = 0; i < tags.size(); i++) {
                tagsText += tags.get(i);

            }
            createPostPresenter.savePostInServer(token, getContext(), desc, stId, size, fileForUpload, tagsText, new CreatePostPresenterImpl.OnSavePostComplete() {
                @Override
                public void onSuccess() {
                    dialog.cancel();
                    tags.clear();
                    fileType = FILE_TYPE_NO_FILE;
                }
                });

            }
        }

        private boolean checkPermissions () {
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
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
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
        public void showProgress () {

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public void hideProgress () {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void setDataToRecyclerView (List < Post > posts) {
            //save in local storage
            swipeRefreshLayout.setRefreshing(false);
            for (int i = 0; i < posts.size(); i++) {
                try {
                    Timestamp createdAt = convertStringToTimeStamp(posts.get(i).getCreated_at());
                    posts.get(i).setTsCreatedAt(createdAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(posts, new Comparator<Post>() {
                @Override
                public int compare(Post o1, Post o2) {
                    return o2.getTsCreatedAt().compareTo(o1.getTsCreatedAt());
                }
            });
            recyclerView1.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            PostAdapter adapter = new PostAdapter(posts, mContext);
            recyclerView1.setAdapter(adapter);
        }

        private Timestamp convertStringToTimeStamp (String created_at) throws ParseException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date parsedDate = simpleDateFormat.parse(created_at);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            return timestamp;
        }


        @Override
        public void onResponseFailure (Throwable throwable){
            Log.e(TAG, "onResponseFailure: HomeFragment " + throwable.toString());
        }


        class SwipeListener implements SwipeRefreshLayout.OnRefreshListener {

            @Override
            public void onRefresh() {
                PresenterImpl presenter = new PresenterImpl(HomeFragment.this, new GetNoticeIntractorImpl());
                presenter.requestGetData(getContext());
            }
        }


        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ACTION_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                fileType = FILE_TYPE_IMAGE;
                List<Uri> uriList = new ArrayList<>();
                selectedImageBytes = new ArrayList<>();
                if (data.getClipData() != null) {
                    Uri uri;

                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uri = data.getClipData().getItemAt(i).getUri();
                        uriList.add(uri);
                        byte[] byteArray = null;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            //How to load tiles from a large bitmap

                            //convet bitmap to byte[]
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();


                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            byteArray = stream.toByteArray();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectedImageBytes.add(byteArray);

                    }
                } else {
                    Uri uri = data.getData();
                    byte[] byteArray = null;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        //How to load tiles from a large bitmap

                        //convet bitmap to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();


                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byteArray = stream.toByteArray();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedImageBytes.add(byteArray);

                }
            }

            if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                fileType = FILE_TYPE_FILE;
                Uri uri = data.getData();
                String path = uri.getPath();
                selectedFile = new File(path);
            }


        }
    }
