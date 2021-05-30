package com.cuupa.classificator.api_client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ClassificationRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-29T11:42:02.031Z[GMT]")
public class ClassificationRequest {
    @JsonProperty("content_type")
    private String contentType = null;

    @JsonProperty("content")
    private String content = null;

    public ClassificationRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Schema(example = "text/plain", description = "The content type of the payload")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ClassificationRequest content(String content) {
        this.content = content;
        return this;
    }

    @Schema(description = "The plain text or the base64 representation of the payload")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassificationRequest classificationRequest = (ClassificationRequest) o;
        return Objects.equals(this.contentType, classificationRequest.contentType) &&
                Objects.equals(this.content, classificationRequest.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClassificationRequest {\n");

        sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
        sb.append("    content: ").append(toIndentedString(content)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
