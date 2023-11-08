package com.example.CRM.Integration;


import com.example.CRM.Integration.Telegram.APIConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CRMBot {

    private final APIConfig apiConfig;

    public String URL = "https://api.chatling.ai/v1/chatbots?page=1";
    public String URLGetSettings = "https://api.chatling.ai/v1/chatbots/ID/settings";
    public String URLGetText = "https://api.chatling.ai/v1/chatbots/ID/widget/texts";
    public String URLGetContacts = "https://api.chatling.ai/v1/chatbots/ID/contacts";
    public String URLGetMessage = "https://api.chatling.ai/v1/chatbots/ID/predefined-queries";
    public String url = "https://api.weeek.net/public/v1/crm/funnels";
    public String url2 = "https://api.weeek.net/public/v1/crm/funnels";
    public String url3 = "https://api.weeek.net/public/v1/crm/statuses/CODE/deals";
    public String url4 = "https://api.weeek.net/public/v1/crm/funnels/CODE/statuses";

    public String urlContacts = "https://api.weeek.net/public/v1/crm/contacts";
    public String urlOrg = "https://api.weeek.net/public/v1/crm/organizations";
    public String urlMe = "https://api.weeek.net/public/v1/user/me";


    public String init() {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();


        try {
            connection = (HttpURLConnection) new URL(urlContacts).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    builder.append(line + "\n");
                }
                System.out.println(builder.toString());
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return builder.toString();
    }


    public void dealCreate(String name, String orgID, String contactID, String desired_service){
        String jsonInputString = "{\n" +
                "\"executorId\": \"ID\",\n" +
                "\"organizationId\": \"" + orgID + "\",\n" +
                "\"contactId\": \"" + contactID + "\",\n" +
                "\"title\": \"" + name + "\",\n" +
                "\"description\": \"" + desired_service + "\",\n" +
                "\"amount\": 0,\n" +
                "\"winStatus\": \"won\"\n" +
                "}";
        HttpURLConnection connection = null;


        try {
            connection = (HttpURLConnection) new URL(url3).openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);


            connection.connect();


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public boolean hasOrg(String telegram) {
        boolean isHasOrg = false;
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();


        try {
            connection = (HttpURLConnection) new URL(urlOrg).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    builder.append(line + "\n");
                }
                if (builder.toString().contains(telegram)) {
                    isHasOrg = true;
                }
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return isHasOrg;
    }

    public void orgCreate(String name, String telegram) {

        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();

        String jsonCreateOrg = "{\n" +
                "\"name\": \"" + name + "\",\n" +
                "\"addresses\": [\n" +
                "\"none\"\n" +
                "],\n" +
                "\"emails\": [\n" +
                "\"none@example.com\"\n" +
                "],\n" +
                "\"phones\": [\n" +
                "\"" + telegram + "\"\n" +
                "],\n" +
                "\"responsibles\": [\n" +
                "\"ID\"\n" +
                "]\n" +
                "}";


        try {
            connection = (HttpURLConnection) new URL(urlOrg).openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonCreateOrg.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public boolean hasContact(String telegram) {
        boolean isHasContact = false;
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();


        try {
            connection = (HttpURLConnection) new URL(urlContacts).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    builder.append(line + "\n");
                }
                if (builder.toString().contains(telegram)) {
                    isHasContact = true;
                }
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return isHasContact;
    }


    public void contactCreate(String telegram, String orgID) {

        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();

        String jsonCreateOrg = "{\n" +
                "\"organizationId\": \"" + orgID + "\",\n" +
                "\"name\": \"none\",\n" +
                "\"phone\": \"" + telegram + "\"\n" +
                "}";


        try {
            connection = (HttpURLConnection) new URL(urlContacts).openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonCreateOrg.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public String getOrgIdByName(String telegram) {
        String id = "";
        String ids = "";
        String idsp = "";
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();


        try {
            connection = (HttpURLConnection) new URL(urlOrg).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    builder.append(line + "\n");
                }
                if (builder.toString().contains(telegram)) {
                    id = builder.substring(0, builder.indexOf(telegram));
                    ids = id.substring(id.indexOf("\"id\":\"") + 6, id.indexOf("\",\"creatorId\""));
                }
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ids;
    }


    public String getContIdByTg(String tg) {
        String id = "";
        String ids = "";
        String idsp = "";
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();


        try {
            connection = (HttpURLConnection) new URL(urlContacts).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty("Authorization", "Bearer API");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    builder.append(line + "\n");
                }
                if (builder.toString().contains(tg)) {
                    id = builder.toString().substring(0, builder.indexOf(tg));
                    ids = id.substring(id.lastIndexOf("{\"id\":\""), id.lastIndexOf(("\",\"")));
                    idsp = ids.substring(7, ids.indexOf("\",\""));
                }
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return idsp;
    }


}
