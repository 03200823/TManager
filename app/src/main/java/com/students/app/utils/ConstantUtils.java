package com.students.app.utils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.StringUtils;
import com.students.app.http.api.UserInfoLoginApi;

public class ConstantUtils {

    public static synchronized UserInfoLoginApi.Bean getUserData() {

        String userGson = SPStaticUtils.getString(SP.UserData);

        if (StringUtils.isEmpty(userGson)) {
            return new UserInfoLoginApi.Bean();
        }

        return GsonUtils.fromJson(userGson, UserInfoLoginApi.Bean.class);
    }

    public final static class Bundle {

        public static String Bean = "Bean";

        public static String IsEdit = "IsEdit";

    }

    public final static class SP {

        /** 是否登录 */
        public final static String IsLogin = "IsLogin";

        /** 用户数据 */
        public final static String UserData = "UserData";
    }


}
