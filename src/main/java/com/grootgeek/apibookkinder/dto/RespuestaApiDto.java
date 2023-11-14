package com.grootgeek.apibookkinder.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
@JsonPOJOBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaApiDto<T> {

	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";

	private boolean success;

	private String codigo;

	private String dateTime;

	private String message;

	private T data;

	/*
	 * DTO response sucess
	 */
	public RespuestaApiDto(boolean success, String codigo, String message) {
		super();
		this.success = success;
		this.dateTime = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime());
		this.codigo = codigo;
		this.message = message;
	}

	@Override
	public String toString() {
		return "RespuestaApiDto [success=" + success + ", codigo=" + codigo + ", dateTime=" + dateTime + ", message="
				+ message + ", data=" + data + "]";
	}

}
