package com.mastotal.familia.familia;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by diego on 9/08/2016.
 */
public class HttpManager {


    // El siguiente metodo va a traer los datos
    public static String getData(RequestPackage requestPackage){
        BufferedReader reader = null;
        String uri = requestPackage.getUri();

        //prepararme para recibir el metodo GET
        if(requestPackage.getMethod().equals("GET")){
            uri+= "?" + requestPackage.getEncodeParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder stringBuilder = new StringBuilder();

            //tanto para get como post
            connection.setRequestMethod(requestPackage.getMethod());

            if (requestPackage.getMethod().equals("POST")){
                connection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(requestPackage.getEncodeParams());
                //ejecutamos
                outputStreamWriter.flush();
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // nos permitira ir leyendo lo que traemos
            String line;

            while ((line = reader.readLine())!= null){
                stringBuilder.append(line + "\n");

            }

            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { //cerrando
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return  null;
                }
            }
        }

    }

    // El siguiente metodo va a traer los datos
    public static String getData(String uri, String username, String password){
        BufferedReader reader = null;
        //inicio codigo para entrar al webservice con seguridad
        byte[] loginBytes = (username + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
        //fin codigo para entrar al webservice con seguridad

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("Authorization", loginBuilder.toString());

            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // nos permitira ir leyendo lo que traemos
            String line;

            while ((line = reader.readLine())!= null){
                stringBuilder.append(line + "\n");

            }

            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { //cerrando
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return  null;
                }
            }
        }

    }

}
