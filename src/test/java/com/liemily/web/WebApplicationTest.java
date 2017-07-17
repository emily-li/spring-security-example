package com.liemily.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Emily Li on 17/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${spring.boot.test.user}")
    private String user;

    @Value("${spring.boot.test.password}")
    private String password;

    private String url;

    @Before
    public void setup() {
        url = "http://localhost:" + port;
    }


    @Test
    public void testSuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(user, password);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/");
    }

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(user, null);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/login?error");
    }

    protected ResponseEntity<String> postForLogin(String user, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", user);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return restTemplate.postForEntity(url + "/login", request, String.class);
    }
}
