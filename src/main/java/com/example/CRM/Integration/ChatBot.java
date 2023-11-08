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
public class ChatBot {

    private final APIConfig apiConfig;

    public String url = "https://api.chatling.ai/v1/chatbots";



    public void botUpdate(String name, String description, String botId){
        String urlUpdate = "https://api.chatling.ai/v1/chatbots/" + botId + "/settings";
        String jsonInputString = "{\n" +
                "  \"business_name\": \"" + name + "\",\n" +
                "  \"business_description\": \"" + description + "\",\n" +
                "  \"ai_model_id\": 1,\n" +
                "  \"ai_language\": \"Auto\",\n" +
                "  \"visibility\": \"public\"\n" +
                "}";
        HttpURLConnection connection = null;


        try {
            connection = (HttpURLConnection) new URL(urlUpdate).openConnection();
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
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


    public void botCreate(String name){
        String jsonInputString = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"project_id\": \"ID\"\n" +
                "}";
        HttpURLConnection connection = null;


        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
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


    public String getBotId() {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        String output_first = "";


        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
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
                output_first = builder.toString().substring(builder.toString().lastIndexOf("{\"id\":")+7, builder.toString().lastIndexOf("\",\"name\""));
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
        return output_first.trim();
    }

}
