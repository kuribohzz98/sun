package com.sun.app.service.dto;

import com.google.gson.JsonObject;
import com.mservice.pay.models.QRNotifyResponse;

public class MomoQRResponse {
    private Integer status;
    private String signature;
    private Long amount;
    private String partnerRefId;
    private String momoTransId;
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPartnerRefId() {
        return partnerRefId;
    }

    public void setPartnerRefId(String partnerRefId) {
        this.partnerRefId = partnerRefId;
    }

    public String getMomoTransId() {
        return momoTransId;
    }

    public void setMomoTransId(String momoTransId) {
        this.momoTransId = momoTransId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MomoQRResponse{" +
            "status=" + status +
            ", signature='" + signature + '\'' +
            ", amount=" + amount +
            ", partnerRefId='" + partnerRefId + '\'' +
            ", momoTransId='" + momoTransId + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
