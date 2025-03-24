package com.example.CasaFinderBackEnd.payload.request;

import lombok.Data;

@Data
public class EmailRequest {
    private String recipient;
    private String subject;
    private String content;
}
