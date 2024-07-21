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
public class GetDocumentResponse {
	@JsonProperty("title")
	private String title;
	@JsonProperty("document_id")
	private String documentId;
	@JsonProperty("owner_id")
	private String ownerId;
	@JsonProperty("content")
	private String content;
	@JsonProperty("version")
	private String version;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("updated_on")
	private String updatedOn;
	@JsonProperty("is_share")
	private String isShare;
	@JsonProperty("share_id")
	private String shareId;
}
