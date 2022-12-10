package com.planitse2022.planit.util.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orinial = chain.request();
        Request.Builder builder = orinial.newBuilder().header("Authorization", token);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
