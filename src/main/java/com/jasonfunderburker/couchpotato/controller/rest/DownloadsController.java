package com.jasonfunderburker.couchpotato.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created on 30.09.2016
 *
 * @author JasonFunderburker
 */
@RestController
@RequestMapping("checkResults/download")
public class DownloadsController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadsController.class);
    /**
     * Path of the file to be downloaded, relative to application's directory
     */
    private String filePath = File.separator + "downloads"+ File.separator;


    @RequestMapping(value = "/{fileName:\\w+\\.\\w+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName")String fileName) throws IOException, URISyntaxException {
        logger.debug("downloadFile: {}", fileName);

        String relativePath = filePath + fileName;
        URL fileUrl = this.getClass().getClassLoader().getResource(relativePath);
        if (fileUrl != null) {
            logger.debug("fileUrl = {}", fileUrl);

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(fileUrl.toURI())));

            return ResponseEntity.ok()
                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName))
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
