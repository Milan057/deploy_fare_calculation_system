package com.mdbackend.mdbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleLocationDTO{
private String lon;
private String lat;
private Integer busId;
private String recordedTime;
}