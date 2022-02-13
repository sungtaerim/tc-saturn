package com.lepse.integration.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.lepse.integrations.response.BaseResponse;

/**
 * response model
 */
@JsonRootName("response")
public class ModelResponse extends BaseResponse {

    public ModelResponse(Code code, Status status) {
        super(code, status);
    }
}

