package io.github.jkroepke.demo.controller;

import io.awspring.cloud.autoconfigure.core.AwsClientBuilderConfigurer;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.StsClientBuilder;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AWS {;

    private final AwsClientBuilderConfigurer awsClientBuilderConfigurer;
    private final AwsRegionProvider regionProvider;
    private final S3Client s3Client;
    private final S3Template s3Template;

    @Autowired
    AWS(AwsClientBuilderConfigurer awsClientBuilderConfigurer, AwsRegionProvider regionProvider, S3Client s3Client, S3Template s3Template) {
        this.awsClientBuilderConfigurer = awsClientBuilderConfigurer;
        this.regionProvider = regionProvider;
        this.s3Client = s3Client;
        this.s3Template = s3Template;
    }

    @RequestMapping("/aws")
    public Map<String, Object> ping() {

        HashMap<String, Object> result = new HashMap<>();

        List<String> bucket = s3Client.listBuckets().buckets()
            .stream().map(Bucket::name).toList();
        result.put("buckets", bucket);
        result.put("region", regionProvider.getRegion().toString());

        StsClientBuilder client = awsClientBuilderConfigurer.configure(StsClient.builder());
        try (StsClient stsClient = client.build()) {
            GetCallerIdentityResponse callerIdentity = stsClient.getCallerIdentity();

            result.put("arn", callerIdentity.arn());
            result.put("account", callerIdentity.account());
            result.put("userId", callerIdentity.userId());;
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/aws/s3/{bucketName}/{fileName}")
    private Map<String,Object> createFile(@RequestParam("file") MultipartFile file, @PathVariable String bucketName, @PathVariable String fileName) throws Exception {
        S3Resource resource = s3Template.upload(bucketName, fileName, file.getInputStream());

        return Collections.singletonMap(resource.getFilename(), resource.getLocation().toString());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/aws/s3/{bucketName}/{fileName}")
    private String get(@PathVariable String bucketName, @PathVariable String fileName) throws Exception {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
            request -> request.bucket(bucketName).key(fileName));

        return StreamUtils.copyToString(response, Charset.defaultCharset());
    }
}
