package com.meli.backend.rapid.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

 
public class AppProperties {
     
    private Map<String, String> values;

    public AppProperties() throws Exception {
        FileReader fr = null;
        this.values = new HashMap<>();
        
        try {        
            BufferedReader br = open(fr);
            consume( br );
            close(fr);
        } catch (Exception e) {
            close(fr);
            throw e;
        }
    }
    
    private BufferedReader open( FileReader fr ) {

        String file = "src/main/resources/application.properties";
        try {
            fr = new FileReader(file);
        } catch (Exception e) {
            String path = Paths.get("").toAbsolutePath().toString();
            System.err.println("File not found " + path + "/" +file);  
            throw new AppPropertiesNotFoundExeption("File not found " + path + "/" +file); 
        }

        return new BufferedReader(fr);
        
    }

    private void close( FileReader fr ) {
        try {
            if(fr != null)
                fr.close();   
        } catch (Exception e) {
        }
    }

    private void consume( BufferedReader br ) throws IOException {

        String line;
        while( (line = br.readLine()) != null ) {

            if( line.isEmpty() || line.isBlank() )
                 continue;
 
             String[] parts = line.split("=");
             if(parts.length >= 2) {
              
                 String v = "";
                 for(int i=1; i<parts.length; i++) {
                     v += parts[i];
                     if(i < parts.length - 1)
                         v +="=";
                 }
                 values.put(parts[0], v);
             }
         }
    }

    public String getProperty(String key) {
        return this.values.get(key);
    }     
}
