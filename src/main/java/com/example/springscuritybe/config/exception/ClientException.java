package com.example.springscuritybe.config.exception;

import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class ClientException extends BadRequestException {

}
