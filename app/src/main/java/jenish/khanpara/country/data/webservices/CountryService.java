package jenish.khanpara.country.data.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.List;

import jenish.khanpara.country.data.BuildConfig;
import jenish.khanpara.country.data.model.Country;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Developed By JENISH KHANPARA on 09 September 2017.
 */

public interface CountryService {

    @GET("countries.json")
    Observable<List<Country>> getAllCountries();

    @GET("states.json?orderBy=\"country_id\"")
    Observable<JsonElement> getStates(@Query("equalTo") String countryId);

    @GET("cities.json?orderBy=\"state_id\"")
    Observable<JsonElement> getCities(@Query("equalTo") String stateId);



    /**
     * Builder class responsible to create new instances and provide RESTful services.
     */

    class Builder {

        private static HttpLoggingInterceptor getLoggingInterceptor() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            return interceptor;
        }


        private static Gson getGson() {
            return new GsonBuilder().create();
        }

        private static OkHttpClient getOkHttpClient(HttpLoggingInterceptor interceptor) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                   // HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build();
                   // request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }
            });

            return builder.build();
        }

        public static CountryService build() {
            OkHttpClient client = getOkHttpClient(getLoggingInterceptor());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://test-b5c0d.firebaseio.com/country-data/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .client(client)
                    .build();
            return retrofit.create(CountryService.class);
        }
    }
}
