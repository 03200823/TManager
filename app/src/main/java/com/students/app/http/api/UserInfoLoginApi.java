package com.students.app.http.api;

import com.google.gson.annotations.SerializedName;
import com.hjq.http.config.IRequestApi;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录
 */
@NoArgsConstructor
@Data
public final class UserInfoLoginApi implements IRequestApi {

    @SerializedName("password")
    private String password;
    @SerializedName("username")
    private String username;

    @Override
    public String getApi() {
        return "/userInfo/login";
    }

    @NoArgsConstructor
    @Data
    public final static class Bean {

        @SerializedName("headIcon")
        private String headIcon;
        @SerializedName("integral")
        private Integer integral;
        @SerializedName("nickName")
        private String  nickName;
        @SerializedName("openId")
        private String  openId;
        @SerializedName("password")
        private String  password;
        @SerializedName("userId")
        private String  userId;
        @SerializedName("username")
        private String  username;
    }
}