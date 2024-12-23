package com.studyshare.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configuration class for setting up AWS S3 client connection.
 * <p>
 * This configuration class provides the necessary credentials and region information
 * to establish a connection with AWS S3 using the AWS SDK v2. It retrieves AWS access credentials
 * and region information from application properties.
 * </p>
 * 
 * <p>
 * Beans defined in this class:
 * <ul>
 *     <li>{@link S3Client} - Configures the AWS S3 client with the specified credentials and region.</li>
 *     <li>{@link Region} - Specifies the AWS region to be used by S3 operations.</li>
 * </ul>
 * </p>
 * 
 * @see software.amazon.awssdk.services.s3.S3Client
 * @since 1.0
 */
@Configuration
public class S3Config {

    /**
     * AWS access key ID, retrieved from application properties.
     */
    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    /**
     * AWS secret access key, retrieved from application properties.
     */
    @Value("${aws.secretKey}")
    private String secretKey;

    /**
     * AWS region for the S3 service, retrieved from application properties.
     */
    @Value("${aws.s3.region}")
    private String region;

    /**
     * Creates and configures an S3 client bean.
     * <p>
     * This method uses AWS basic credentials and the specified region to initialize an {@link S3Client}.
     * The credentials are provided via {@link StaticCredentialsProvider}, which allows using static credentials.
     * </p>
     * 
     * @return a configured {@link S3Client} instance for AWS S3 interactions.
     */
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    /**
     * Creates a bean for the AWS region configuration.
     * <p>
     * This method provides a configured {@link Region} instance based on the specified AWS region.
     * </p>
     * 
     * @return a {@link Region} instance for the AWS S3 client configuration.
     */
    @Bean
    public Region s3Region() {
        return Region.of(region);
    }
}
