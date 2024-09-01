package com.disruption.sys.Configuration;

import com.disruption.sys.Main;
import com.disruptionsystems.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Config {
    public JsonNode getConfig(){
        ObjectMapper mapper = new ObjectMapper();
        File configFile = new File("config.json");
        JsonNode node = null;
        try {
            node = mapper.readTree(configFile);
        } catch (IOException e){
            Main.getLogger().printToLog(LogLevel.ERROR, "ERROR: COULD NOT READ CONFIGURATION: " + e.getMessage() + "\nEXITING");
        }
        return node;
    }
}
