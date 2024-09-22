package com.dawson.document.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollaborateDocumentResponse {
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("is_collaborate")
	private String isCollaborate;
	@JsonProperty("collaborate_users")
	private List<String> collaborateUsers;
	@JsonProperty("message")
	private String message;
	@JsonProperty("status")
	private String status;
}
