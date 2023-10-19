package com.moktar.searchablespinner.interfaces;
import android.view.View;

public interface OnItemSelectedListener {
    void onItemSelected(View view, int position, long id);
    void onNothingSelected();
}
