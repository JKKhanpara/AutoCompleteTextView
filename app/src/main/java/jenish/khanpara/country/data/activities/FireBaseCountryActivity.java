package jenish.khanpara.country.data.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jenish.khanpara.country.data.R;
import jenish.khanpara.country.data.adapters.CountryAdapter;
import jenish.khanpara.country.data.database.CountryData;
import jenish.khanpara.country.data.database.DBHelper;
import jenish.khanpara.country.data.model.City;
import jenish.khanpara.country.data.model.Country;
import jenish.khanpara.country.data.model.State;

public class FireBaseCountryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "RestCountryActivity";
    private AutoCompleteTextView actvCountry, actvState, actvCity;
    private ProgressDialog progressDialog;
    private Country mSelectedCountry;
    private City mSelectedCity;
    private State mSelectedState;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        actvCountry = (AutoCompleteTextView)
                findViewById(R.id.actv_country);
        actvCountry.setOnItemClickListener(this);

        actvState = (AutoCompleteTextView)
                findViewById(R.id.actv_state);
        actvState.setOnItemClickListener(this);

        actvCity = (AutoCompleteTextView)
                findViewById(R.id.actv_city);
        actvCity.setOnItemClickListener(this);

        findViewById(R.id.btn_save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CountryData data = new CountryData();

                    data.setCountryId(mSelectedCountry.getId());
                    data.setCountryName(mSelectedCountry.getName());

                    data.setStateId(mSelectedState.getId());
                    data.setStateName(mSelectedState.getName());

                    data.setCityId(mSelectedCity.getId());
                    data.setCityName(mSelectedCity.getName());

                    DBHelper dbHelper = new DBHelper(FireBaseCountryActivity.this);
                    dbHelper.createOrUpdate(data);
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'country-data"' node
        mFirebaseDatabase = mFirebaseInstance.getReference("country-data");
        fetchCountry();
    }

    private void fetchCountry() {
        Log.v(TAG, "fetchCountry");
        showProgressDialog();
        mFirebaseDatabase.child("countries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                List<Country> countryList = new ArrayList<Country>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Country country = childDataSnapshot.getValue(Country.class);
                    countryList.add(country);
                }
                CountryAdapter adapter = new CountryAdapter<>(FireBaseCountryActivity.this, countryList);
                actvCountry.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                hideProgressDialog();
            }
        });
    }

    private void fetchStates(Country country) {
        Log.v(TAG, "fetchStates");
        showProgressDialog();
        Query query = mFirebaseDatabase.child("states").orderByChild("country_id").equalTo(country.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                if (dataSnapshot.exists()) {
                    List<State> stateList = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        State state = childDataSnapshot.getValue(State.class);
                        stateList.add(state);
                    }
                    CountryAdapter adapter = new CountryAdapter<>(FireBaseCountryActivity.this, stateList);
                    actvState.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });

    }

    private void fetchCity(State state) {
        Log.v(TAG, "fetchCity");
        showProgressDialog();

        Query query = mFirebaseDatabase.child("cities").orderByChild("state_id").equalTo(state.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                if (dataSnapshot.exists()) {
                    List<City> cityList = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        City city = childDataSnapshot.getValue(City.class);
                        cityList.add(city);
                    }
                    CountryAdapter adapter = new CountryAdapter<>(FireBaseCountryActivity.this, cityList);
                    actvCity.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object item = adapterView.getAdapter().getItem(position);
        if (item instanceof Country) {
            Country country = (Country) item;
            Log.v(TAG, "Country-onItemClick--" + country.getName() + "--" + country.getId());
            fetchStates(country);
            mSelectedCountry = country;
        } else if (item instanceof State) {
            State state = (State) item;
            Log.v(TAG, "State-onItemClick--" + state.getName() + "--" + state.getId());
            fetchCity(state);
            mSelectedState = state;
        } else if (item instanceof City) {
            City city = (City) item;
            Log.v(TAG, "City-onItemClick--" + city.getName() + "--" + city.getId());
            mSelectedCity = city;
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            Log.v(TAG, "showProgressDialog--new object");
            progressDialog = ProgressDialog.show(this,null, null);
            progressDialog.setMessage("Please wait....");
        }
        progressDialog.show();
    }
}
