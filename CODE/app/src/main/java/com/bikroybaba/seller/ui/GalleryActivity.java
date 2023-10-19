package com.bikroybaba.seller.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GalleryImageAdapter;
import com.bikroybaba.seller.databinding.ActivityGalleryBinding;
import com.bikroybaba.seller.util.KeyWord;
import com.moktar.zmvvm.base.base.BaseActivity;
import com.moktar.zmvvm.base.base.NoViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class GalleryActivity extends BaseActivity<NoViewModel, ActivityGalleryBinding> {

    private ArrayList<String> imagesFromGallery, selectedImageList;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        showContentView();
        setNoTitle();
        selectedImageList = new ArrayList<>();
        imagesFromGallery = getAllShownImagesPath(this);
        GalleryImageAdapter galleryImageAdapter =
                new GalleryImageAdapter(this, imagesFromGallery, selectedImageList);
        bindingView.galleryGridView.setAdapter(galleryImageAdapter);
        onClickEvent();
    }

    //All kinds of click events

    private void onClickEvent() {
        bindingView.galleryGridView.setOnItemClickListener((arg0, view, position, arg3) -> {
            if (null != imagesFromGallery && !imagesFromGallery.isEmpty()) {
                // Logic to show button and show total selected image
                if (selectedImageList.size() < 6) {
                    for (String image : selectedImageList) {
                        if (image.equalsIgnoreCase(imagesFromGallery.get(position))) {
                            selectedImageList.remove(image);
                            view.findViewById(R.id.textViewID).setVisibility(View.GONE);
                            view.findViewById(R.id.imageCheckIVId).setVisibility(View.GONE);
                            if (selectedImageList.size() == 0) {
                                bindingView.gallerySendButton.setVisibility(View.GONE);
                            } else {
                                bindingView.gallerySendButton.setText(getString(R.string.select,
                                        selectedImageList.size()));
                            }
                            return;
                        }
                    }
                    selectedImageList.add(imagesFromGallery.get(position));
                    view.findViewById(R.id.textViewID).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.imageCheckIVId).setVisibility(View.VISIBLE);
                    bindingView.gallerySendButton.setVisibility(View.VISIBLE);
                    bindingView.gallerySendButton
                            .setText(getString(R.string.select, selectedImageList.size()));
                } else {
                    for (String image : selectedImageList) {
                        if (image.equalsIgnoreCase(imagesFromGallery.get(position))) {
                            selectedImageList.remove(image);
                            view.findViewById(R.id.textViewID).setVisibility(View.GONE);
                            view.findViewById(R.id.imageCheckIVId).setVisibility(View.GONE);
                            bindingView.gallerySendButton
                                    .setText(getString(R.string.select, selectedImageList.size()));
                            return;
                        }
                    }
                    Toast.makeText(GalleryActivity.this,
                            "Sorry ! You can not select more than 06 photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bindingView.gallerySendButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("image", selectedImageList);
            setResult(KeyWord.GALLERY_IMAGE, intent);
            finish();
        });
    }

    // Load all image from gallery
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection;
        projection = new String[]{MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
            Log.d("Log", "getAllShownImagesPath: " + absolutePathOfImage);
        }
        Collections.reverse(listOfAllImages);
        cursor.close();
        return listOfAllImages;
    }
}