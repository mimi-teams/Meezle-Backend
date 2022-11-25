package com.mimi.w2m.backend.domain.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mimi.w2m.backend.error.InvalidValueException;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

/**
 * ParticipleTime : 참여가능한 start & end time
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
public final class ParticipleTime implements Serializable {
@JsonProperty(required = true, value = "beginTime") private final LocalTime beginTime;
@JsonProperty(required = true, value = "endTime") private final   LocalTime endTime;

private ParticipleTime(LocalTime beginTime, LocalTime endTime){
    this.beginTime = beginTime;
    this.endTime   = endTime;
}

public static ParticipleTime of(String participleTimeStr) throws InvalidValueException {
    var splitTimes = participleTimeStr.split("-");
    var formatter  = DateTimeFormatter.ofPattern("HH:mm:ss");
    try {
        var beginTime = LocalTime.parse(splitTimes[0], formatter);
        var endTime   = LocalTime.parse(splitTimes[1], formatter);
        if(verify(beginTime, endTime)) {
            return new ParticipleTime(beginTime, endTime);
        } else {
            throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeStr, "유효하지 않은 시간 형식");
        }
    } catch(DateTimeParseException e) {
        throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeStr, "유효하지 않은 시간 형식");
    }
}

private static Boolean verify(LocalTime beginTime, LocalTime endTime) {
    return beginTime.isBefore(endTime);
}

public static ParticipleTime of(Map<String, LocalTime> participleTimeMap) throws InvalidValueException {
    var beginTime = participleTimeMap.get("beginTime");
    var endTime   = participleTimeMap.get("endTime");

    if(Objects.isNull(beginTime) || Objects.isNull(endTime)) {
       throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeMap, "유효하지 않은 시간 형식");
    } else {
        return new ParticipleTime(beginTime, endTime);
    }
}

@JsonProperty(required = true, value = "beginTime")
public LocalTime beginTime() {
    return beginTime;
}

@JsonProperty(required = true, value = "endTime")
public LocalTime endTime() {
    return endTime;
}

@Override
public int hashCode() {
    return Objects.hash(beginTime, endTime);
}

@Override
public boolean equals(Object obj) {
    if(obj == this) {
        return true;
    }
    if(obj == null || obj.getClass() != this.getClass()) {
        return false;
    }
    var that = (ParticipleTime) obj;
    return Objects.equals(this.beginTime, that.beginTime) &&
           Objects.equals(this.endTime, that.endTime);
}

/**
 * 시간은 HH:mm:ss 형식; Instance는 beginTime-endTime 형식
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Override
public String toString() {
    var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return beginTime.format(formatter) + "-" + endTime.format(formatter);
}

}