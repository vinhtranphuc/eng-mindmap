package com.tranphucvinh.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

public enum UpdateHistoryEnum {

    MEMBER(
            new PropertyEnum[] {
                    PropertyEnum.member_example
            }
    );

    public final PropertyEnum[] properties;

    UpdateHistoryEnum(PropertyEnum[] properties) {
        this.properties = properties;
    }

    public enum PropertyEnum {
        member_example("status", "상태", "အခြေအနေ", "Status", "VISIT_EMBASSY_STATUS", new String[][] {{"date","visitEmbassyDate"}, {"time","visitEmbassyTime"}});
        public final String propertyName;
        public final String ko;
        public final String my;
        public final String en;
        public final String masterType;
        public final String[][] moreInfoProperties; // [{"rootProperty", "convertProperty"}]

        PropertyEnum(String propertyName, String ko, String my, String en) {
            this.propertyName = propertyName;
            this.ko = ko;
            this.my = my;
            this.en = en;
            this.masterType = null;
            this.moreInfoProperties = null;
        }

        PropertyEnum(String propertyName, String ko, String my, String en, String masterType) {
            this.propertyName = propertyName;
            this.ko = ko;
            this.my = my;
            this.en = en;
            this.masterType = masterType;
            this.moreInfoProperties = null;
        }

        PropertyEnum(String propertyName, String ko, String my, String en, String[][] moreInfoProperties) {
            this.propertyName = propertyName;
            this.ko = ko;
            this.my = my;
            this.en = en;
            this.masterType = null;
            this.moreInfoProperties = moreInfoProperties;
        }

        PropertyEnum(String propertyName, String ko, String my, String en, String masterType, String[][] moreInfoProperties) {
            this.propertyName = propertyName;
            this.ko = ko;
            this.my = my;
            this.en = en;
            this.masterType = masterType;
            this.moreInfoProperties = moreInfoProperties;
        }
    }

    @AllArgsConstructor
    @Data
    public class MoreInfoProp {
        private String rootPropName;
        private String convertPropName;
    }
}
