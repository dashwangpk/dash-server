package com.dash.server.sys.modules.sys.dto;

import com.dash.server.sys.modules.sys.entity.Aa10Entity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Aa10DTO extends Aa10Entity {
    private Map<String, Object> params;
    private List<Aa10Entity> dictList;
    private List<String> aaa100s;

    public Aa10DTO(){}

    public Aa10DTO( String aaa100){
        setAaa100(aaa100);
    }

    public Aa10DTO(String aaa100, String aaa102){
        setAaa100(aaa100);
        setAaa102(aaa102);
    }


}
