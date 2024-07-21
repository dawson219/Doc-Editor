package com.dawson.document.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawson.document.models.CreateDocumentRequest;
import com.dawson.document.models.CreateDocumentResponse;
import com.dawson.document.models.DeleteDocumentRequest;
import com.dawson.document.models.DeleteDocumentResponse;
import com.dawson.document.models.FetchDocumentResponse;
import com.dawson.document.models.FetchDocumentsRequest;
import com.dawson.document.models.FetchShareDocumentsRequest;
import com.dawson.document.models.GetDocumentRequest;
import com.dawson.document.models.GetDocumentResponse;
import com.dawson.document.models.ShareDocumentRequest;
import com.dawson.document.models.ShareDocumentResponse;
import com.dawson.document.models.UpdateDocumentRequest;
import com.dawson.document.models.UpdateDocumentResponse;
import com.dawson.document.services.DocumentService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping("/create")
	public ResponseEntity<CreateDocumentResponse> createDocument(@RequestBody CreateDocumentRequest request) {
		log.info("Create document request received : {}", request);
		return ResponseEntity.ok(documentService.createDocument(request));
	}

	@PostMapping("/update")
	public ResponseEntity<UpdateDocumentResponse> updateDocument(@RequestBody UpdateDocumentRequest request) {
		log.info("Update document request received : {}", request);
		return ResponseEntity.ok(documentService.updateDocument(request));
	}

	@PostMapping("/delete")
	public ResponseEntity<DeleteDocumentResponse> deleteDocument(@RequestBody DeleteDocumentRequest request) {
		log.info("Delete document request received : {}", request);
		return ResponseEntity.ok(documentService.deleteDocument(request));
	}

	@PostMapping("/get")
	public ResponseEntity<GetDocumentResponse> getDocument(@RequestBody GetDocumentRequest request) {
		log.info("Get document request received : {}", request);
		return ResponseEntity.ok(documentService.getDocument(request));
	}

	@PostMapping("/fetch-by-user")
	public ResponseEntity<FetchDocumentResponse> fetchDocuments(@RequestBody FetchDocumentsRequest request) {
		log.info("Get document request received : {}", request);
		return ResponseEntity.ok(documentService.fetchDocument(request));
	}

	@PostMapping("/share")
	public ResponseEntity<ShareDocumentResponse> shareDocument(@RequestBody ShareDocumentRequest request) {
		log.info("Share document request received : {}", request);
		return ResponseEntity.ok(documentService.shareDocument(request));
	}

	@PostMapping("/share/get")
	public ResponseEntity<GetDocumentResponse> shareDocument(@RequestBody FetchShareDocumentsRequest request) {
		log.info("Get Share document request received : {}", request);
		return ResponseEntity.ok(documentService.fetchShareDocument(request));
	}

}
