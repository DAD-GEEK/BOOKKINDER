package com.grootgeek.apibookkinder.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Component
public class Logger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
    private final String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @Setter
    @Value("${logs.path}")
    private String pathLog;


    public void logMessage(String message, String fileName) {
        try {
            File dir = new File(pathLog);
            if (!dir.exists()) dir.mkdir();
            String filePath = dir + File.separator + fileName;
            try (final FileWriter fileWriter = new FileWriter(filePath, true); final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            message = "Error Utils: fall in logs ";
            logApiError("Error to save the log" + message);
            logger.error(e.getMessage(), e);
        }
    }

    public void logApiError(String message) {
        String messageError = simpleDateFormat.format(new Date()) + " ------> " + message;
        logMessage(messageError, fecha + "-Errors.log");
    }

    public void logApi(int responseAPI, String message, String classConsumeApi, String ip) {
        String messageLog;
        switch (responseAPI) {
            case 0:
                messageLog = "RECEIPT: ";
                break;
            case 1:
                messageLog = "RESPONSE: ";
                break;
            default:
                messageLog = "ERROR: ";
        }
        messageLog += simpleDateFormat.format(new Date()) + " " + classConsumeApi + " " + message + " IP_CONSUMO: " + ip;
        logMessage(messageLog, fecha + "-Transactions.log");
    }

}
