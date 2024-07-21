package com.dawson.document.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawson.document.entities.DocumentData;
import com.dawson.document.entities.ShareDocumentData;
import com.dawson.document.entities.User;
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
import com.dawson.document.repositories.DocumentRepository;
import com.dawson.document.repositories.ShareDocumentRepository;
import com.dawson.document.repositories.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DocumentService {
	@Autowired
	private DocumentRepository repository;
	@Autowired
	private ShareDocumentRepository shareRepository;
	@Autowired
	private UserRepository userRepository;

	public CreateDocumentResponse createDocument(CreateDocumentRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getOwnerId());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}
			User userData = user.get();

			DocumentData document = DocumentData.builder().id("DOC" + RandomStringUtils.randomAlphabetic(20))
					.title(request.getTitle()).content("").ownerId(request.getOwnerId()).version("1")
					.createdAt(new Timestamp(System.currentTimeMillis()).toString()).updatedAt(null).build();

			HashMap<String, String> documents = userData.getDocuments();
			documents.put(document.getId(), document.getTitle());
			userData.setDocuments(documents);

			userRepository.save(userData);
			repository.save(document);
			log.info("Document created Successfully");
			return CreateDocumentResponse.builder().documentId(document.getId()).version(document.getVersion()).build();
		} catch (Exception e) {
			log.info("Exception while creating document");
			throw new RuntimeException();
		}
	}

	public UpdateDocumentResponse updateDocument(UpdateDocumentRequest request) {
		try {
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException();
			}

			int version = Integer.parseInt(documentData.get().getVersion()) + 1;
			DocumentData document = DocumentData.builder().id(request.getDocumentId()).title(request.getTitle())
					.content(request.getContent()).ownerId(documentData.get().getOwnerId())
					.version(Integer.toString(version)).createdAt(documentData.get().getCreatedAt())
					.updatedAt(new Timestamp(System.currentTimeMillis()).toString()).build();

			repository.save(document);
			log.info("Document updated Successfully");
			return UpdateDocumentResponse.builder().documentId(document.getId()).version(document.getVersion()).build();
		} catch (Exception e) {
			log.info("Exception while updating document");
			throw new RuntimeException();
		}
	}

	public GetDocumentResponse getDocument(GetDocumentRequest request) {
		try {
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException();
			}

			DocumentData document = documentData.get();

			return GetDocumentResponse.builder().content(document.getContent()).createdOn(document.getCreatedAt())
					.documentId(document.getId()).ownerId(document.getOwnerId()).title(document.getTitle())
					.updatedOn(document.getUpdatedAt()).version(document.getVersion()).isShare(document.getIsShare())
					.shareId(document.getShareId()).build();
		} catch (Exception e) {
			log.info("Exception while fetching document");
			throw new RuntimeException();
		}
	}

	public DeleteDocumentResponse deleteDocument(DeleteDocumentRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getOwnerId());
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException();
			}
			User userData = user.get();

			HashMap<String, String> documents = userData.getDocuments();
			documents.remove(request.getDocumentId());
			userData.setDocuments(documents);

			userRepository.save(userData);
			repository.deleteById(request.getDocumentId());

			return DeleteDocumentResponse.builder().documentId(request.getDocumentId()).ownerId(request.getOwnerId())
					.status("SUCCESS").build();
		} catch (Exception e) {
			log.info("Exception while fetching document");
			throw new RuntimeException();
		}
	}

	public FetchDocumentResponse fetchDocument(FetchDocumentsRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getUsername());

			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}

			User userData = user.get();

			return FetchDocumentResponse.builder().documents(userData.getDocuments()).build();
		} catch (Exception e) {
			log.error(e);
			log.info("Exception while fetching documents");
			throw new RuntimeException();
		}
	}

	public ShareDocumentResponse shareDocument(ShareDocumentRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getOwnerId());
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException();
			}

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException();
			}
			DocumentData document = documentData.get();

			if (request.getIsShare().equals("Y")) {
				ShareDocumentData shareDocumentData = ShareDocumentData.builder()
						.id("SHAREDOC" + RandomStringUtils.randomAlphabetic(20)).title(document.getTitle())
						.content(document.getContent()).ownerId(request.getOwnerId()).version(document.getVersion())
						.createdAt(document.getCreatedAt()).updatedAt(document.getUpdatedAt())
						.documentId(document.getId()).build();

				document.setIsShare("Y");
				document.setShareId(shareDocumentData.getId());
				shareRepository.save(shareDocumentData);
				repository.save(document);

				return ShareDocumentResponse.builder().documentId(document.getId()).ownerId(request.getOwnerId())
						.shareId(shareDocumentData.getId()).isShare(request.getIsShare()).build();
			}

			Optional<ShareDocumentData> shareData = shareRepository.findById(document.getShareId());
			if (!shareData.isPresent()) {
				log.info("Share Document not found in database");
				throw new RuntimeException();
			}

			shareRepository.deleteById(document.getShareId());
			document.setIsShare("N");
			document.setShareId(null);
			repository.save(document);

			return ShareDocumentResponse.builder().documentId(document.getId()).ownerId(request.getOwnerId())
					.shareId(null).isShare(request.getIsShare()).build();
		} catch (Exception e) {
			log.error("", e);
			log.info("Exception while fetching documents");
			throw new RuntimeException();
		}
	}

	public GetDocumentResponse fetchShareDocument(FetchShareDocumentsRequest request) {
		try {
			Optional<ShareDocumentData> data = shareRepository.findById(request.getShareId());

			if (!data.isPresent()) {
				log.info("Shared Document not found in database");
				throw new RuntimeException();
			}
			ShareDocumentData document = data.get();

			return GetDocumentResponse.builder().content(document.getContent()).createdOn(document.getCreatedAt())
					.title(document.getTitle()).updatedOn(document.getUpdatedAt()).version(document.getVersion())
					.build();
		} catch (Exception e) {
			log.error(e);
			log.info("Exception while fetching documents");
			throw new RuntimeException();
		}
	}
}
