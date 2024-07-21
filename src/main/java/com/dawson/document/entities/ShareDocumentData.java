package com.dawson.document.entities;

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
@Document(collection = "ShareDocument")
public class ShareDocumentData {
	@Id
    private String id;
	private String documentId;
    private String title;
    private String content;
    private String version;
    private String ownerId;
    private String createdAt;
    private String updatedAt;
}
