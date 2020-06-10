package com.myoptimind.g8_app.features.login;

import com.myoptimind.g8_app.api.Meta;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    class AuthResponse {
        private LoginResponse data;
        private Meta meta;

        public LoginResponse getData() {
            return data;
        }

        public void setData(LoginResponse data) {
            this.data = data;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }
    }

    @POST("users/login")
    @FormUrlEncoded
    Flowable<AuthResponse> authenticateUser(
            @Field("employee_number") String employeeNumber,
            @Field("password") String password
    );

}
