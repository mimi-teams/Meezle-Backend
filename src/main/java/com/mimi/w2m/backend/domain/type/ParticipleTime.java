package com.mimi.w2m.backend.domain.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

public static Optional<ParticipleTime> of(String participleTimeStr) {
    var splitTimes = participleTimeStr.split("-");
    var formatter  = DateTimeFormatter.ofPattern("HH:mm:ss");
    var beginTime  = LocalTime.parse(splitTimes[0], formatter);
    var endTime    = LocalTime.parse(splitTimes[1], formatter);
    if(verify(beginTime, endTime)) {
        return Optional.of(new ParticipleTime(beginTime, endTime));
    } else {
        return Optional.empty();
    }
}

private static Boolean verify(LocalTime beginTime, LocalTime endTime) {
    return beginTime.isBefore(endTime);
}

public static Optional<ParticipleTime> of(Map<String, LocalTime> participleTimeMap) {
    var beginTime = participleTimeMap.get("beginTime");
    var endTime   = participleTimeMap.get("endTime");

    if(Objects.isNull(beginTime) || Objects.isNull(endTime)) {
        return Optional.empty();
    } else if(verify(beginTime, endTime)) {
        return Optional.of(new ParticipleTime(beginTime, endTime));
    } else {
        return Optional.empty();
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