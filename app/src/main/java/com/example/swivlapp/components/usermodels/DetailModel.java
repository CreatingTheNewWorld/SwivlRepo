package com.example.swivlapp.components.usermodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swivlapp.components.app.App;
import com.example.swivlapp.entriesmodel.User;
import com.example.swivlapp.entriesmodel.UserDetail;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailModel extends ViewModel {

    private User user;
    private UserDetail curUserDetail;
    private Disposable disposable;

    MutableLiveData<UserDetail> userDetailMutableLiveData = new MutableLiveData<>();

    public void setCurrentUser(User currentUser) {
        user = currentUser;
        fetchDetailInfo();
    }

    private void fetchDetailInfo() {


        disposable = App.getService().getUserInfo(user.getLogin())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserDetail>() {
                    @Override
                    public void accept(UserDetail userDetail) throws Exception {

                        curUserDetail = userDetail;
                        userDetailMutableLiveData.setValue(userDetail);
                    }
                });

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public MutableLiveData<UserDetail> getUserDetailMutableLiveData() {
        return userDetailMutableLiveData;
    }

    public void setUserDetailMutableLiveData(UserDetail userDetail) {
        userDetailMutableLiveData.setValue(userDetail);
    }
}
