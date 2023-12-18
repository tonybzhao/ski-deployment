package edu.brown.cs.student.Ski.Records;

import edu.brown.cs.student.Ski.Records.BasicInfo;
import edu.brown.cs.student.Ski.Records.DayForecast;

import java.util.List;

public record WeatherForecast(List<DayForecast> forecast5Day, String summary3Day, String summaryDays4To6, BasicInfo basicInfo) {
}
