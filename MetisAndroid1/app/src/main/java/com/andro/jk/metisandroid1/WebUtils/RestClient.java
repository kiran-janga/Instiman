package com.andro.jk.metisandroid1.WebUtils;

import com.andro.jk.metisandroid1.WebUtils.metisApi;

import retrofit.RestAdapter;

public class RestClient {

    private static metisApi REST_CLIENT;

    private static String ROOT =

            "http://10.1.138.95:8000/";

    static {
        setupRestClient();
    }

    private RestClient() {};

    public static metisApi get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                setEndpoint(ROOT).build();

        REST_CLIENT = restAdapter.create(metisApi.class);
    }

    public static String getRootURL(){

        return ROOT;
    }
}
