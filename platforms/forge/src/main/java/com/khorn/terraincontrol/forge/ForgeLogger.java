package com.khorn.terraincontrol.forge;

import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.configuration.standard.PluginStandardValues;
import com.khorn.terraincontrol.logging.LogMarker;
import com.khorn.terraincontrol.logging.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 * CImplementation of {@link Logger} for Forge.
 *
 * <p>Note that Forge (unlike Bukkit) automatically adds the TerrainControl
 * prefix, so we don't need to do that ourselves.</p>
 */
final class ForgeLogger extends Logger
{
    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(PluginStandardValues.PLUGIN_NAME);

    @Override
    public void log(LogMarker marker, String message, Throwable error)
    {
        if (this.minimumLevel.compareTo(marker) < 0)
        {
            // Only log messages that we want to see...
            return;
        }

        Level level = toLevel(marker);
        if (error == null)
        {
            this.logger.log(level, message);
        } else
        {
            this.logger.log(level, message, error);
        }
    }

    private Level toLevel(LogMarker marker)
    {
        switch (marker)
        {
            case DEBUG:
                return Level.DEBUG;
            case ERROR:
                return Level.ERROR;
            case FATAL:
                return Level.FATAL;
            case INFO:
                return Level.INFO;
            case TRACE:
                return Level.TRACE;
            case WARN:
                return Level.WARN;
            default:
                TerrainControl.log(LogMarker.ERROR, "Unknown log level: " + marker);
                return Level.INFO;
        }
    }
}
