package com.jasonfunderburker.couchpotato.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Ekaterina.Bashkankova on 30.09.2016
 */
@Controller
@RequestMapping("checkResults/download")
public class DownloadsController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadsController.class);

    @Autowired
    ServletContext context;
    /**
     * Path of the file to be downloaded, relative to application's directory
     */
    private String filePath = File.separator + "downloads"+ File.separator;

    /**
     * Method for handling file download request from client
     */

    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("fileName")String fileName, HttpServletResponse response) throws IOException {
        logger.debug("downloadFile: {}", fileName);

        // get absolute path of the application
        URL routePath = this.getClass().getClassLoader().getResource(File.separator);
        if (routePath != null) {
            logger.debug("routePath = {}", routePath);

            // construct the complete absolute path of the file
            String fullPath = routePath.getPath() + filePath + fileName;
            logger.debug("fullPath = {}", fullPath);
            File downloadFile = new File(fullPath);

            // get MIME type of the file
            String mimeType = context.getMimeType(fullPath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            logger.debug("MIME type: {}", mimeType);

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            IOUtils.copy(new FileInputStream(downloadFile), response.getOutputStream());
        }
    }
}
