package com.exskylab.koala.core.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class R2ClientConfig {


    @Value("${r2.endpoint}")
    private static String R2_ENDPOINT;

    @Value("${r2.access-key}")
    private static String ACCESS_KEY;

    @Value("${r2.secret-key}")
    private static String SECRET_KEY;

    public static S3Client createS3Client(){

        return S3Client.builder()
                .endpointOverride(URI.create(R2_ENDPOINT))
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }


}
