package com.kbhkn.shareandwin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbhkn.shareandwin.domain.Token;
import com.kbhkn.shareandwin.repository.CampaignRepository;
import com.kbhkn.shareandwin.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * Created by kbhkn on 11/25/17.
 */
// ALl Rest URL's has Regexp protection. If doesn't match, return 404 Error.
@RestController
public class ProxyServiceController {
    private final Logger LOG = LoggerFactory.getLogger(ProxyServiceController.class);
    private String token = "";

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    CampaignRepository campaignRepository;

    //Check customer by tckn. Return value type --> TRUE/FALSE
    @GetMapping(value = "/checkUser/{customerTckn:^[\\d]+$}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<?> checkUser(@PathVariable("customerTckn") String customerTckn) throws Exception {
        //If token is expired? Do not show the service error. Renew the token and recall the service.
        boolean isExist;
        try {
            isExist = isCustomerExist(customerTckn);
        }catch (Exception ex){
            if(ex.getMessage().equals("Token Expired")){
                token = authTokenRefresh();
                isExist = isCustomerExist(customerTckn);
            }else
                throw ex;

        }
        return new ResponseEntity<Object>(isExist, isExist ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    //If token was created return count, otherwise create token and return (Coco Cola - Zero) :D :D .
    @GetMapping(value = "/checkToken/{token:^[a-zA-Z0-9]*$}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<?> checkToken(@PathVariable("token") String token) {
        Optional<Token> token1 = tokenRepository.findTokenByToken(token);
        if (token1.isPresent())
            return new ResponseEntity<Object>(token1.get().getCount(), HttpStatus.OK);
        else {
            token1 = Optional.of(new Token(token, 0));
            tokenRepository.save(token1.get());
        }
        return new ResponseEntity<Object>(token1.get().getCount(), HttpStatus.OK);
    }

    //Increase Token and Return the count of token used.
    @GetMapping(value = "/applyToCampaing/{token:^[a-zA-Z0-9_.-]*$}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<?> applyToCampaing(@PathVariable("token") String token) {
        Optional<Token> token1 = tokenRepository.findTokenByToken(token);
        if (!token1.isPresent())
            return new ResponseEntity<Object>("Token isn't exist!", HttpStatus.OK);
        else {
            token1.map(x -> x.getCount()+1);
            tokenRepository.save(token1.get());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "https://www.yapikredi.com.tr/basvuru-merkezi/kredi-karti-basvurusu");
            return new ResponseEntity<String>(headers,HttpStatus.FOUND);
        }
    }

    //Get All Campaigns
    @GetMapping(value = "/getCampaigns", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<?> getCampaigns() {
        return new ResponseEntity<Object>(campaignRepository.findAll(), HttpStatus.OK);
    }

    //Copy-Paste API Doc. code example. :)
    private boolean isCustomerExist(String customerTckn) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("https://api.yapikredi.com.tr/api/customers/v1/customerInformationByTCKN");
        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", token);
        conn.setRequestProperty("Content-Type", "application/json");

        byte[] body = String.format("{\"identityNumber\": \"%s\"}", customerTckn).getBytes();
        conn.setFixedLengthStreamingMode(body.length);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(body);

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            throw new Exception("Token Expired");
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        boolean status = (sb.toString()).contains("clientNo");
        LOG.info(sb.toString());
        LOG.info("Value --> {}", status);

        return status;
    }

    //Copy-Paste API Doc. code example. :)
    private String authTokenRefresh() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String urlToken = "https://api.yapikredi.com.tr/auth/oauth/v2/token";

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("CONTENT_TYPE", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("scope", "oob");
        body.add("grant_type", "client_credentials");
        body.add("client_id", "l7xx3ecf09ffcfd44db19ab729f9aa90ec48");
        body.add("client_secret", "c459009e9cfc491cbe93c397119ea1e5");

        HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
        ResponseEntity<String> result = restTemplate.exchange(urlToken, HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result.getBody());
        JsonNode accessToken = root.path("access_token");
        JsonNode tokenType = root.path("token_type");
        JsonNode expiresIn = root.path("expires_in");

        return String.format("%s %s", tokenType.asText(), accessToken.asText());
    }

}
