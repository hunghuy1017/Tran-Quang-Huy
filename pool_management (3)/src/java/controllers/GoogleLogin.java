/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;

import models.Google;
import models.GoogleForm;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

/**
 *
 * @author Hi
 */
public class GoogleLogin {
    public static String getToken(String code) throws ClientProtocolException, IOException {

        String response = Request.Post(Google.GOOGLE_LINK_GET_TOKEN)

                .bodyForm(

                        Form.form()

       .add("client_id", Google.GOOGLE_CLIENT_ID)

                        .add("client_secret", Google.GOOGLE_CLIENT_SECRET)

                        .add("redirect_uri", Google.GOOGLE_REDIRECT_URI)

                        .add("code", code)

                        .add("grant_type", Google.GOOGLE_GRANT_TYPE)

                        .build()

                )

                .execute().returnContent().asString();


        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);

        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");

        return accessToken;

    }
    public static GoogleForm getUserInfo(final String accessToken) throws ClientProtocolException, IOException {

        String link = Google.GOOGLE_LINK_GET_USER_INFO + accessToken;

        String response = Request.Get(link).execute().returnContent().asString();

        GoogleForm googlePojo = new Gson().fromJson(response, GoogleForm.class);

        return googlePojo;

    }
}
