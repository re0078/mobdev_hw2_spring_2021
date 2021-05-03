package edu.sharif.mobdev_hw2_spring_2021.clients;

import android.content.res.Resources;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationClient {
    private static final LocationClient API_UTIL = new LocationClient();
    private OkHttpClient client;
    private Resources resources;
    private ObjectMapper objectMapper;
    private LocationDTOConverter converter;

    public static LocationClient getInstance(Resources resources) {
        API_UTIL.resources = resources;
        API_UTIL.client = new OkHttpClient();
        API_UTIL.objectMapper = new ObjectMapper();
        API_UTIL.objectMapper.setVisibility(API_UTIL.objectMapper.getSerializationConfig().
                getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        API_UTIL.converter = LocationDTOConverter.getInstance();
        return API_UTIL;
    }

    public List<LocationDTO> searchLocation(String text) {
        Request request = buildSearchLocationRequest(text);
        List<LocationDTO> locationDTOS = new ArrayList<>();
        CompletableFuture<Boolean> lockCompletableFuture = new CompletableFuture<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.wtf("Api", "searchLocation->onFailure: ", e);
                lockCompletableFuture.complete(false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    ServerSearchLocationResponse serverInfoResponse = objectMapper.reader().
                            readValue(Objects.requireNonNull(response.body()).string(),
                                    ServerSearchLocationResponse.class);
                    serverInfoResponse.getFeatures().forEach(serverLocationDTO -> locationDTOS.add(converter.getLocationDTO(serverLocationDTO)));
                    lockCompletableFuture.complete(true);
                } else {
                    Log.e("Api", "getCoinsInfo->onResponse code: " + response.code());
                    lockCompletableFuture.complete(false);
                }
            }
        });
        if (!lockCompletableFuture.join())
            throw new APIConnectionException();
        return locationDTOS;
    }

    private Request buildSearchLocationRequest(String text) {
        String url = resources.getString(R.string.location_api_base_url) + text + ".json";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder()
                .addQueryParameter("access_token", resources.getString(R.string.mapbox_download_token));
        Log.i("Api", "request url: " + urlBuilder.build().toString());
        return new Request.Builder().url(urlBuilder.build()).build();
    }
}
