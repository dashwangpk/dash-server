package com.dash.server.sys.modules.cfg.vo;

import com.dash.server.sys.modules.cfg.entity.Aa00Entity;
import com.dash.server.sys.modules.cfg.entity.Aa10Entity;
import lombok.Data;

import java.util.List;

@Data
public class Aa10VO extends Aa10Entity {

    private List<Aa10Entity> aa10s;
    private Aa00Entity aa00;

    private String items = "";

    public void addItems(String aaa102, String aaa103) {
        items = items + aaa102 + ":" + aaa103 + ";";
    }
}
