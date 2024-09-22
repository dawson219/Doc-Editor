package com.dawson.document.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Document")
public class DocumentData {
	@Id
	private String id;
	private String title;
	private String content;
	private String version;
	private String ownerId;
	private String createdAt;
	private String updatedAt;
	private String isShare;
	private String shareId;
	private String isCollaborate;
	private List<String> users;
}
