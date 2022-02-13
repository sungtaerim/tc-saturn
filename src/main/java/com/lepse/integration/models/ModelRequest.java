package com.lepse.integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.lepse.integrations.request.BaseRequest;
import java.util.List;

/**
 * request model
 * Generic T
 */
@JsonRootName("request")
public class ModelRequest<T> extends BaseRequest {

    @JsonProperty("items")
    private final List<T> model;

    public ModelRequest(String sender, String integrationName, List<T> model) {
        super(sender, integrationName);
        this.model = model;
    }

    public List<T> getModel() {
        return model;
    }
}