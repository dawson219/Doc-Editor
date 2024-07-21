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
public class DeleteDocumentRequest {
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("owner_id")
	private String ownerId;
}
