package com.example.swivlapp.components.datasources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.swivlapp.components.app.App;
import com.example.swivlapp.entriesmodel.User;
import com.example.swivlapp.network.api.GitHubApi;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class UsersDataSource extends ItemKeyedDataSource<Long, User> {

    private static final String TAG = "USERS_DATA_SOURCE";
    private GitHubApi gitHubApi;
    private CompositeDisposable compositeDisposable;
    private Completable retryCompletable;

    UsersDataSource(CompositeDisposable compositeDisposable) {
        this.gitHubApi = App.getService();
        this.compositeDisposable = compositeDisposable;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                        }
                    }, throwable -> Log.d(TAG, throwable.getMessage())));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<User> callback) {

        compositeDisposable.add(gitHubApi.getUsers(1, params.requestedLoadSize)

                .subscribe(users -> {
                            setRetry(null);
                            callback.onResult(users);
                        },
                        throwable -> {
                        }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {

        compositeDisposable.add(gitHubApi.getUsers(params.key, params.requestedLoadSize).subscribe(users -> {
                    setRetry(null);
                    Log.d("myLog_3", "1");
                    callback.onResult(users);
                },
                throwable -> {
                    setRetry(() -> loadAfter(params, callback));
                }));
    }


    @NonNull
    @Override
    public Long getKey(@NonNull User item) {
        return item.getId();
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }
}

