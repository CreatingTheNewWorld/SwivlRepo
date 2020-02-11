package com.example.swivlapp.components.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.swivlapp.R;
import com.example.swivlapp.components.userui.activities.DetailUserInfoActivity;
import com.example.swivlapp.entriesmodel.User;
import com.google.gson.Gson;

import java.util.Objects;

public class UsersGitHubAdapter extends PagedListAdapter<User, UsersGitHubAdapter.UserViewHolder> {

    private  Gson gson = new Gson();
    public static final String KEY_TO_PARSE = "key_to_parse";

    private static DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public UsersGitHubAdapter() {
        super(DIFF_CALLBACK);
        Log.d("myLog_4", "1");

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("myLog_4", "2");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = getItem(position);

        if(user != null){
            holder.userName.setText(user.getLogin());
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatarUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userImage);

        }

    }

    @Override
    public int getItemCount() {
        Log.d("myLog_4", "3 getItemCount: " + super.getItemCount());
        return super.getItemCount();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.iv_user_image);
            userName = itemView.findViewById(R.id.tv_user_name);
            
            itemView.setOnClickListener(view -> {

                User user = getItem(getAdapterPosition());
                Toast.makeText(view.getContext(), user.getLogin(), Toast.LENGTH_SHORT).show();

                String userInfoString = gson.toJson(user);

                Intent intent = new Intent();
                intent.setClass(view.getContext(),DetailUserInfoActivity.class);
                intent.putExtra(KEY_TO_PARSE,userInfoString);
                view.getContext().startActivity(intent);


            });
        }


    }
}
