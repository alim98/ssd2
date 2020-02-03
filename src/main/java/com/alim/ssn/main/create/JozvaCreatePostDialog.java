package com.alim.ssn.main.create;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alim.ssn.R;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.University;
import com.alim.ssn.newWebService.ApiClient;
import com.alim.ssn.newWebService.ApiInterface;
import com.alim.ssn.studentProperties.Stid;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JozvaCreatePostDialog extends Dialog {


    private Activity b;

    private EditText mEdittext;
    private List<String> tags;
    private ChipsInput chipsInput;
    private ImageView mImagePick;
    private ImageView mFilePick;
    private Button mPostBtn;
    private OnJozvaDialogActionClicked onJozvaDialogActionClicked;
    private int stId;

    public JozvaCreatePostDialog(@NonNull Activity activity, OnJozvaDialogActionClicked onJozvaDialogActionClicked) {
        super(activity);
        b = activity;
        this.onJozvaDialogActionClicked = onJozvaDialogActionClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_post);

         stId = ((Stid) Objects.requireNonNull(this).getContext().getApplicationContext()).getStId();

        getStPubInfo();

        mEdittext=findViewById(R.id.et_create_post_desc);
        chipsInput=findViewById(R.id.tags_input);
        mImagePick=findViewById(R.id.iv_create_post_image_pick);
        mFilePick=findViewById(R.id.iv_create_post_file_pick);
        mPostBtn=findViewById(R.id.btn_create_post_post);

        mImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJozvaDialogActionClicked.onImageClicked();
            }
        });

        mFilePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJozvaDialogActionClicked.onFileClicked();
            }
        });
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdittext.getText() != null) {
                    onJozvaDialogActionClicked.onPostClicked(mEdittext.getText().toString(), tags);
                }
            }
        });

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
                                    Toast.makeText(b, "این تگ را قبلا وارد کرده اید.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            chipsInput.addChip(text.toString(), "");
                        } else {
                            Toast.makeText(b, "هر تگ حداقل باید از سه حرف تشکیل شده باشد.", Toast.LENGTH_SHORT).show();
                            chipsInput.addChip(text.toString(), "");
                            chipsInput.removeChipByLabel(text.toString());
                        }


                    }
                }
            }

        });
    }

    public void getStPubInfo(){
        CircleImageView imageView=findViewById(R.id.iv_create_post_st_prf);
        TextView name=findViewById(R.id.tv_create_post_st_name);
        TextView uni=findViewById(R.id.tv_create_post_st_uni);
        ApiInterface apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Student> call=apiInterface.getStPub(stId);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    Picasso.get().load(response.body().getPhotoUrl()).into(imageView);
                    name.setText(response.body().getFull_name());
                    Call<University> universityCall=apiInterface.getUniversity(response.body().getUniversity());
                    universityCall.enqueue(new Callback<University>() {
                        @Override
                        public void onResponse(Call<University> call, Response<University> response) {
                            if (response.isSuccessful()&&response.body().getName()!=null) {
                                uni.setText(response.body().getName());
                            }
                        }

                        @Override
                        public void onFailure(Call<University> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
    }
}


