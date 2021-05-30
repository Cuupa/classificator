package com.cuupa.classificator.api_client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SemanticResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-29T11:42:02.031Z[GMT]")
public class SemanticResult {
    @JsonProperty("topic")
    private String topic = null;

    @JsonProperty("sender")
    private String sender = null;

    @JsonProperty("metadata")
    @Valid
    private List<Metadata> metadata = null;

    public SemanticResult topicName(String topicName) {
        this.topic = topicName;
        return this;
    }

    @Schema(description = "")
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public SemanticResult sender(String sender) {
        this.sender = sender;
        return this;
    }

    @Schema(description = "")
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public SemanticResult metadata(List<Metadata> metadata) {
        this.metadata = metadata;
        return this;
    }

    public SemanticResult addMetadataItem(Metadata metadataItem) {
        if (this.metadata == null) {
            this.metadata = new ArrayList<Metadata>();
        }
        this.metadata.add(metadataItem);
        return this;
    }

    @Schema(description = "")
    @Valid
    public List<Metadata> getMetadata() {
        return metadata;
    }
    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SemanticResult semanticResult = (SemanticResult) o;
        return Objects.equals(this.topic, semanticResult.topic) &&
                Objects.equals(this.sender, semanticResult.sender) &&
                Objects.equals(this.metadata, semanticResult.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, sender, metadata);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SemanticResult {\n");

        sb.append("    topicName: ").append(toIndentedString(topic)).append("\n");
        sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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
