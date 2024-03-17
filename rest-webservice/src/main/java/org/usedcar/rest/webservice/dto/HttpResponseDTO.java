package org.usedcar.rest.webservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HttpResponseDTO {

    private String access_token;
    private String refresh_token;
}
