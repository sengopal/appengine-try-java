/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myapp;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DemoServlet extends HttpServlet {
    public static final String CLIENT_ID = "<Registered AppId from eBay>";
    public static final String CLIENT_SECRET = "<Registered ClientSecret from eBay>";
    public static final String CRED = "Basic " + Base64.encodeBase64((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("req.getRequestURI():" + req.getRequestURI());
        if (req.getRequestURI().contains("demo")) {
            resp.setContentType("text/plain");
            resp.getWriter().println("{ \"name\": \"Senthil " + "CSC83d74b-c9aa-7\" }");
        } else if (req.getRequestURI().contains("exchange")) {
            resp.setContentType("application/json");
            System.out.println("code:" + req.getParameter("code"));
            JSONObject object = exchangeCodeToToken(req.getParameter("code"));
            resp.getWriter().println(object.toString());
        } else if(req.getRequestURI().contains("search")){
            resp.setContentType("application/json");
            JSONObject object = searchFor(req.getParameter("query"), req.getParameter("token"));
            resp.getWriter().println(object.toString());
        }
    }

    private JSONObject searchFor(String query, String token) {
        System.out.println("query:" + query);
        System.out.println("token" + token);
        JSONObject object = new JSONObject();
        try {
            // https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search#_samples
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet getReq = new HttpGet("https://api.ebay.com/buy/browse/v1/item_summary/search?limit=6&q=" + query);
            getReq.setHeader("Accept", "application/json");
            getReq.setHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse httpResponse = httpClient.execute(getReq);

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            object.put("status",httpResponse.getStatusLine().getStatusCode());
            System.out.println("Result:" + result.toString());

            httpClient.close();
            object.put("result", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            object.put("result", e.toString());
        }
        return object;
    }

    private JSONObject exchangeCodeToToken(String code) {
        JSONObject object = new JSONObject();
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://api.ebay.com/identity/v1/oauth2/token");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setHeader("Authorization", CRED);

            post.setEntity(new StringEntity("grant_type=authorization_code&redirect_uri=C_SC-CSC83d74b-c9aa--ezxsnf&code=" + code));

            CloseableHttpResponse httpResponse = httpClient.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            object.put("status",httpResponse.getStatusLine().getStatusCode());
//            System.out.println("Result:" + result.toString());

            httpClient.close();
            JSONObject response = new JSONObject(result.toString());
            System.out.println(response);
            object.put("accessToken", response.getString("access_token"));
            object.put("refreshToken", response.getString("refresh_token"));
        } catch (Exception e) {
            e.printStackTrace();
            object.put("accessToken", e.toString());
        }
        return object;
    }
}
