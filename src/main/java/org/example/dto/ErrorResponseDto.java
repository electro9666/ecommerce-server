package org.example.dto;

public class ErrorResponseDto {

//	private String errorCode;
	private String message;

	public ErrorResponseDto(String message) {
		this.message = message;
	}

	public static ErrorResponseDto of(String message) {
		return new ErrorResponseDto(message);
	}

//	public String getErrorCode() {
//		return message;
//	}

	public String getMessage() {
		return message;
	}

}
