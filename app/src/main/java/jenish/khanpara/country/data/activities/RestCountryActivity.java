package jenish.khanpara.country.data.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jenish.khanpara.country.data.R;
import jenish.khanpara.country.data.adapters.CountryAdapter;
import jenish.khanpara.country.data.webservices.CountryService;
import jenish.khanpara.country.data.database.CountryData;
import jenish.khanpara.country.data.database.DBHelper;
import jenish.khanpara.country.data.model.City;
import jenish.khanpara.country.data.webservices.CityResponse;
import jenish.khanpara.country.data.model.Country;
import jenish.khanpara.country.data.model.State;
import jenish.khanpara.country.data.webservices.StateResponse;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestCountryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "RestCountryActivity";
    private AutoCompleteTextView actvCountry,actvState,actvCity;
    private ProgressDialog progressDialog;
    private Country mSelectedCountry;
    private City mSelectedCity;
    private State mSelectedState;

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
                    CountryData data=new CountryData();

                    data.setCountryId(mSelectedCountry.getId());
                    data.setCountryName(mSelectedCountry.getName());

                    data.setStateId(mSelectedState.getId());
                    data.setStateName(mSelectedState.getName());

                    data.setCityId(mSelectedCity.getId());
                    data.setCityName(mSelectedCity.getName());

                    DBHelper dbHelper = new DBHelper(RestCountryActivity.this);
                    dbHelper.createOrUpdate(data);
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        fetchCountry();
    }

    private void fetchCountry() {
        Log.v(TAG,"fetchCountry");

        showProgressDialog();

        CountryService service = CountryService.Builder.build();
        service.getAllCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Country>>() {
                    @Override
                    public final void onCompleted() {
                        Log.v(TAG,"fetchCountry--onCompleted");
                        hideProgressDialog();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.v(TAG,"fetchCountry--onError");
                    }

                    @Override
                    public final void onNext(List<Country> response) {
                        Log.v(TAG,"fetchCountry--onNext"+response.size());
                        CountryAdapter adapter = new CountryAdapter<>(RestCountryActivity.this, response);
                        actvCountry.setAdapter(adapter);
                    }
                });
    }

    private void fetchStates(Country country) {
        Log.v(TAG,"fetchStates");
        showProgressDialog();
        CountryService service = CountryService.Builder.build();
        service.getStates("\""+country.getId()+"\"")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public final void onCompleted() {
                        Log.v(TAG,"fetchStates--onCompleted");
                        hideProgressDialog();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.v(TAG,"fetchStates--onError"+e.getMessage());
                    }

                    @Override
                    public final void onNext(JsonElement response) {
                        Log.v(TAG,"fetchStates--onNext"+response);
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(StateResponse.class, new StateJSONDeserializer()).create();
                        StateResponse stateResponse = gson.fromJson(response, StateResponse.class);
                        Log.v(TAG,"fetchStates--onNext"+ stateResponse.statesList.size());
                        CountryAdapter adapter = new CountryAdapter<>(RestCountryActivity.this, stateResponse.statesList);
                        actvState.setAdapter(adapter);
                    }
                });
    }

    private void fetchCity(State state) {
        Log.v(TAG,"fetchCity");
        showProgressDialog();
        CountryService service = CountryService.Builder.build();
        service.getCities("\""+state.getId()+"\"")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public final void onCompleted() {
                        Log.v(TAG,"fetchCity--onCompleted");
                        hideProgressDialog();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.v(TAG,"fetchCity--onError"+e.getMessage());
                    }

                    @Override
                    public final void onNext(JsonElement response) {
                        Log.v(TAG,"fetchCity--onNext"+response);
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(CityResponse.class, new CityJSONDeserializer()).create();
                        CityResponse cityResponse = gson.fromJson(response, CityResponse.class);
                        Log.v(TAG,"fetchCity--onNext"+ cityResponse.cityList.size());
                        CountryAdapter adapter = new CountryAdapter<>(RestCountryActivity.this, cityResponse.cityList);
                        actvCity.setAdapter(adapter);
                    }
                });
    }


    /*****
     *
     * @MyDeserializer is used to convert JSON With Dynamic Keys
     *
     * {
     * "34" : {
     "country_id" : "101",
     "id" : "35",
     "name" : "Tamil Nadu"
     },
     "12" : {
     "country_id" : "101",
     "id" : "13",
     "name" : "Haryana"
     }}
     *
     */
    private class StateJSONDeserializer implements JsonDeserializer<StateResponse> {
        @Override
        public StateResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            StateResponse response = new StateResponse();
            JsonObject object = json.getAsJsonObject();
            Map<String, State> retMap = new Gson().fromJson(object, new TypeToken<HashMap<String, State>>() {}.getType());
            response.statesList = new ArrayList<>(retMap.values());
            return response;
        }
    }

    /*****
     *
     * @MyDeserializer is used to convert JSON With Dynamic Keys
     *
     * {
     * "34" : {
    "country_id" : "101",
    "id" : "35",
    "name" : "Tamil Nadu"
    },
    "12" : {
    "country_id" : "101",
    "id" : "13",
    "name" : "Haryana"
    }}
     *
     */
    private class CityJSONDeserializer implements JsonDeserializer<CityResponse> {
        @Override
        public CityResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            CityResponse response = new CityResponse();
            JsonObject object = json.getAsJsonObject();
            Map<String, City> retMap = new Gson().fromJson(object, new TypeToken<HashMap<String, City>>() {}.getType());
            response.cityList = new ArrayList<>(retMap.values());
            return response;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object item=adapterView.getAdapter().getItem(position);
        if(item instanceof Country){
            Country country = (Country)item ;
            Log.v(TAG,"Country-onItemClick--"+country.getName()+"--"+country.getId());
            fetchStates(country);
            mSelectedCountry=country;
        }else if(item instanceof State){
            State state = (State) item ;
            Log.v(TAG,"State-onItemClick--"+state.getName()+"--"+state.getId());
            fetchCity(state);
            mSelectedState=state;
        }else if(item instanceof City){
            City city = (City) item ;
            Log.v(TAG,"City-onItemClick--"+city.getName()+"--"+city.getId());
            mSelectedCity=city;
        }
    }

    private void hideProgressDialog() {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if(progressDialog==null){
            Log.v(TAG,"showProgressDialog--new object");
            progressDialog = ProgressDialog.show(this,null, null);
            progressDialog.setMessage("Please wait....");
        }
        progressDialog.show();
    }
}
