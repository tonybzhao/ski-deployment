package edu.brown.cs.student.Ski.Records;

import edu.brown.cs.student.Ski.Records.BasicInfo;

public record SnowForecast(String topSnowDepth, String botSnowDepth, String freshSnowfall, String lastSnowfallDate, BasicInfo basicInfo) {
}
