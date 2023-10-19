package com.bikroybaba.seller.ui.rating;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.CommentAdapter;
import com.bikroybaba.seller.databinding.FragmentRatingBinding;
import com.bikroybaba.seller.model.entity.Comment;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.RatingViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RatingFragment extends Fragment {

    private final List<Comment> comments = new ArrayList<>();
    private FragmentRatingBinding binding;
    private String language;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_rating, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RatingViewModel ratingViewModel =
                new ViewModelProvider(this).get(RatingViewModel.class);
        Utility utility = new Utility(getActivity());
        language = utility.getLangauge();
        binding.ratingCategoryWise
                .setText(language.equals(KeyWord.BANGLA) ? getString(R.string.average_rating_following_category_bn) : getString(R.string.average_rating_following_category));
        DecimalFormat df = new DecimalFormat("#.##");
        ratingViewModel.getRating().observe(requireActivity(), rating -> {
            binding.ratingProductQuality.setText(getString(R.string.product_quality) + "(" + df.format(Float.parseFloat(rating.getProductQuality())) + ")");
            binding.ratingShopDeliveryService.setText(getString(R.string.shop_delivery_service) + "(" + df.format(Float.parseFloat(rating.getDeliveryService())) + ")");
            binding.ratingBehaviour.setText(getString(R.string.behaviour) + "(" + df.format(Float.parseFloat(rating.getSellerBehavior())) + ")");
            binding.ratingProductPackaging.setText(getString(R.string.product_packaging) + "(" + df.format(Float.parseFloat(rating.getProductPackaging())) + ")");
            binding.productQualityStar.setRating(Float.parseFloat(rating.getProductQuality()));
            binding.ratingShopDeliveryServiceStar.setRating(Float.parseFloat(rating.getDeliveryService()));
            binding.ratingBehaviourStar.setRating(Float.parseFloat(rating.getSellerBehavior()));
            binding.ratingProductPackagingStar.setRating(Float.parseFloat(rating.getProductPackaging()));
            binding.ratingTotalRating.setText("Total Rating " + rating.getTotalRating());

            binding.ratingAverage.setText("Average Rating" + " " + df.format(Float.parseFloat(rating.getAverageRating())) + " out of 5");

            for (Comment comment : rating.getCommentList()) {
                if (!comment.getBuyerComment().equals("")) {
                    comments.add(comment);
                }
            }
            CommentAdapter adapter = new CommentAdapter(getActivity(), comments);
            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            binding.ratingCommentRv.setLayoutManager(mLayoutManager);
            binding.ratingCommentRv.setAdapter(adapter);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? KeyWord.RATING_BN : KeyWord.RATING);
    }
}