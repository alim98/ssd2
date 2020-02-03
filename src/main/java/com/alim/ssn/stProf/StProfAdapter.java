package com.alim.ssn.stProf;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alim.ssn.R;
import com.alim.ssn.bookmark.BookmarkController;
import com.alim.ssn.like.LikeController;
import com.alim.ssn.main.Comment.CommentController;
import com.alim.ssn.main.Comment.CommentsFragment;
import com.alim.ssn.main.search.TagSearchResult;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;
import com.alim.ssn.studentProperties.Stid;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.github.abdularis.buttonprogress.DownloadButtonProgress;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StProfAdapter extends RecyclerView.Adapter<StProfAdapter.ProfileViewHolder> {
    private List<Post> posts;
    private Context context;
    private LikeController likeController;
    private CommentController commentController;
    private BookmarkController bookmarkController;
    private int stId;
    private String token;
    private Student mStudent;
    private ImageLoader imageLoader;
    private int downloadId;

    public StProfAdapter(List<Post> posts, Context context, Student mStudent) {
        this.posts = posts;
        this.context = context;
        this.mStudent=mStudent;
    }

    @NonNull
    @Override
    public StProfAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        likeController = new LikeController();
        commentController=new CommentController();
        bookmarkController=new BookmarkController();
        stId=((Stid)context.getApplicationContext()).getStId();
        token="Bearer "+((Stid)context.getApplicationContext()).getToken();
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(context.getApplicationContext(), config);

// Setting timeout globally for the download network requests:
        PRDownloaderConfig prconfig = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(context.getApplicationContext(), prconfig);
        return new StProfAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.likesCount.setText(String.valueOf(post.getLikesCount()));
      //  Picasso.get().load(post.getImagePath()).into(holder.downloadIcon);

        holder.download.addOnClickListener(new DownloadButtonProgress.OnClickListener() {
            @Override
            public void onIdleButtonClick(View view) {
                holder.download.setDeterminate();
                downloadId=  PRDownloader.download(post.getImagePath(), Environment.getExternalStorageDirectory().getPath()+".pdf", post.getTitle()).build()

                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                PRDownloader.pause(downloadId);
                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                holder.download.setMaxProgress((int) progress.totalBytes);
                                holder.download.setCurrentProgress((int)progress.currentBytes);
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                holder.download.setFinish();
                            }

                            @Override
                            public void onError(Error error) {

                            }
                        });


            }

            @Override
            public void onCancelButtonClick(View view) {
                PRDownloader.pause(downloadId);
                holder.download.setIdle();
            }

            @Override
            public void onFinishButtonClick(View view) {

            }
        });

        //Chip***********************
        List<String> tags=post.getTags();
        if (tags!=null)
        {
            for (int i = 0; i < tags.size(); i++) {
                Chip chip=new Chip(holder.tagsGroup.getContext());
                chip.setText(tags.get(i));
                holder.tagsGroup.addView(chip);
                int finalI = i;
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TagSearchResult tagSearch =new TagSearchResult(tags.get(finalI));
                        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_activity_container, tagSearch);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });
            }
        }

//*****************STUDENT************
        holder.stName.setText(mStudent.getName()+" "+mStudent.getLast_name());
        if (mStudent.getPhotoUrl() != null) {
            imageLoader.displayImage(mStudent.getPhotoUrl(),holder.profile);
        }


//*********COMMENT*********
        holder.commentsCount.setText(String.valueOf(post.getCommentsCount()));
        holder.commentSendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.CommentEt.getText()!=null){
                    String comment=holder.CommentEt.getText().toString();
                    commentController.send(token, stId, posts.get(position).getId(), comment, new CommentController.OnComment() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(context, "Commented", Toast.LENGTH_SHORT).show();

                            posts.get(position).setCommentsCount(posts.get(position).getCommentsCount()+1);
                            holder.commentsCount.setText(String.valueOf(posts.get(position).getCommentsCount()));
                            holder.CommentEt.getText().clear();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(context, "Error in Comment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentsFragment.class);
                intent.putExtra(CommentsFragment.POST_ID,posts.get(position).getId());
                context.startActivity(intent);
            }
        });
        //********BOOKMARK*******

        bookmarkController.check(token, stId, posts.get(position).getId(), new BookmarkController.OnCheckController() {
            @Override
            public void onComplete(boolean isSaved) {
                posts.get(position).setSaved(isSaved);
                if (isSaved) {
                    holder.bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bookmark_black_fill));
                }else {
                    holder.bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bookmark_black_border));
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Error in save", Toast.LENGTH_SHORT).show();
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posts.get(position).isSaved()){
                    bookmarkController.deleteSavedPost(token, stId, posts.get(position).getId(), new BookmarkController.OnDeleteComplete() {
                        @Override
                        public void onComplete() {
                            holder.bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bookmark_black_border));
                            posts.get(position).setSaved(false);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    bookmarkController.save(token, stId, posts.get(position).getId(), new BookmarkController.OnSaveComplete() {
                        @Override
                        public void onComplete() {
                            holder.bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bookmark_black_fill));
                            posts.get(position).setSaved(true);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //******Like*********
        likeController.isLiked(stId,post.getId(), new LikeController.OnGetIsLiked() {
            @Override
            public void onGet(boolean isLiked) {
                if (isLiked) {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red_fill));
                    post.setLiked(true);

                } else {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_border));
                    post.setLiked(false);
                }
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.isLiked()) {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red_fill));
                    likeController.Like(token, stId,posts.get(position).getId(), new LikeController.OnLikeComplete() {

                        @Override
                        public void onComplete() {
                            holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red_fill));
                            Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                            holder.likesCount.setText(String.valueOf(posts.get(position).getLikesCount()+1));
                            posts.get(position).setLikesCount(posts.get(position).getLikesCount()+1);
                            post.setLiked(true);
                        }

                        @Override
                        public void onError(String e) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_border));
                        }
                    });
                } else {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_border));

                    likeController.unLike(token, stId,posts.get(position).getId(), new LikeController.OnUnlikeComplete() {


                        @Override
                        public void onComplete() {
                            post.setLiked(false);
                            holder.likesCount.setText(String.valueOf(posts.get(position).getLikesCount()-1));
                            posts.get(position).setLikesCount(posts.get(position).getLikesCount()-1);}

                        @Override
                        public void onError() {
                            holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red_fill));

                            Toast.makeText(context, "Error in unlike ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ProfileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_download_icon_post_item)
         DownloadButtonProgress download;
        @BindView(R.id.text_post_title)
        TextView title;/*
        @BindView(R.id.alim)
        ImageView downloadIcon;*/
        @BindView(R.id.jozve_size_tv)
        TextView sizeText;
        @BindView(R.id.tv_post_likes_count)
        TextView likesCount;
        @BindView(R.id.tv_post_comments_count)
        TextView commentsCount;
        @BindView(R.id.text_post_content)
        TextView content;
        @BindView(R.id.iv_like_post)
        ImageView like;
        @BindView(R.id.et_create_comment)
        EditText CommentEt;
        @BindView(R.id.iv_create_comment_send)
        ImageView commentSendIv;
        @BindView(R.id.iv_post_item_bookmark_icon)
        ImageView bookmark;
        @BindView(R.id.iv_post_comments_icon)
        ImageView comments;
        @BindView(R.id.post_profile_image)
        ImageView profile;
        @BindView(R.id.post_profile_name)
        TextView stName;
        @BindView(R.id.chipgroup_tags_posts)
        ChipGroup tagsGroup;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
