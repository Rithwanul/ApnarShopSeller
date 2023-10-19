package com.bikroybaba.seller.ui.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentSupportBinding;
import com.bikroybaba.seller.model.remote.response.GetConfig;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class SupportFragment extends Fragment {

    private FragmentSupportBinding binding;
    private Utility utility;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_support,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utility = new Utility(getActivity());
        initView();
        // tap event of first contact number
        binding.tvPhone.setOnClickListener(v -> {
            dialPhoneNumber(binding.tvPhone.getText().toString());
        });
        // tap event of second contact number
        binding.tvPhone1.setOnClickListener(v -> {
            dialPhoneNumber(binding.tvPhone1.getText().toString());
        });
        // tap event of third contact number
        binding.tvPhone2.setOnClickListener(v -> {
            dialPhoneNumber(binding.tvPhone2.getText().toString());
        });
        // tap event of fourth contact number
        binding.tvPhone3.setOnClickListener(v -> {
            dialPhoneNumber(binding.tvPhone3.getText().toString());
        });
        // tap event of fifth contact number
        binding.tvPhone4.setOnClickListener(v -> {
            dialPhoneNumber(binding.tvPhone4.getText().toString());
        });

        // tap event of mail address
        binding.tvEmail.setOnClickListener(v -> {
            composeEmail(new String[]{binding.tvEmail.getText().toString()},
                    "Write your mail subject",
                    "Hi,",
                    null);
        });
    }
    private void initView() {
        SharedPreferences preferences =
                requireActivity().getSharedPreferences("Configuration", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("config", "");
        GetConfig getConfig = gson.fromJson(json,GetConfig.class);
        String phoneNumbers = getConfig.getPhoneNo();
        String[] numbers = phoneNumbers.split(",");
        for (int number = 0; number < numbers.length; number++) {
            if (number == 0) {
                binding.tvPhone.setText("+" + numbers[0]);
                binding.tvPhone.setVisibility(View.VISIBLE);
            } else if (number == 1) {
                binding.tvPhone1.setText("+" + numbers[1]);
                binding.tvPhone1.setVisibility(View.VISIBLE);
            } else if (number == 2) {
                binding.tvPhone2.setText("+" + numbers[2]);
                binding.tvPhone2.setVisibility(View.VISIBLE);
            } else if (number == 3) {
                binding.tvPhone3.setText("+" + numbers[3]);
                binding.tvPhone3.setVisibility(View.VISIBLE);
            } else if (number == 4) {
                binding.tvPhone4.setText("+" + numbers[4]);
                binding.tvPhone4.setVisibility(View.VISIBLE);
            }
        }
        binding.tvEmail.setText(getConfig.getEmail());
        binding.tvEmail.setVisibility(View.VISIBLE);
        binding.textView.setText(utility.getLangauge()
                .equalsIgnoreCase(KeyWord.ENGLISH)?R.string.for_any_information_please_contact_following_details:R.string.for_any_information_please_contact_following_details_bn);
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "+" + phoneNumber));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject, String body, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH)?KeyWord.SUPPORT:KeyWord.SUPPORT_BN);
    }
}