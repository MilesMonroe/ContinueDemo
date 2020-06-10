package com.example.log_catcher.test_demo.test7_viewmodel.viewmodel_actiity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelWithLiveData extends ViewModel {
    /**
     * 保存赞的数量，类型设置为整型
     */
    private MutableLiveData<Integer> LikedNumber;



    public MutableLiveData<Integer> getLikedNumber() {
        if(LikedNumber ==null)
        {
            LikedNumber = new MutableLiveData<>();
            LikedNumber.setValue(0);
        }
        return LikedNumber;
    }
    public void setLikedNumber(int n)
    {
        LikedNumber.setValue(n);
    }

}
