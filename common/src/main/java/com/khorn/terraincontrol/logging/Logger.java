package com.khorn.terraincontrol.logging;

import com.khorn.terraincontrol.util.helpers.StringHelper;

import java.util.List;

public abstract class Logger
{

    protected LogMarker minimumLevel = LogMarker.INFO;

    public void setLevel(LogMarker level)
    {
        minimumLevel = level;
    }

    /**
     * Logs the message(s) with the given importance. Message will be prefixed
     * with [TerrainControl], so don't do that yourself.
     *
     * @param level   The severity of the message
     * @param message The messages to log
     */
    public void log(LogMarker level, List<String> message)
    {
        log(level, StringHelper.join(message, "\n"));
    }
    
    /**
     * Logs a string message with the given importance. Message will be
     * prefixed with [TerrainControl], so don't do that yourself.
     *
     * @param level   The severity of the message
     * @param message The message, already formatted.
     */
    public void log(LogMarker level, String message)
    {
    	log(level, message, null);
    }

    /**
     * Logs a string message with the given importance. Message will be
     * prefixed with [TerrainControl], so don't do that yourself.
     *
     * @param level   The severity of the message
     * @param message The message, already formatted.
     * @param error   A stacktrace to print, may be null.
     */
    public abstract void log(LogMarker level, String message, Throwable error);

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger level
     * matches the level provided. Message will be prefixed with
     * [TerrainControl], so don't do that yourself.
     *
     * @param ifLevel  the Log level to test for
     * @param messages The messages to log.
     */
    public void logIfLevel(LogMarker ifLevel, List<String> messages)
    {
        if (minimumLevel == ifLevel)
            log(ifLevel, messages);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger level
     * matches the level provided. Message will be prefixed with
     * [TerrainControl], so don't do that yourself.
     *
     * @param ifLevel the Log level to test for
     * @param message The messages to log.
     * @param error   Stacktrace to print, may be null.
     */
    public void logIfLevel(LogMarker ifLevel, String message, Throwable error)
    {
        if (minimumLevel == ifLevel)
            log(ifLevel, message, error);
    }

    /**
     * Logs the message(s) with the given importance <b>ONLY IF</b> logger level
     * is between the min/max provided. Message will be prefixed with
     * [TerrainControl], so don't do that yourself.
     *
     * @param min      The minimum Log level to test for
     * @param max      The maximum Log level to test for
     * @param messages The messages to log.
     */
    public void logIfLevel(LogMarker min, LogMarker max, List<String> messages)
    {
        if (minimumLevel.compareTo(max) <= 0 && minimumLevel.compareTo(min) >= 0)
            log(max, messages);
    }

    /**
     * Logs the message with the given importance <b>ONLY IF</b> logger level
     * is between the min/max provided. Message will be prefixed with
     * [TerrainControl], so don't do that yourself.
     *
     * @param min     The minimum Log level to test for
     * @param max     The maximum Log level to test for
     * @param message The messages to log.
     * @param error   A stacktrace to print, may be null.
     */
    public void logIfLevel(LogMarker min, LogMarker max, String message, Throwable error)
    {
        if (minimumLevel.compareTo(max) <= 0 && minimumLevel.compareTo(min) >= 0)
            log(max, message, error);
    }

}
