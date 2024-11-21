package com.artogether.venue.venue;

public enum VenueStatusEnum {
        OFFLINE(0, "下架(預設)"),
        ONLINE(1, "上架"),
        SUSPENDED(2, "停權"); // 新增停權狀態

        private final int code; // 狀態代碼
        private final String description; // 狀態描述

        // 私有建構子，僅限於類內部初始化
        VenueStatusEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        // 獲取狀態代碼
        public int getCode() {
            return code;
        }

        // 獲取狀態描述
        public String getDescription() {
            return description;
        }

        // 靜態方法：通過代碼獲取對應的枚舉值
        public static VenueStatusEnum fromCode(int code) {
            for (VenueStatusEnum status : VenueStatusEnum.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("無效的場地狀態代碼：" + code);
        }
    }


