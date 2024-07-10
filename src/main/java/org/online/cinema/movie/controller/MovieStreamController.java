package org.online.cinema.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.exception.MovieException;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/api/stream")
public class MovieStreamController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private S3Client s3Client;

    @Value("${s3.bucketName}")
    private String bucketName;

    @GetMapping(value = "/movie/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> streamMovieById(@PathVariable Long id) {
        try {
            movieService.getMovieByIdForStreaming(id);

            String movieFileName = id.toString() + ".mp4";

            log.info("Getting object from S3, bucket: {}, key: {}", bucketName, movieFileName);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(movieFileName)
                    .build();
            InputStream movieStream = s3Client.getObject(getObjectRequest);

            log.info("Streaming movie with id={}, status={}", id,
                    HttpStatus.OK.getReasonPhrase());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + movieFileName + "\"")
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(new InputStreamResource(movieStream));
        } catch (UserException | MovieException ex) {
            log.error("UserException | MovieException={}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("StreamException={}, status={}", ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InputStreamResource(new ByteArrayInputStream(ex.getMessage().getBytes())));
        }
    }
}
