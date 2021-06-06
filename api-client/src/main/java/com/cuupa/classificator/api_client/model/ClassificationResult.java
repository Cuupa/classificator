package com.cuupa.classificator.api_client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ClassificationResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-29T11:42:02.031Z[GMT]")
public class ClassificationResult {
    @JsonProperty("info")
    private ClassificationMetadata info = null;

    @JsonProperty("content_type")
    private String contentType = null;

    @JsonProperty("results")
    @Valid
    private List<SemanticResult> results = null;

    public ClassificationResult info(ClassificationMetadata info) {
        this.info = info;
        return this;
    }

    @Schema(description = "")
    @Valid
    public ClassificationMetadata getInfo() {
        return info;
    }

    public void setInfo(ClassificationMetadata info) {
        this.info = info;
    }

    public ClassificationResult contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Schema(example = "text/plain", description = "")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ClassificationResult results(List<SemanticResult> results) {
        this.results = results;
        return this;
    }

    public ClassificationResult addResultsItem(SemanticResult resultsItem) {
        if (this.results == null) {
            this.results = new ArrayList<SemanticResult>();
        }
        this.results.add(resultsItem);
        return this;
    }

    @Schema(description = "")
    @Valid
    public List<SemanticResult> getResults() {
        return results;
    }

    public void setResults(List<SemanticResult> results) {
        this.results = results;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassificationResult classificationResult = (ClassificationResult) o;
        return Objects.equals(this.info, classificationResult.info) &&
                Objects.equals(this.contentType, classificationResult.contentType) &&
                Objects.equals(this.results, classificationResult.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, contentType, results);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClassificationResult {\n");

        sb.append("    info: ").append(toIndentedString(info)).append("\n");
        sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
        sb.append("    results: ").append(toIndentedString(results)).append("\n");
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
