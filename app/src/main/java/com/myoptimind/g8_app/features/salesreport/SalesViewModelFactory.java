package com.myoptimind.g8_app.features.salesreport;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mApplication;
    private final String mStoreUuid;

    public SalesViewModelFactory(Application application, String storeUuid) {
        mApplication = application;
        mStoreUuid = storeUuid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesViewModel(mApplication,mStoreUuid);
    }
}
