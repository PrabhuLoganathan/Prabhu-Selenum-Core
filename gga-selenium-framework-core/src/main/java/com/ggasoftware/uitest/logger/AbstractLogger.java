package com.ggasoftware.uitest.logger;

import com.ggasoftware.uitest.logger.enums.BusinessInfoTypes;
import com.ggasoftware.uitest.logger.enums.LogInfoTypes;
import com.ggasoftware.uitest.logger.enums.LogLevels;

import java.util.ArrayList;
import java.util.List;

import static com.ggasoftware.uitest.logger.enums.BusinessInfoTypes.*;
import static com.ggasoftware.uitest.logger.enums.LogInfoTypes.BUSINESS;
import static com.ggasoftware.uitest.logger.enums.LogInfoTypes.FRAMEWORK;
import static com.ggasoftware.uitest.logger.enums.LogInfoTypes.TECHNICAL;
import static com.ggasoftware.uitest.logger.enums.LogLevels.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Created by Roman_Iovlev on 6/9/2015.
 */
public abstract class AbstractLogger implements ILogger {
    public void init(String message, Object... args) {
        if (logLevel.equalOrLessThan(FATAL) && isMatchLogInfoType(BUSINESS))
            inLog(format(message, args), INIT);
    }

    public void suit(String message, Object... args) {
        if (logLevel.equalOrLessThan(FATAL) && isMatchLogInfoType(BUSINESS)) {
            inLog(format(message, args), SUIT);
        }
    }

    public void test(String message, Object... args) {
        if (logLevel.equalOrLessThan(FATAL) && isMatchLogInfoType(BUSINESS)) {
            inLog(format(message, args), TEST);
        }
    }

    public void step(String message, Object... args) {
        if (logLevel.equalOrLessThan(FATAL) && isMatchLogInfoType(BUSINESS)) {
            inLog(format(message, args), STEP);
        }
    }

    public void fatal(String message, Object... args) {
        if (logLevel.equalOrLessThan(FATAL) && isMatchLogInfoType(BUSINESS)) {
            inLog(format(message, args), FATAL, TECHNICAL);
        }
    }

    public void error(LogInfoTypes logInfoType, String message, Object... args) {
        if (logLevel.equalOrLessThan(ERROR) && isMatchLogInfoType(logInfoType))
            inLog(format(message, args), ERROR, logInfoType);
    }

    public void warning(LogInfoTypes logInfoType, String message, Object... args) {
        if (logLevel.equalOrLessThan(WARNING) && isMatchLogInfoType(logInfoType))
            inLog(format(message, args), WARNING, logInfoType);
    }

    public void info(String message, Object... args) {
        if (logLevel.equalOrLessThan(INFO) && isMatchLogInfoType(FRAMEWORK))
            inLog(format(message, args), INFO, FRAMEWORK);
    }

    public void debug(String message, Object... args) {
        if (logLevel.equalOrLessThan(DEBUG) && isMatchLogInfoType(TECHNICAL))
            inLog(format(message, args), DEBUG, TECHNICAL);
    }

    public void inLog(String message, LogLevels logLevel, LogInfoTypes logInfoType) {}
    public void inLog(String message, BusinessInfoTypes infoType) {
        inLog(message, FATAL, BUSINESS);
    }

    public AbstractLogger() { this(INFO); }
    public AbstractLogger(LogLevels logLevel) {
        this.logLevel = logLevel;
        setLogInfoTypes(BUSINESS, FRAMEWORK, TECHNICAL);
    }

    private LogLevels logLevel = INFO;
    public LogLevels getLogLevel() {
        return logLevel;
    }

    private boolean isMatchLogInfoType(LogInfoTypes logInfoType) {
        switch (logInfoType) {
            case BUSINESS:
                return asList(new int[]{1, 3, 5}).contains(logInfoTypesSettings);
            case FRAMEWORK:
                return asList(new int[]{2, 3, 6}).contains(logInfoTypesSettings);
            case TECHNICAL:
                return asList(new int[]{4, 5, 6}).contains(logInfoTypesSettings);
            default:
                return false;
        }
    }

    private int logInfoTypesSettings = 0;
    public AbstractLogger setLogInfoTypes(LogInfoTypes... logInfoTypes) {
        List<LogInfoTypes> usedTypes = new ArrayList<>();
        logInfoTypesSettings = 0;
        for (LogInfoTypes logInfoType : logInfoTypes)
            if (!usedTypes.contains(logInfoType)) {
                usedTypes.add(logInfoType);
                logInfoTypesSettings += logInfoType.type;
            }
        return this;
    }
}
