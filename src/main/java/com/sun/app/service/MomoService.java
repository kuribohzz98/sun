package com.sun.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mservice.shared.constants.Parameter;
import com.mservice.shared.utils.Encoder;
import com.sun.app.domain.enumeration.PaymentStatus;
import com.sun.app.service.dto.MomoConfim;
import com.sun.app.service.dto.MomoQRResponse;
import com.sun.app.service.dto.PaymentDTO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MomoService {
    private final Logger log = LoggerFactory.getLogger(MomoService.class);

    private final String STORESLUG = "MOMODXPJ20200304-DUC_THO_ELECTRONIC";
    private final String PARTNER_CODE = "MOMODXPJ20200304";
    private final String SECRET_KEY = "fBUpTPXsWVBdXMIsoAtI1PdVcKH58zdB";
    private final String DOMAIN = "https://test-payment.momo.vn";
    private final String pathCreateQr = "/pay/store/";
    private final String pathConfim = "/pay/confirm";

    private final PaymentService paymentService;

    public MomoService(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String createUrlQrcode(Long amount, String billId) {
        String urlQrCode = "";
        try {
            String signatureBuilder = new StringBuilder()
                .append("storeSlug").append("=").append(STORESLUG).append("&")
                .append(Parameter.AMOUNT).append("=").append(amount).append("&")
                .append("billId").append("=").append(billId)
                .toString();

            String signature = Encoder.signHmacSHA256(signatureBuilder, SECRET_KEY);
            log.debug("[createUrlQrcode] signatureBuilder: " + signatureBuilder + ", [Signature] -> " + signature);

            urlQrCode = new StringBuilder()
                .append(DOMAIN + pathCreateQr)
                .append(STORESLUG).append("?")
                .append("a").append("=").append(amount).append("&")
                .append("b").append("=").append(billId).append("&")
                .append("s").append("=").append(signature)
                .toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return urlQrCode;
    }

    public byte[] createQrCode(Long amount, String uuid) {
        byte[] jpgByteArray = new byte[0];
        try {
            String data = this.createUrlQrcode(amount, uuid);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "JPG", outputStream);
            jpgByteArray = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jpgByteArray;
    }

    public MomoQRResponse validateQRNotifyRequest(String momoQRRequest) {
        String[] pairs = momoQRRequest.split("&");
        Map<String, String> qrMomoReq = new HashMap<>();
        MomoQRResponse momoQRResponse = null;
        for (String pair: pairs) {
            System.out.println("______________" + pair);
            String[] child = pair.split("=");
            qrMomoReq.put(child[0], child[1]);
        }
        try {
            String signatureBuilder = new StringBuilder()
                .append(Parameter.AMOUNT).append("=").append(Long.parseLong(qrMomoReq.get("amount"))).append("&")
                .append("message").append("=").append(URLDecoder.decode(qrMomoReq.get("message"), StandardCharsets.UTF_8.toString()))
                .append("momoTransId").append("=").append(qrMomoReq.get("transaction_id"))
                .append("partnerRefId").append("=").append(qrMomoReq.get("order_id"))
                .append("status").append("=").append(Integer.parseInt(qrMomoReq.get("status_code")))
                .toString();

            String signature = Encoder.signHmacSHA256(signatureBuilder, SECRET_KEY);
            log.debug("[validateQRNotifyRequest] signatureBuilder: " + signatureBuilder + ", [Signature] -> " + signature);

            momoQRResponse = new MomoQRResponse();
            momoQRResponse.setAmount(Long.parseLong(qrMomoReq.get("amount")));
            momoQRResponse.setMessage(URLDecoder.decode(qrMomoReq.get("message"), StandardCharsets.UTF_8.toString()));
            momoQRResponse.setStatus(Integer.parseInt(qrMomoReq.get("status_code")));
            momoQRResponse.setPartnerRefId(qrMomoReq.get("order_id")); // uuid
            momoQRResponse.setMomoTransId(qrMomoReq.get("transaction_id"));
            momoQRResponse.setSignature(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        confim(momoQRResponse);
        if(momoQRResponse.getStatus() == 0) {
            paymentService.updateStatusByTransactionId(PaymentStatus.PAID, null, momoQRResponse.getPartnerRefId());
        }

        return momoQRResponse;
    }

    private void confim(MomoQRResponse momoQRResponse) {
        try {
            MomoConfim momoConfim = new MomoConfim();
            momoConfim.setPartnerCode(PARTNER_CODE);
            momoConfim.setPartnerRefId(momoQRResponse.getPartnerRefId());
            momoConfim.setRequestId(UUID.randomUUID().toString());
            if (momoQRResponse.getStatus() == 0) {
                momoConfim.setRequestType("capture");
            } else {
                momoConfim.setRequestType("revertAuthorize");
            }
            momoConfim.setMomoTransId(momoQRResponse.getMomoTransId());
            momoConfim.setCustomerNumber("0969346468");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(DOMAIN + pathConfim);
            StringEntity entity = new StringEntity(momoConfim.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            log.debug("_____confim_____", response);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
