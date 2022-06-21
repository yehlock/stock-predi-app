package com.mcup.predi.ui.basic_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BasicInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> Text;
/*
    public BasicInfoViewModel() {
        mText = new MutableLiveData<>();
        ///Text = new MutableLiveData<>();
        mText.setValue("This is basic info fragment");
        //Text.setValue(123456789);
    }

    public LiveData<String> getText() {
        return mText;
    }
/*
    public BasicInfoViewModel2() {
        Text = new MutableLiveData<>();
        Text.setValue(123456789);
    }

    public MutableLiveData<Integer> getText2() {
        return Text;
    }*/
}
