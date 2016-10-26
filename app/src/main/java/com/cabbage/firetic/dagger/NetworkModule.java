package com.cabbage.firetic.dagger;

import com.cabbage.firetic.model.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    public static String domain = "https://project-nephele.firebaseio.com/";

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        JsonDeserializer<List<Player>> deserializer = new JsonDeserializer<List<Player>>() {
            @Override
            public List<Player> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<Player> results = new ArrayList<>();

                JsonObject object = json.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> set = object.entrySet();
                for (Map.Entry<String, JsonElement> item : set) {
                    String userId = item.getKey();
                    JsonObject userObj = item.getValue().getAsJsonObject();

                    Gson gson = new Gson();
                    Player player = gson.fromJson(userObj, Player.class);
                    player.setUserId(userId);
                    results.add(player);
                }
                return results;
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class, deserializer)
                .create();


        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor logging) {
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }
}
