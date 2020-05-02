package com.sun.app.service.dto;

public class MomoConfim {
    private String partnerCode;
    private String partnerRefId;
    private String requestType;
    private String requestId;
    private String momoTransId;
    private String signature;
    private String customerNumber;
    private String description;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerRefId() {
        return partnerRefId;
    }

    public void setPartnerRefId(String partnerRefId) {
        this.partnerRefId = partnerRefId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMomoTransId() {
        return momoTransId;
    }

    public void setMomoTransId(String momoTransId) {
        this.momoTransId = momoTransId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MomoConfim{" +
            "partnerCode='" + partnerCode + '\'' +
            ", partnerRefId='" + partnerRefId + '\'' +
            ", requestType=" + requestType +
            ", requestId=" + requestId +
            ", momoTransId=" + momoTransId +
            ", signature='" + signature + '\'' +
            ", customerNumber=" + customerNumber +
            ", description='" + description + '\'' +
            '}';
    }
}
