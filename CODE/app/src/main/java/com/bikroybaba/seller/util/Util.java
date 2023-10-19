package com.bikroybaba.seller.util;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.app.App;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final boolean DEBUG = true;
    private static ProgressBarView mProgressBarView;
    private static ProgressDialog mProgressDialog;

    private Util() {

    }

    public static boolean validatePhoneNumber(String phoneNo) {
        if (phoneNo.length() != 11) {
            return false;
        }
        return !phoneNo.startsWith("010") && !phoneNo.startsWith("011") && !phoneNo.startsWith("012");
    }

    public static String readPhoneNumber(String phoneNo) {
        return phoneNo.substring(2);
    }

    public static String getDateTimeFromMillSecond(String time) {
        // New date object from millis
        Date date = new Date(Long.parseLong(time));
        // formattter
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm aaa");
        // Pass date object
        return formatter.format(date);
    }

    public static void showProgressbar(Context context, boolean isCancelable, String message) {
        if (mProgressBarView == null) {
            mProgressBarView = new ProgressBarView();
        }
        mProgressBarView.setCancelable(isCancelable);
        mProgressBarView.show(((FragmentActivity) context).getSupportFragmentManager(), message);
    }

    public static void hideProgressBar() {
        if (Objects.requireNonNull(mProgressBarView).isVisible()) {
            mProgressBarView.dismiss();
            mProgressBarView = null;
        }
    }

    public static void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static boolean isEmailValid(String email) {
        String regExp =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void debug(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void debug(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String error) {
        if (DEBUG) {
            Log.e(tag, error);
        }
    }

    public static void error(String error) {
        if (DEBUG) {
            Log.e(TAG, error);
        }
    }

    public static void isMainThread() {
        if (DEBUG) {
            Log.e(TAG, "---是否在主线程：" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
        }
    }

    public static Resources getResource() {
        return App.getInstance().getResources();
    }

    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    public static String convertMinutesToDays(int minutes) {
        int hours;
        int days;
        int restMinutes;
        int oneDayMinutes = 1440; //24*60;
        if (minutes < 60) {
            return minutes + " " + getString(R.string.minute);
        } else if (minutes > 60 && minutes < oneDayMinutes) {
            hours = (int) Math.floor((double) minutes / 60);
            restMinutes = minutes % 60;
            if (restMinutes != 0)
                return hours + " " + getString(R.string.hour) + " " + restMinutes + " " + getString(R.string.minute);
            else
                return hours + " " + getString(R.string.hour);
        } else {
            days = (int) Math.floor(((double) minutes / 60) / 24);
            restMinutes = minutes % oneDayMinutes;
            hours = (int) Math.floor((double) restMinutes / 60);
            restMinutes = restMinutes % 60;
            if (restMinutes == 0 && hours == 0) {
                return days + " " + getString(R.string.day);
            } else {
                if (hours != 0) {
                    if (restMinutes > 1) {
                        return days + " " + getString(R.string.day) + " " + hours + " " + getString(R.string.hour)
                                + " " + restMinutes + " " + getString(R.string.minute);
                    } else {
                        return days + " " + getString(R.string.day) + " "
                                + hours + " " + getString(R.string.hour);
                    }
                } else {
                    return days + " " + getString(R.string.day) + " "
                            + restMinutes + " " + getString(R.string.minute);
                }
            }
        }
    }
   /* public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }*/

    /* public static String getLanguage(Application application) {
         return LocaleHelper.getLanguage(application);
     }
 */
   /* public static String getLanguage() {
        return LocaleHelper.getLanguage(App.getInstance());
    }*/
}
