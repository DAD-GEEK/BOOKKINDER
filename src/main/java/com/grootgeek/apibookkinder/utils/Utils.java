package com.grootgeek.apibookkinder.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
    private final String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String message;

    @Value("${logs.path}")
    private String pathlog;

    public void logApi(int respuestaApi, String mensaje, String classconsumoApi, String ip) {
        try {
            File dir = new File(pathlog);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = dir + File.separator + fecha + "-Transacciones.txt";
            message = respuestaApi == 0 ? "RECIBO: " + simpleDateFormat.format(new Date()) + " " + classconsumoApi + " " + mensaje + " IP_CONSUMO: " + ip : "RESPONDO: " + simpleDateFormat.format(new Date()) + " " + mensaje + " IP_CONSUMO: " + ip + "\n";
            imprimir(message, fileName);
        } catch (Exception e) {
            message = "Error Utils: fall in logs ";
            logApiError("ERROR AL GUARDAR EL LOG" + message);
            logger.error(e.getMessage(), e);

        }
    }
    public void logApiError(String mensaje) {
        try {
            File dir = new File(pathlog);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = dir + File.separator + fecha + "-Errores.txt";
            message = simpleDateFormat.format(new Date()) + " ------> " + mensaje + "\n";
            imprimir(message, fileName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void imprimir(final String message, String name) {
        synchronized (this) {
            try (final FileWriter fileWriter = new FileWriter(name, true); final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
