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
public final class UploadContactApi implements IRequestApi, IRequestType {

    @SerializedName("m_id")
    private String m_id;

    @SerializedName("type")
    private String type;

    @SerializedName("number")
    private String number;
    @Override
    public String getApi() {
        return "/import";
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }

}