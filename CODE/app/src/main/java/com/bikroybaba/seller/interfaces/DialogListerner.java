package com.bikroybaba.seller.interfaces;

import android.app.Dialog;
import android.view.View;

public interface DialogListerner {
    void yesOnClick(View v, Dialog dialog);
    void noOnClick(View v, Dialog dialog);
}
