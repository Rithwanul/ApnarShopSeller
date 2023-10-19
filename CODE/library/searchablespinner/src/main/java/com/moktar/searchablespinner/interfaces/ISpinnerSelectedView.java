package com.moktar.searchablespinner.interfaces;

import android.view.View;

public interface ISpinnerSelectedView {
    View getNoSelectionView();
    View getSelectedView(int position);
}
