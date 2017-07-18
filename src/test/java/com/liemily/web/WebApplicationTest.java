package com.liemily.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(WebApplicationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${spring.boot.test.user}")
    private String user;

    @Value("${spring.boot.test.password}")
    private String password;

    private String url;

    private final int AVG_RUN_COUNT = 100;
    private final long MAX_REQUEST_WAIT_MS = 10;

    @Before
    public void setup() {
        url = "http://localhost:" + port;
        restTemplate.getForObject(url, String.class); // First server request initialises server components which takes longer, so run this before performance checks
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

    @Test
    public void testSuccessfulLoginAvgTime() throws Exception {
        long avg = 0;
        for (int i = 0; i < AVG_RUN_COUNT; i++) {
            avg += timePostForLogin(user, password);
        }
        avg /= AVG_RUN_COUNT;
        logger.info("Average run time of successful logins (with " + AVG_RUN_COUNT + " runs) is " + avg + "ms");

        assertThat(avg).isLessThan(MAX_REQUEST_WAIT_MS);
    }

    @Test
    public void testUnsuccessfulLoginAvgTime() throws Exception {
        long avg = 0;
        for (int i = 0; i < AVG_RUN_COUNT; i++) {
            avg += timePostForLogin(user, "");
        }
        avg /= AVG_RUN_COUNT;
        logger.info("Average run time of unsuccessful logins (with " + AVG_RUN_COUNT + " runs) is " + avg + "ms");

        assertThat(avg).isLessThan(MAX_REQUEST_WAIT_MS);
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

    protected long timePostForLogin(String user, String password) {
        long startTime = System.currentTimeMillis();
        postForLogin(user, password);
        long stopTime = System.currentTimeMillis();

        long time = stopTime - startTime;
        logger.info("Post took " + time + "milliseconds");
        return time;
    }
}
