package com.dawson.document.models;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchDocumentResponse {
	@JsonProperty("documents")
	private HashMap<String, String> documents;
	@JsonProperty("collaborate_documents")
	private HashMap<String, String> collaborateDocuments;
	@JsonProperty("message")
	private String message;
	@JsonProperty("status")
	private String status;

}
