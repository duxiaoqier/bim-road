package com.glodon.hackserver.bean.zl;

import java.util.Map;

public class Shadows {
    private Map<String, ShadowsAttribute> attributeMap;

    public Map<String, ShadowsAttribute> getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map<String, ShadowsAttribute> attributeMap) {
        this.attributeMap = attributeMap;
    }

    @Override
    public String toString() {
        return "Shadows{" +
                "attributeMap=" + attributeMap +
                '}';
    }
}
