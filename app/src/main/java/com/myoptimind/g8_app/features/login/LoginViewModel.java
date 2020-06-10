package com.myoptimind.g8_app.features.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myoptimind.g8_app.api.ErrorResponse;
import com.myoptimind.g8_app.api.G8Api;
import com.myoptimind.g8_app.features.shared.SharedPref;
import com.myoptimind.g8_app.features.shared.SingleLiveEvent;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.User;
import com.myoptimind.g8_app.models.UserStore;
import com.myoptimind.g8_app.repositories.AnnouncementRepository;
import com.myoptimind.g8_app.repositories.StoreRepository;
import com.myoptimind.g8_app.repositories.UserRepository;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";

    private SharedPref sharedPref;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    /**
     * Repos
     */
    private UserRepository mUserRepository;
    private StoreRepository mStoreRepository;
    private AnnouncementRepository mAnnouncementRepository;

    private SingleLiveEvent<AuthService.AuthResponse> mAuthResponse = new SingleLiveEvent<>();
    private SingleLiveEvent<String> mErrorString                    = new SingleLiveEvent<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mUserRepository         = new UserRepository(application);
        mStoreRepository        = new StoreRepository(application);
        mAnnouncementRepository = new AnnouncementRepository(application);
        sharedPref              = SharedPref.getInstance(application);
    }

    /**
     * Authenticate user
     */
    public void authenticateUser(
            String employeeNumber,
            String password
    ){
        mDisposable.add(mUserRepository.authenticateUser(employeeNumber,password)
                .toObservable()
                .doOnNext(new Consumer<AuthService.AuthResponse>() {
                    @Override
                    public void accept(AuthService.AuthResponse authResponse) throws Exception {
                        LoginResponse loginResponse = authResponse.getData();
                        int id = loginResponse.getId();

                        sharedPref.setIdLoggedIn(String.valueOf(id));
                        sharedPref.putStringIfEmpty(SharedPref.LAST_SYNC_STORE,loginResponse.getStoreLastSync());
                        sharedPref.putStringIfEmpty(SharedPref.LAST_SYNC_SALES,loginResponse.getSalesLastSync());
                        sharedPref.putStringIfEmpty(SharedPref.LAST_SYNC_TIMESLIP,loginResponse.getTimeSlipLastSync());
                        sharedPref.putStringIfEmpty(SharedPref.LAST_SYNC_ANNOUNCEMENTS,loginResponse.getAnnouncementsLastSync());
                        sharedPref.putStringIfEmpty(SharedPref.LAST_SYNC_TIMEINOUT,loginResponse.getTimeInOut());

                        User user = new User();
                        user.setId(loginResponse.getId());
                        user.setPosition(Integer.parseInt(loginResponse.getPosition()));
                        user.setBirthday(loginResponse.getBirthday());
                        user.setFirstName(loginResponse.getFirstName());
                        user.setLastName(loginResponse.getLastName());
                        user.setEmail(user.getEmail());
                        user.setEmployeeNumber(user.getEmployeeNumber());
                        user.setCreatedAt(loginResponse.getCreatedAt());
                        user.setUpdatedAt(loginResponse.getUpdatedAt());
                        mUserRepository.insertUser(user);

                        // Insert stores tagged to logged in user

                        mStoreRepository.insertStore(loginResponse.getTaggedStores(),StoreRepository.STRAT_IGNORE);

                        List<UserStore> userStores = new ArrayList<>();

                        // Junction table for store and user

                        for(Store store : loginResponse.getTaggedStores()){
                            userStores.add(new UserStore(
                                    String.valueOf(user.getId()),
                                    store.getUuid()
                            ));
                        }

                        mStoreRepository.insertUserStore(userStores);

                        // Insert announcements

                        mAnnouncementRepository.insertAnnouncement(loginResponse.getUnreadAnnouncements());


                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    mAuthResponse.setValue(authResponse);
                }, throwable -> {
                    HttpException e = (HttpException) throwable;
                    ErrorResponse errorResponse = G8Api.getConverter().convert(e.response().errorBody());
                    mErrorString.setValue(errorResponse.getMeta().getMessage());
                })
        );
    }

    public LiveData<AuthService.AuthResponse> getAuthResponse() {
        return mAuthResponse;
    }

    public LiveData<String> getErrorString() {
        return mErrorString;
    }
}
