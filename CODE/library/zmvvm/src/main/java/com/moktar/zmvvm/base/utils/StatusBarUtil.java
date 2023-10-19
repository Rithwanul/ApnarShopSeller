package com.moktar.zmvvm.base.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class StatusBarUtil {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;
    /**
     * Set the status bar color
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     */
    @SuppressLint("InlinedApi")
    public static void setStatusBarColor(AppCompatActivity activity, @ColorInt int color) {
        //  set status text dark
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(color);// set status background white
    }
    /**
     * Set the status bar color
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     */
    public static void setColor(AppCompatActivity activity, @ColorInt int color) {
        setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Set the status bar color
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     * @param statusBarAlpha status bar transparency
     */
    public static void setColor(AppCompatActivity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1)
                        .setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
                decorView.addView(statusView);
            }
            setRootView(activity);
        }
    }

    /**
     * Set the solid color of the status bar without translucent effect
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     */
    public static void setColorNoTranslucent(AppCompatActivity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    /**
     * Set the color of the status bar (no translucent effect under 5.0, not recommended)
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     */
    @Deprecated
    public static void setColorDiff(AppCompatActivity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Generate a rectangle the size of the status bar
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundColor(color);
        } else {
            StatusBarView statusView = createStatusBarView(activity, color);
            decorView.addView(statusView);
        }
        setRootView(activity);
    }

    /**
     * Make the status bar semi-transparent
     * <p>
     * Applicable to the interface with a picture as the background, and the picture needs to be filled into the status bar
     *
     * @param activity The activity that needs to be set
     */
    public static void setTranslucent(AppCompatActivity activity) {
        setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Make the status bar semi-transparent
     * <p>
     * Applicable to the interface with a picture as the background, and the picture needs to be filled into the status bar
     *
     * @param activity The activity that needs to be set
     * @param statusBarAlpha status bar transparency
     */
    public static void setTranslucent(AppCompatActivity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * For the root layout is CoordinatorLayout, which makes the status bar semi-transparent
     * <p>
     * Applicable to the interface with a picture as the background, and the picture needs to be filled into the status bar
     *
     * @param activity The activity that needs to be set
     * @param statusBarAlpha status bar transparency
     */
    public static void setTranslucentForCoordinatorLayout(
            AppCompatActivity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * Set the status bar to be fully transparent
     *
     * @param activity The activity that needs to be set
     */
    public static void setTransparent(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }

    /**
     * Make the status bar transparent (translucent effect above 5.0, not recommended)
     * <p>
     * Applicable to the interface with a picture as the background, and the picture needs to be filled into the status bar
     *
     * @param activity The activity that needs to be set
     */
    @Deprecated
    public static void setTranslucentDiff(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set the status bar to be transparent
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setRootView(activity);
        }
    }

    /**
     * Set the status bar color for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     * @param color status bar color value
     */
    public static void setColorForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Set the status bar color, solid color for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     * @param color status bar color value
     */
    public static void setColorNoTranslucentForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    /**
     * Set the status bar color for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     * @param color status bar color value
     * @param statusBarAlpha status bar transparency
     */
    public static void setColorForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout, @ColorInt int color,
                                               int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // Generate a rectangle the size of the status bar
        // Add statusBarView to the layout
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        if (contentLayout.getChildCount() > 0 &&
                contentLayout.getChildAt(0) instanceof StatusBarView) {
            contentLayout.getChildAt(0)
                    .setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
        } else {
            StatusBarView statusBarView = createStatusBarView(activity, color);
            contentLayout.addView(statusBarView, 0);
        }
        // When the content layout is not LinearLayout, set padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity)
                                    + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // Set properties
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);

        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * Set the status bar for DrawerLayout to change color (no translucent effect under 5.0, not recommended)
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     * @param color status bar color value
     */
    @Deprecated
    public static void setColorForDrawerLayoutDiff(AppCompatActivity activity,
                                                   DrawerLayout drawerLayout, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Generate a rectangle the size of the status bar
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 &&
                    contentLayout.getChildAt(0) instanceof StatusBarView) {
                contentLayout.getChildAt(0).
                        setBackgroundColor(calculateStatusColor(color, DEFAULT_STATUS_BAR_ALPHA));
            } else {
                // Add statusBarView to the layout
                StatusBarView statusBarView = createStatusBarView(activity, color);
                contentLayout.addView(statusBarView, 0);
            }
            // When the content layout is not LinearLayout, set padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            // Set properties
            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }
    }

    /**
     * Set the status bar transparency for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * Set the status bar transparency for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForDrawerLayout(activity, drawerLayout);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * Set the status bar transparency for DrawerLayout layout
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     */
    public static void setTransparentForDrawerLayout(
            AppCompatActivity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        // When the content layout is not LinearLayout, set padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
        }

        // Set properties
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * Set the status bar transparency for DrawerLayout layout (translucent effect above 5.0, not recommended)
     *
     * @param activity The activity that needs to be set
     * @param drawerLayout DrawerLayout
     */
    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(
            AppCompatActivity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set the status bar to be transparent
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Set content layout properties
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            // Set drawer layout properties
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            // Set DrawerLayout property
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    /**
     * Set the status bar to be fully transparent for the interface whose head is ImageView
     *
     * @param activity The activity that needs to be set
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTransparentForImageView(
            AppCompatActivity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    /**
     * Set the status bar transparency for the interface whose head is ImageView (use the default transparency)
     *
     * @param activity The activity that needs to be set
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTranslucentForImageView(
            AppCompatActivity activity, View needOffsetView) {
        setTranslucentForImageView(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * Set the status bar transparency for the interface whose head is ImageView
     *
     * @param activity The activity that needs to be set
     * @param statusBarAlpha status bar transparency
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTranslucentForImageView(
            Activity activity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
           if (activity instanceof TabActivity){
               activity.getWindow()//兼容TabActivity
                       .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                               WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
           }
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        addTranslucentView((AppCompatActivity) activity, statusBarAlpha);
        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            }
        }
    }

    public static void setMargin(AppCompatActivity activity, View needOffsetView) {
        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            }
        }
    }


    /**
     * The head of the fragment is the setting status bar of the ImageView is transparent
     *
     * @param activity fragment corresponding activity
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTranslucentForImageViewInFragment(
            AppCompatActivity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * The head of the fragment is the setting status bar of the ImageView is transparent
     *
     * @param activity fragment corresponding activity
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTransparentForImageViewInFragment(
            AppCompatActivity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    /**
     * The head of the fragment is the setting status bar of the ImageView is transparent
     *
     * @param activity fragment corresponding activity
     * @param statusBarAlpha status bar transparency
     * @param needOffsetView The View that needs to be offset downward
     */
    public static void setTranslucentForImageViewInFragment(
            AppCompatActivity activity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(AppCompatActivity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                    .findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * Add semi-transparent rectangular strips
     *
     * @param activity The activity that needs to be set
     * @param statusBarAlpha transparency value
     */
    private static void addTranslucentView(AppCompatActivity activity, int statusBarAlpha) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1)
                    .setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * Generate a colored rectangular bar with the same size as the status bar
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     * @return status bar rectangle
     */
    private static StatusBarView createStatusBarView(AppCompatActivity activity, @ColorInt int color) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    /**
     * Generate a semi-transparent rectangular bar with the same size as the status bar
     *
     * @param activity The activity that needs to be set
     * @param color status bar color value
     * @param alpha transparency value
     * @return status bar raectngle
     */
    private static StatusBarView createStatusBarView(AppCompatActivity activity,
                                                     @ColorInt int color, int alpha) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    /**
     * Set root layout parameters
     */
    private static void setRootView(AppCompatActivity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.
                findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    /**
     * Make the status bar transparent
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Create a semi-transparent rectangular View
     *
     * @param alpha transparency value
     * @return semi-transparent View
     */
    private static StatusBarView createTranslucentStatusBarView(
            AppCompatActivity activity, int alpha) {
        // Draw a rectangle the same height as the status bar
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    /**
     * Get the height of the status bar
     *
     * @param context context
     * @return status bar height
     */
    public static int getStatusBarHeight(Context context) {
        // Get the height of the status bar
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * Calculate the color of the status bar
     *
     * @param color color value
     * @param alpha alpha value
     * @return final status bar color
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
