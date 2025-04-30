package com.cg.estate_tracker.dtos;

import lombok.Data;

@Data
public class Response {
    String message;
    Object data;

    public Response(String message,Object data){
        this.message = message;
        this.data = data;
    }
}
