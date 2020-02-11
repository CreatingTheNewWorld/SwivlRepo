package com.example.swivlapp.components.userui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.swivlapp.R;
import com.example.swivlapp.components.adapters.UsersGitHubAdapter;
import com.example.swivlapp.components.usermodels.MainModel;
import com.example.swivlapp.entriesmodel.User;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvUsersGitHub;
    private LinearLayoutManager linearLayoutManager;
    private MainModel mainModel;
    private UsersGitHubAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        provideViewModel();

        initRecycler();
        initViews();
        initAdapters();
        setRecycler();
        observeMainModel();
        swipeToRefresh();

    }

    private void swipeToRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainModel.refresh();
            }
        });
    }

    private void setRecycler() {
        setRecyclerLayoutManager();
        setRecyclerAdater();
    }

    private void initRecycler() {
        rvUsersGitHub = findViewById(R.id.rv_users_list);


    }

    private void observeMainModel() {

        mainModel.userList.observe(this, new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                Log.d("myLog_9", "4");

                adapter.submitList(users);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void provideViewModel() {
        mainModel = new ViewModelProvider(this).get(MainModel.class);
    }

    private void initAdapters() {
        adapter = new UsersGitHubAdapter();
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }

    private void setRecyclerLayoutManager() {
        rvUsersGitHub.setLayoutManager(linearLayoutManager);
    }

    private void setRecyclerAdater() {
        rvUsersGitHub.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
