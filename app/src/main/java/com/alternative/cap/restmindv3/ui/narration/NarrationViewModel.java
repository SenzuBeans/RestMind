package com.alternative.cap.restmindv3.ui.narration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NarrationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NarrationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}