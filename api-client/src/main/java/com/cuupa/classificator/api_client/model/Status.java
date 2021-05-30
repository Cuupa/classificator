package com.cuupa.classificator.api_client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Status
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-29T11:42:02.031Z[GMT]")
public class Status {
    @JsonProperty("status")
    private String status = null;

    @JsonProperty("knowledgebase_version")
    private String knowledgebaseVersion = null;

    public Status status(String status) {
        this.status = status;
        return this;
    }

    @Schema(example = "running", description = "")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Status knowledgebaseVersion(String knowledgebaseVersion) {
        this.knowledgebaseVersion = knowledgebaseVersion;
        return this;
    }

    @Schema(example = "1.0.3", description = "")
    public String getKnowledgebaseVersion() {
        return knowledgebaseVersion;
    }
    public void setKnowledgebaseVersion(String knowledgebaseVersion) {
        this.knowledgebaseVersion = knowledgebaseVersion;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Status status = (Status) o;
        return Objects.equals(this.status, status.status) &&
                Objects.equals(this.knowledgebaseVersion, status.knowledgebaseVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, knowledgebaseVersion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Status {\n");

        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    knowledgebaseVersion: ").append(toIndentedString(knowledgebaseVersion)).append("\n");
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
