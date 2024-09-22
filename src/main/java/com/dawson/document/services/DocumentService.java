package com.dawson.document.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawson.document.entities.DocumentData;
import com.dawson.document.entities.ShareDocumentData;
import com.dawson.document.entities.User;
import com.dawson.document.models.CollaborateDocumentRequest;
import com.dawson.document.models.CollaborateDocumentResponse;
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
				throw new RuntimeException("User not found");
			}
			User userData = user.get();

			DocumentData document = DocumentData.builder().id("DOC" + RandomStringUtils.randomAlphabetic(20))
					.title(request.getTitle()).content("").ownerId(request.getOwnerId()).version("1")
					.createdAt(new Timestamp(System.currentTimeMillis()).toString()).updatedAt(null).isCollaborate("N")
					.build();

			HashMap<String, String> documents = userData.getDocuments();
			documents.put(document.getId(), document.getTitle());
			userData.setDocuments(documents);

			userRepository.save(userData);
			repository.save(document);
			log.info("Document created Successfully");
			return CreateDocumentResponse.builder().documentId(document.getId()).version(document.getVersion())
					.status("200").message("Document Created Successfully").build();
		} catch (Exception e) {
			log.error("Exception while creating document ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public UpdateDocumentResponse updateDocument(UpdateDocumentRequest request) {
		try {
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException("Document not found");
			}

			int version = Integer.parseInt(documentData.get().getVersion()) + 1;
			DocumentData document = DocumentData.builder().id(request.getDocumentId()).title(request.getTitle())
					.content(request.getContent()).ownerId(documentData.get().getOwnerId())
					.version(Integer.toString(version)).createdAt(documentData.get().getCreatedAt())
					.updatedAt(new Timestamp(System.currentTimeMillis()).toString()).build();

			repository.save(document);
			log.info("Document updated Successfully");
			return UpdateDocumentResponse.builder().documentId(document.getId()).version(document.getVersion())
					.status("200").message("Document Updated Successfully").build();
		} catch (Exception e) {
			log.error("Exception while updating document ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public CollaborateDocumentResponse collaborateDocument(CollaborateDocumentRequest request) {
		try {
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException("Document not found");
			}

			DocumentData document = documentData.get();
			List<String> users = request.getUsers();
			if (users == null) {
				users = new ArrayList<String>();
			}

			if (request.getIsCollaborate().equals("Y")) {
				users.stream().forEach(user -> {
					Optional<User> repoUser = userRepository.findById(user);
					if (!repoUser.isPresent()) {
						log.info("User with user ID : {} not found", user);
						throw new RuntimeException("User Not Found");
					}
					User foundUser = repoUser.get();
					if (foundUser.getCollaborateDocuments() == null
							|| !foundUser.getCollaborateDocuments().containsKey(document.getId())) {
						HashMap<String, String> collabs = foundUser.getCollaborateDocuments() == null
								? new HashMap<String, String>()
								: foundUser.getCollaborateDocuments();
						collabs.put(document.getId(), document.getTitle());
						foundUser.setCollaborateDocuments(collabs);
						userRepository.save(foundUser);
					}
				});

				if (document.getUsers() != null) {
					for (String user : document.getUsers()) {
						if (!users.contains(user)) {
							Optional<User> repoUser = userRepository.findById(user);
							if (!repoUser.isPresent()) {
								log.info("User with user ID : {} not found", user);
								throw new RuntimeException("User Not Found");
							}
							User foundUser = repoUser.get();
							if (foundUser.getCollaborateDocuments() != null
									&& foundUser.getCollaborateDocuments().containsKey(document.getId())) {
								HashMap<String, String> collabs = foundUser.getCollaborateDocuments();
								collabs.remove(document.getId());
								foundUser.setCollaborateDocuments(collabs);
								userRepository.save(foundUser);
							}
						}
					}
				}
			} else {
				document.getUsers().stream().forEach(user -> {
					Optional<User> repoUser = userRepository.findById(user);
					if (!repoUser.isPresent()) {
						log.info("User with user ID : {} not found", user);
						throw new RuntimeException("User Not Found");
					}
					User foundUser = repoUser.get();
					if (foundUser.getCollaborateDocuments() != null
							&& foundUser.getCollaborateDocuments().containsKey(document.getId())) {
						HashMap<String, String> collabs = foundUser.getCollaborateDocuments();
						collabs.remove(document.getId());
						foundUser.setCollaborateDocuments(collabs);
						userRepository.save(foundUser);
					}
				});
				users = null;
			}

			document.setIsCollaborate(request.getIsCollaborate());
			document.setUsers(users);

			repository.save(document);
			log.info("Document updated Successfully");
			return CollaborateDocumentResponse.builder().documentId(document.getId())
					.isCollaborate(document.getIsCollaborate()).collaborateUsers(document.getUsers()).status("200")
					.message("Document Collaborated Successfully").build();
		} catch (Exception e) {
			log.error("Exception while collaborating document", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public GetDocumentResponse getDocument(GetDocumentRequest request) {
		try {
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException("Document not found");
			}

			DocumentData document = documentData.get();

			if (!document.getOwnerId().equals(request.getOwnerId()) && (document.getUsers() == null
					|| (document.getUsers() != null && !document.getUsers().contains(request.getOwnerId())))) {
				log.info("User not Authorized to Access document");
				throw new RuntimeException("User not Authorized to Access document");
			}
			return GetDocumentResponse.builder().content(document.getContent()).createdOn(document.getCreatedAt())
					.documentId(document.getId()).ownerId(document.getOwnerId()).title(document.getTitle())
					.updatedOn(document.getUpdatedAt()).version(document.getVersion()).isShare(document.getIsShare())
					.shareId(document.getShareId()).users(document.getUsers())
					.isCollaborate(document.getIsCollaborate()).status("200").message("Document Loaded")
					.build();
		} catch (Exception e) {
			log.error("Exception while fetching document ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public DeleteDocumentResponse deleteDocument(DeleteDocumentRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getOwnerId());
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());
			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException("User not found");
			}

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException("Document not found");
			}
			User userData = user.get();
			DocumentData doc = documentData.get();

			HashMap<String, String> documents = userData.getDocuments();
			documents.remove(request.getDocumentId());
			userData.setDocuments(documents);

			if(doc.getUsers() != null) {
				for (String docUser : doc.getUsers()) {
					Optional<User> repoUser = userRepository.findById(docUser);
					if (!repoUser.isPresent()) {
						log.info("User with user ID : {} not found", user);
						throw new RuntimeException("User Not Found");
					}
					User foundUser = repoUser.get();
					if (foundUser.getCollaborateDocuments() != null
							&& foundUser.getCollaborateDocuments().containsKey(request.getDocumentId())) {
						HashMap<String, String> collabs = foundUser.getCollaborateDocuments();
						collabs.remove(request.getDocumentId());
						foundUser.setCollaborateDocuments(collabs);
						if (userData.getId().equals(foundUser.getId())) {
							userData.setCollaborateDocuments(collabs);
						}
						userRepository.save(foundUser);
					}
				}
			}

			userRepository.save(userData);
			repository.deleteById(request.getDocumentId());

			return DeleteDocumentResponse.builder().documentId(request.getDocumentId()).ownerId(request.getOwnerId())
					.status("200").message("Document Deleted Successfully").build();
		} catch (Exception e) {
			log.error("Exception while fetching document ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public FetchDocumentResponse fetchDocument(FetchDocumentsRequest request) {
		try {
			Optional<User> user = userRepository.findById(request.getUsername());

			if (!user.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException("User not found");
			}

			User userData = user.get();

			return FetchDocumentResponse.builder().documents(userData.getDocuments())
					.collaborateDocuments(userData.getCollaborateDocuments()).status("200")
					.message("Document Fetched Successfully").build();
		} catch (Exception e) {
			log.error("Exception while fetching documents ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public ShareDocumentResponse shareDocument(ShareDocumentRequest request) {
		try {
			Optional<User> userData = userRepository.findById(request.getOwnerId());
			Optional<DocumentData> documentData = repository.findById(request.getDocumentId());
			if (!userData.isPresent()) {
				log.info("User not found in database");
				throw new RuntimeException("User not found");
			}

			if (!documentData.isPresent()) {
				log.info("Document not found in database");
				throw new RuntimeException("Document not found");
			}
			DocumentData document = documentData.get();
			User user = userData.get();
			HashMap<String, String> shareDs = user.getShareDocuments();

			if (request.getIsShare().equals("Y")) {
				ShareDocumentData shareDocumentData = ShareDocumentData.builder()
						.id("SHAREDOC" + RandomStringUtils.randomAlphabetic(20)).title(document.getTitle())
						.content(document.getContent()).ownerId(request.getOwnerId()).version(document.getVersion())
						.createdAt(document.getCreatedAt()).updatedAt(document.getUpdatedAt())
						.documentId(document.getId()).build();

				document.setIsShare("Y");
				document.setShareId(shareDocumentData.getId());
				if (shareDs == null) {
					shareDs = new HashMap<String, String>();
				}
				shareDs.put(shareDocumentData.getId(), shareDocumentData.getTitle());
				user.setShareDocuments(shareDs);
				userRepository.save(user);
				shareRepository.save(shareDocumentData);
				repository.save(document);

				return ShareDocumentResponse.builder().documentId(document.getId()).ownerId(request.getOwnerId())
						.shareId(shareDocumentData.getId()).isShare(request.getIsShare()).status("200")
						.message("Document Shared Successfully").build();
			}

			Optional<ShareDocumentData> shareData = shareRepository.findById(document.getShareId());
			if (!shareData.isPresent()) {
				log.info("Share Document not found in database");
				throw new RuntimeException("Share Document not found");
			}
			shareDs.remove(document.getShareId());
			user.setShareDocuments(shareDs);
			document.setIsShare("N");
			document.setShareId(null);
			userRepository.save(user);
			shareRepository.deleteById(shareData.get().getId());
			repository.save(document);

			return ShareDocumentResponse.builder().documentId(document.getId()).ownerId(request.getOwnerId())
					.shareId(null).isShare(request.getIsShare()).status("200")
					.message("Share link Disabled Successfully").build();
		} catch (Exception e) {
			log.error("Exception while fetching documents ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public GetDocumentResponse fetchShareDocument(FetchShareDocumentsRequest request) {
		try {
			Optional<ShareDocumentData> data = shareRepository.findById(request.getShareId());

			if (!data.isPresent()) {
				log.info("Shared Document not found in database");
				throw new RuntimeException("Shared Document not found");
			}
			ShareDocumentData document = data.get();

			return GetDocumentResponse.builder().content(document.getContent()).createdOn(document.getCreatedAt())
					.title(document.getTitle()).updatedOn(document.getUpdatedAt()).version(document.getVersion())
					.status("200").message("Document Loaded").build();
		} catch (Exception e) {
			log.error("Exception while fetching documents ", e);
			throw new RuntimeException(e.getMessage());
		}
	}
}
