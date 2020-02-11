package com.example.swivlapp.components.usermodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.swivlapp.components.datasources.UsersDataSourceFactory;
import com.example.swivlapp.entriesmodel.User;

import io.reactivex.disposables.CompositeDisposable;

public class MainModel extends ViewModel {

   public LiveData<PagedList<User>> userList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 15;

    private UsersDataSourceFactory usersDataSourceFactory;

    public MainModel() {
        usersDataSourceFactory = new UsersDataSourceFactory(compositeDisposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build();

        userList = new LivePagedListBuilder<>(usersDataSourceFactory, config).build();
    }

    public void retry() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().invalidate();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}