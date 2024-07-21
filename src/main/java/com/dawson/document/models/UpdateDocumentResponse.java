package com.dawson.document.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocumentResponse {
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("version")
	private String version;
	@JsonProperty("is_share")
	private String isShare;
	@JsonProperty("share_id")
	private String shareId;

}
