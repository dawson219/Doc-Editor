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
public class DeleteDocumentResponse {
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("owner_id")
	private String ownerId;
	@JsonProperty("message")
	private String message;
	@JsonProperty("status")
	private String status;

}
