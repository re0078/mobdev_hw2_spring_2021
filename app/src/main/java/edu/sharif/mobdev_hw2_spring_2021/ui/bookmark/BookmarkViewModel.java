package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookmarkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BookmarkViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}