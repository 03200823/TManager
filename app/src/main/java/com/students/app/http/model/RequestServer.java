package com.students.app.http.model;

import com.students.app.other.AppConfig;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

/**
 * time   : 2020/10/02
 * desc   : 服务器配置
 */
public class RequestServer implements IRequestServer {

    @Override
    public String getHost() {
        return AppConfig.getHostUrl();
    }

    @Override
    public BodyType getType() {
        // 以表单的形式提交参数
        return BodyType.JSON;
    }
}