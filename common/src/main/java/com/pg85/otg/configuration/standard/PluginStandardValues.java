package com.pg85.otg.configuration.standard;

import com.pg85.otg.configuration.PluginConfig.LogLevels;
import com.pg85.otg.configuration.settingType.Setting;
import com.pg85.otg.configuration.settingType.Settings;

public class PluginStandardValues extends Settings
{
   
    // Files
    //	Main Plugin Config
    public static final String ConfigFilename = "OTG.ini";
    
    // Folders
    public static final String BiomeConfigDirectoryName = "GlobalBiomes";
    public static final String BO_DirectoryName = "GlobalObjects";
    
    //  Network
    public static final String ChannelName = "OpenTerrainGenerator";
    public static final int ProtocolVersion = 5;
    
    //  Plugin Defaults
    public static final Setting<LogLevels> LogLevel = enumSetting("LogLevel", LogLevels.Standard);

    /**
     * Name of the plugin, "OpenTerrainGenerator".
     */
    public static final String PLUGIN_NAME = "OpenTerrainGenerator";

}