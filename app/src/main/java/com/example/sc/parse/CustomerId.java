package com.example.sc.parse;

public class CustomerId{
    private String createdTime;
    public CUSTOMERID customerId;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public CUSTOMERID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CUSTOMERID customerId) {
        this.customerId = customerId;
    }

    public static class CUSTOMERID{
        public String entityType;
        public String id;

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
