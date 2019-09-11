package com.alternative.cap.restmindv3.ui.background;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BackgroundViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BackgroundViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}