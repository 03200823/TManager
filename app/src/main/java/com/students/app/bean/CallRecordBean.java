package com.students.app.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CallRecordBean {

    private int type;
    private int during;
    private long time;
}