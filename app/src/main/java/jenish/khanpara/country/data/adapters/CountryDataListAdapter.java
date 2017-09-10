/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jenish.khanpara.country.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jenish.khanpara.country.data.R;
import jenish.khanpara.country.data.database.CountryData;


/**
 * Provide views to RecyclerView with data from mData.
 */
public class CountryDataListAdapter extends RecyclerView.Adapter<CountryDataListAdapter.ViewHolder> {
    private List<CountryData> mData;
    private Context mContext;

    /**
     * Initialize the data of the Adapter.
     *
     * @param data List<CountryData> containing the data to populate views to be used by RecyclerView.
     */
    public CountryDataListAdapter(Context context, List<CountryData> data) {
        this.mContext = context;
        mData = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_country_data_list, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your data at this position and replace the contents of the view
        // with that element
        final CountryData countryData = mData.get(position);
        viewHolder.mCountryName.setText(countryData.getCountryName());
        viewHolder.mCityName.setText(countryData.getCityName());
        viewHolder.mStateName.setText(countryData.getStateName());
        viewHolder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCountryName;
        TextView mCityName;
        TextView mStateName;
        View mContainer;

        ViewHolder(View v) {
            super(v);
            this.mContainer = v;
            mCountryName = v.findViewById(R.id.country_name);
            mCityName = v.findViewById(R.id.city_name);
            mStateName = v.findViewById(R.id.state_name);
        }

    }
}
