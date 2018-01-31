package com.khorn.terraincontrol.bukkit;

import com.khorn.terraincontrol.logging.LogMarker;
import com.khorn.terraincontrol.logging.Logger;

import java.util.Objects;
import java.util.logging.Level;

/**
 * Implementation of {@link Logger} for Bukkit.
 */
final class BukkitLogger extends Logger
{
    private final java.util.logging.Logger logger;

    BukkitLogger(java.util.logging.Logger logger)
    {
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    @Override
    public void log(LogMarker level, String message, Throwable error)
    {
        if (minimumLevel.compareTo(level) < 0)
        {
            // Only log messages that we want to see...
            return;
        }
        switch (level)
        {
            case FATAL:
                logger.log(Level.SEVERE, message, error);
                break;
            case ERROR:
                logger.log(Level.SEVERE, message, error);
                break;
            case WARN:
                logger.log(Level.WARNING, message, error);
                break;
            case INFO:
                logger.log(Level.INFO, message, error);
                break;
            case DEBUG:
                logger.log(Level.FINE, message, error);
                break;
            case TRACE:
                logger.log(Level.FINEST, message, error);
                break;
            default:
                // Unknown log level, should never happen
                logger.log(Level.INFO, message, error);
                throw new RuntimeException("Unknown log marker: " + level);
        }
    }
}
