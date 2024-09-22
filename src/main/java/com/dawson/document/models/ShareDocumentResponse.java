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
public class ShareDocumentResponse {
	@JsonProperty("owner_id")
	private String ownerId;
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("share_id")
	private String shareId;
	@JsonProperty("is_share")
	private String isShare;
	@JsonProperty("message")
	private String message;
	@JsonProperty("status")
	private String status;
}
