package org.alma.middleware.bearbouncer.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.message.BasicNameValuePair;
import java.util.UUID;

public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("== Gets Token");
		TokenResponse token = getToken("357870062579664");
		System.out.println("\ttoken = "+token.getToken());
		System.out.println("\tcallback = "+token.getCallback());
		System.out.println("\n== Gets Identity with token");
		System.out.println("\t"+getIdentity(token.getToken()));
	}
	
	public static TokenResponse getToken(String imei) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		 // Définition de l'URL d'appel
        HttpPost request = new HttpPost("http://localhost:8080/auth");

        request.setHeader("accept", "application/json");
		request.setEntity(
			new StringEntity(
				"{\"apikey\":\"bb9dd8fb-98fb-40d7-914a-7ff2cc06cff9\",\"imei\":\"357870062579664\"}",
				ContentType.create("application/json")
			)
		);
		
        // Execution de la requête
        CloseableHttpResponse response = httpClient.execute(request);

        // Affichage des données de la réponse (statusCode + headers)
        System.out.println(response);

        // Récupération d'une valeur dans les headers
        Header header = response.getFirstHeader("Content-Type");
        System.out.println(header.getValue());

		Gson gson = new Gson();
        return gson.fromJson(getContextAsString(response),TokenResponse.class);
	}
	
	public static String getIdentity(String token) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		 // Définition de l'URL d'appel
        HttpPost request = new HttpPost("http://localhost:8080/token");

        request.setHeader("accept", "application/json");
		request.setEntity(
			new StringEntity(
				"{\"token\":\""+token+"\"}",
				ContentType.create("application/json")
			)
		);
		
        // Execution de la requête
        CloseableHttpResponse response = httpClient.execute(request);

        // Affichage des données de la réponse (statusCode + headers)
        System.out.println(response);

        // Récupération d'une valeur dans les headers
        Header header = response.getFirstHeader("Content-Type");
        System.out.println(header.getValue());

        return getContextAsString(response);
	}
	
	protected static String getContextAsString(HttpResponse response) throws IOException {
        StringWriter writer = new StringWriter();
        InputStream inputStream = response.getEntity().getContent();
        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }
}
