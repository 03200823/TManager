package com.students.app.http.api;

import com.google.gson.annotations.SerializedName;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

import lombok.Data;

/**

 *    time   : 2019/12/07
 *    desc   : 可进行拷贝的副本
 */
@Data
public final class SignApi implements IRequestApi, IRequestType {

    @SerializedName("m_id")
    private String m_id;

    @SerializedName("number")
    private String number;

    @SerializedName("sign_type")
    private  String sign_type;
    @Override
    public String getApi() {
        return "/sign";
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }

}