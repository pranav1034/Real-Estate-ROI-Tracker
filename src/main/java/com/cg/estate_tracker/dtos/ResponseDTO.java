package com.cg.estate_tracker.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    String message;
    Object data;

    public ResponseDTO(String message,Object data){
        this.message = message;
        this.data = data;
    }
}
