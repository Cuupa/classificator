package com.cuupa.classificator.api_client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * ClassificationMetadata
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-29T11:42:02.031Z[GMT]")
public class ClassificationMetadata {
    @JsonProperty("processed_at")
    private OffsetDateTime processedAt = null;

    @JsonProperty("processing_time")
    private String processingTime = null;

    @JsonProperty("knowledgebase_version")
    private String knowledgebaseVersion = null;

    public ClassificationMetadata processedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
        return this;
    }

    @Schema(description = "")
    @Valid
    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public ClassificationMetadata processingTime(String processingTime) {
        this.processingTime = processingTime;
        return this;
    }

    @Schema(example = "20ms", description = "")
    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public ClassificationMetadata knowledgebaseVersion(String knowledgebaseVersion) {
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
        ClassificationMetadata classificationMetadata = (ClassificationMetadata) o;
        return Objects.equals(this.processedAt, classificationMetadata.processedAt) &&
                Objects.equals(this.processingTime, classificationMetadata.processingTime) &&
                Objects.equals(this.knowledgebaseVersion, classificationMetadata.knowledgebaseVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processedAt, processingTime, knowledgebaseVersion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClassificationMetadata {\n");

        sb.append("    processedAt: ").append(toIndentedString(processedAt)).append("\n");
        sb.append("    processingTime: ").append(toIndentedString(processingTime)).append("\n");
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
