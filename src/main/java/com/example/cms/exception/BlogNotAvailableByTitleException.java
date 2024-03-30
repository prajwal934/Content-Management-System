package com.example.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class BlogNotAvailableByTitleException  extends RuntimeException{

	private String message;
}
