package com.bikroybaba.seller.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.DialogProgressbarBinding;

import java.util.Objects;

public class ProgressBarView extends DialogFragment {
    private DialogProgressbarBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.dialog_progressbar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog()).getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }

/*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
*/


    @Override
    public void dismiss() {
        super.dismiss();
        binding = null;
    }
}