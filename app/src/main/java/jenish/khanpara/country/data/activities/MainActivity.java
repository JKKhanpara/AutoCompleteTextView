package jenish.khanpara.country.data.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import jenish.khanpara.country.data.BuildConfig;
import jenish.khanpara.country.data.R;
import jenish.khanpara.country.data.webservices.WeatherService;
import jenish.khanpara.country.data.model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    // composite subscription holds a list of child subscriptions so that we can unsubscribe them all at once
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Call<Weather> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchRxJavaWeather();

        fetchNormalWeather();
    }

    private void fetchNormalWeather() {
        WeatherService service = WeatherService.Builder.build();
        call= service.getWeatherFromNormalApi("", BuildConfig.API_KEY);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });
    }

    private void fetchRxJavaWeather() {
        WeatherService service = WeatherService.Builder.build();
        Subscription subscription=service.getWeatherFromApi("", BuildConfig.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("GithubDemo", e.getMessage());
                    }

                    @Override
                    public final void onNext(Weather response) {

                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeSubscription.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
    }
}
