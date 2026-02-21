package com.BillSystem.Appticals.helpers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
@Component
public class CustomLogger {
	
	private static final String LOG_DIR = "logs";
    private String currentLogFileName;
    private PrintWriter logWriter;
    
    public CustomLogger() {
        ensureLogDirectory();
        initializeLogFile();
    }
    
    private void ensureLogDirectory() {
        try {
            java.nio.file.Path logPath = Paths.get(LOG_DIR);
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }
    
    private void initializeLogFile() {
        try {
            currentLogFileName = getLogFileName();
            logWriter = new PrintWriter(new FileWriter(currentLogFileName, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }
    
    private String getLogFileName() {
        // Daily logs
        String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Paths.get(LOG_DIR, "log_" + dateString + ".log").toString();
        
        // For hourly logs, use:
        // String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH"));
        // return Paths.get(LOG_DIR, "log_" + dateString + ".log").toString();
    }
    
    private void checkLogFile() {
        String newLogFileName = getLogFileName();
        if (!newLogFileName.equals(currentLogFileName)) {
            closeWriter();
            currentLogFileName = newLogFileName;
            initializeLogFile();
        }
    }
    
    public void log(Object data) {
        checkLogFile();
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String message = "[" + timestamp + "] " + String.valueOf(data);
        
        // Write to file
        if (logWriter != null) {
            logWriter.println(message);
            logWriter.flush();
        }
        
        // Write to console
        System.out.println(message);
    }
    
    public void log(String format, Object... args) {
        log(String.format(format, args));
    }
    
    private void closeWriter() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
    
    // Cleanup when application shuts down
    public void destroy() {
        closeWriter();
    }

}
