package com.moktar.searchablespinner;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.IdRes;

import java.util.Objects;

public class SelectedView implements Parcelable {
    public static final Creator<SelectedView> CREATOR = new Creator<>() {
        @Override
        public SelectedView createFromParcel(Parcel in) {
            return new SelectedView(in);
        }

        @Override
        public SelectedView[] newArray(int size) {
            return new SelectedView[size];
        }
    };
    private View mView;
    private int mPosition;
    private @IdRes
    long mId;

    public SelectedView(View view, int position, @IdRes long id) {
        mView = view;
        mPosition = position;
        mId = id;
    }

    protected SelectedView(Parcel in) {
        mPosition = in.readInt();
        mId = in.readLong();
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public long getId() {
        return mId;
    }

    public void setId(@IdRes long id) {
        mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectedView that = (SelectedView) o;

        if (mPosition != that.mPosition) return false;
        if (mId != that.mId) return false;
        return Objects.equals(mView, that.mView);

    }

    @Override
    public int hashCode() {
        int result = mView != null ? mView.hashCode() : 0;
        result = 31 * result + mPosition;
        result = 31 * result + (int) (mId ^ (mId >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPosition);
        dest.writeLong(mId);
    }
}
