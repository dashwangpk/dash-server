package com.dash.server.sys.modules.sys.vo;

import com.dash.server.sys.modules.sys.entity.Aa10Entity;
import lombok.Data;

@Data
public class Aa10VO extends Aa10Entity {

    private String items = "";

    public void addItems(String aaa102, String aaa103) {
        items = items + aaa102 + ":" + aaa103 + ";";
    }
}
