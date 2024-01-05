package com.zsheep.ai.common.constants.enums;

public enum TaggerModel {
    
    WD14_VIT_V2("wd14-vit-v2", "wd14-vit-v2"),
    DEEPDANBOORU_V3("deepdanbooru-v3", "deepdanbooru-v3"),
    ;
    
    private String name;
    
    private String model;
    
    TaggerModel(String name, String model) {
        this.name = name;
        this.model = model;
    }
    
    public static TaggerModel getByName(String name) {
        for (TaggerModel taggerModel : TaggerModel.values()) {
            if (taggerModel.getName().equals(name)) {
                return taggerModel;
            }
        }
        return null;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
}
