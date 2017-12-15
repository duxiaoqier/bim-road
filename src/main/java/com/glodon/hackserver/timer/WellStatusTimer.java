package com.glodon.hackserver.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glodon.hackserver.bean.Well;
import com.glodon.hackserver.bean.zl.Shadows;
import com.glodon.hackserver.bean.zl.ShadowsAttribute;
import com.glodon.hackserver.bean.zl.Token;
import com.glodon.hackserver.bean.zl.ZlGeneralResponse;
import com.glodon.hackserver.controller.WellController;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class WellStatusTimer {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private String token = "";
    private OkHttpClient client = new OkHttpClient();

    @PostConstruct
    void init() throws IOException {
        getToken();
    }

    @Scheduled(fixedRate = 5000)
    public void getToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\": \"kevin12@kevinglodon.com\", \"password\": \"%B12345678\"}");
        Request request = new Request.Builder()
                .url("https://zl.glodon.com/api/v1/token/login")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("get zl token error");
            return;
        }

        String responseString = response.body().string();
        ZlGeneralResponse<Token> tokenZlGeneralResponse = JSON.parseObject(responseString, new TypeReference<ZlGeneralResponse<Token>>() {
        });

        token = tokenZlGeneralResponse.getResult().getJwtToken();
    }

    @Scheduled(fixedRate = 2000)
    public void getWellStatus() throws IOException {
        boolean wellUploaded = false;
        for (Well well : WellController.wellList) {
            Request request = new Request.Builder()
                    .url("https://zl.glodon.com/api/v1/devices/" + well.getId() + "/shadows")
                    .get()
                    .addHeader("authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("get zl token error");
                continue;
            }
            String responseString = response.body().string();
            ZlGeneralResponse<Shadows> shadowsZlGeneralResponse = JSON.parseObject(responseString, new TypeReference<ZlGeneralResponse<Shadows>>() {
            });

            if (shadowsZlGeneralResponse.getResult() == null) {
//                if (well.getStatus() != -1){
//                    well.setStatus(-1);
//                    wellUploaded = true;
//                }
                continue;
            }

            Shadows shadows = shadowsZlGeneralResponse.getResult();
            ShadowsAttribute rhumidAttribute = shadows.getAttributeMap().get("rhumid");
            ShadowsAttribute tempAttribute = shadows.getAttributeMap().get("temp");
            ShadowsAttribute lstatusAttribute = shadows.getAttributeMap().get("lstatus");
//            Date now = new Date();
//            if (getDateDiff(rhumidAttribute.getTimestamp(), now, TimeUnit.SECONDS) > 30 && getDateDiff(tempAttribute.getTimestamp(), now, TimeUnit.SECONDS) > 30){
//                if (well.getStatus() != -1){
//                    well.setStatus(-1);
//                    wellUploaded = true;
//                }
//                continue;
//            } else {
//                if (well.getStatus() != 1){
//                    well.setStatus(1);
//                    wellUploaded = true;
//                }
//            }
            if (lstatusAttribute != null && lstatusAttribute.getValue() != null && lstatusAttribute.getValue() == 0) {
                if (well.getStatus() == 1) {
                    well.setStatus(-1);
                    wellUploaded = true;
                }
            } else {
                if (well.getStatus() == -1){
                    well.setStatus(1);
                    wellUploaded = true;
                }
            }
            if (!Objects.equals(well.getRhumid(), rhumidAttribute.getValue())) {
                well.setRhumid(rhumidAttribute.getValue());
                wellUploaded = true;
            }
            if (!Objects.equals(well.getRhumid(), tempAttribute.getValue())) {
                well.setTemp(tempAttribute.getValue());
                wellUploaded = true;
            }
        }

        if (wellUploaded) {
            messagingTemplate.convertAndSend("/wells", WellController.wellList);
        }
    }


    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
