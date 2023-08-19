package io.github.jkroepke.demo.controller;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AWS {;

    private final AwsCredentialsProvider awsCredentialsProvider;
    private final S3Client s3Client;
    private final S3Template s3Template;

    @Autowired
    AWS(AwsCredentialsProvider awsCredentialsProvider, S3Client s3Client, S3Template s3Template) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.s3Client = s3Client;
        this.s3Template = s3Template;
    }

    @RequestMapping("/aws")
    public Map<String, Object> ping() {
        List<String> bucket = s3Client.listBuckets().buckets()
            .stream().map(Bucket::name).toList();

        HashMap<String, Object> result = new HashMap<>();
        result.put("buckets", bucket);
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
