package com.bikroybaba.seller.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.bikroybaba.seller.R;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.searchablespinner.interfaces.ISpinnerSelectedView;

import java.util.ArrayList;

public class AttributeArrayListAdapter extends
        ArrayAdapter<String> implements Filterable, ISpinnerSelectedView {

    private final Context mContext;
    private final ArrayList<String> mBackupStrings;
    private final StringFilter mStringFilter = new StringFilter();
    private ArrayList<String> mStrings;

    public AttributeArrayListAdapter(Context context, ArrayList<String> strings) {
        super(context, R.layout.view_list_item);
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position - 1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (mStrings == null && position > 0)
            return mStrings.get(position).hashCode();
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            MaterialTextView displayName = view.findViewById(R.id.TxtVw_DisplayName);
            displayName.setText(mStrings.get(position - 1));
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            MaterialTextView displayName = view.findViewById(R.id.TxtVw_DisplayName);
            displayName.setText(mStrings.get(position - 1));
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        return View.inflate(mContext, R.layout.view_list_city_selection_item, null);
    }

    @Override
    public Filter getFilter() {
        return mStringFilter;
    }

    public class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size();
                filterResults.values = mBackupStrings;
                return filterResults;
            }
            final ArrayList<String> filterStrings = new ArrayList<>();
            for (String text : mBackupStrings) {
                if (text.toLowerCase().contains(constraint)) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }
}
