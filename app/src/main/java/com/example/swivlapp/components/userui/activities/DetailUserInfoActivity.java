package com.example.swivlapp.components.userui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.swivlapp.R;
import com.example.swivlapp.components.usermodels.DetailModel;
import com.example.swivlapp.entriesmodel.User;
import com.example.swivlapp.entriesmodel.UserDetail;
import com.google.gson.Gson;

import static com.example.swivlapp.components.adapters.UsersGitHubAdapter.KEY_TO_PARSE;

public class DetailUserInfoActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private DetailModel detailModel;
    private ImageView userIconDetail;
    private TextView tvLoginDetail;
    private TextView tvLinkDetail;
    private TextView tvReposCountDetail;
    private TextView tvGistsCountDetail;
    private TextView tvFollowersCountDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_info);
        initViewModel();
        initLiveData();
        initViews();

        Bundle bundle = getIntent().getExtras();
        String userStr = "";
        if(bundle != null){
           userStr = bundle.getString(KEY_TO_PARSE);
        }
        if (!userStr.isEmpty()){

           User user = gson.fromJson(userStr,User.class);
           detailModel.setCurrentUser(user);
        }

    }

    private void initLiveData() {

        detailModel.getUserDetailMutableLiveData().observe(this, new Observer<UserDetail>() {
            @Override
            public void onChanged(UserDetail userDetail) {

                initUserProfile(userDetail);

            }
        });

    }

    private void initUserProfile(UserDetail userDetail) {


        if(userDetail != null){
            Glide.with(this)
                    .load(userDetail.getAvatarUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userIconDetail);

            tvLoginDetail.setText(userDetail.getLogin());

            tvLinkDetail.setMovementMethod(LinkMovementMethod.getInstance());
            tvLinkDetail.setText(Html.fromHtml(userDetail.getBlog()));

            tvReposCountDetail.setText(userDetail.getPublicRepos().toString());
            tvGistsCountDetail.setText(userDetail.getPublicGists().toString());
            tvFollowersCountDetail.setText(userDetail.getFollowers().toString());

        }

    }

    private void initViews() {

        userIconDetail = findViewById(R.id.iv_user_icon_detail);
        tvLoginDetail = findViewById(R.id.tv_login);
        tvLinkDetail = findViewById(R.id.tv_link);
        tvReposCountDetail = findViewById(R.id.tv_repos_count);
        tvGistsCountDetail = findViewById(R.id.tv_gists_count);
        tvFollowersCountDetail = findViewById(R.id.tv_followers_count);

    }




    private void initViewModel() {
        detailModel = new ViewModelProvider(this).get(DetailModel.class);
    }


}
