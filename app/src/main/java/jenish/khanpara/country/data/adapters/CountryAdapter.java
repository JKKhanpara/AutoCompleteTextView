package jenish.khanpara.country.data.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jenish.khanpara.country.data.model.BaseItem;

/**
 * Developed By JENISH KHANPARA on 10 September 2017.
 */

public class CountryAdapter<T extends BaseItem> extends ArrayAdapter<T> {
    private final Context mContext;
    private final List<T> mItems;
    private final List<T> mItemsAll;
    private final List<T> mItemsSuggestion;

    public CountryAdapter(Context context, List<T> departments) {
        super(context, android.R.layout.simple_dropdown_item_1line, departments);
        this.mContext = context;
        this.mItems = new ArrayList<>(departments);
        this.mItemsAll = new ArrayList<>(departments);
        this.mItemsSuggestion = new ArrayList<>();
    }


    public int getCount() {
        return mItems.size();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        BaseItem country = BaseItem.class.cast(getItem(position));
        TextView name = convertView.findViewById(android.R.id.text1);
        name.setText(country.getName());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return BaseItem.class.cast(resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mItemsSuggestion.clear();
                    for (T department : mItemsAll) {
                        if (department.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            mItemsSuggestion.add(department);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mItemsSuggestion;
                    filterResults.count = mItemsSuggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mItems.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mItems.addAll((ArrayList<T>) results.values);
                    List<?> result = (List<?>) results.values;

                    for (Object object : result) {
                        @SuppressWarnings("unchecked")
                        T objectItem = (T)object;
                        mItems.add(objectItem);
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mItems.addAll(mItemsAll);
                }
                notifyDataSetChanged();
            }
        };
    }
}