package uz.e_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.e_store.entity.Attachment;
import uz.e_store.entity.AttachmentContent;
import uz.e_store.repository.AttachmentContentRepository;
import uz.e_store.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Value("${file.saved.url}")
    private String uploadFolder;

    @Value("${file.saved.type}")
    private String savedType;

    public List<Attachment> uploadFile(List<MultipartFile> photos) {
        List<Attachment> attachments = new ArrayList<>();
        if (photos != null) {
            photos.stream().forEach(photo -> {
                Date date = new Date();
                File folder = new File(String.format("%s/%d/%d/%d", uploadFolder, 1900 + date.getYear(), 1 + date.getMonth(), date.getDate()));
                if (!folder.exists() && folder.mkdirs()) {
                    System.out.println("folder created!!!");
                }
                if (savedType.equals("database")) {
                    Attachment attachment = new Attachment(photo.getOriginalFilename(), photo.getContentType(), getExt(photo.getOriginalFilename()), photo.getSize());
                    Attachment save = attachmentRepository.save(attachment);
                    try {
                        AttachmentContent attachmentContent = new AttachmentContent(save, photo.getBytes());
                        attachmentContentRepository.save(attachmentContent);
                        attachments.add(save);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Attachment attachment = new Attachment(
                            photo.getOriginalFilename(),
                            uploadFolder,
                            photo.getContentType(),
                            getExt(photo.getOriginalFilename()),
                            photo.getSize()
                    );
                    Attachment savedAttachment = attachmentRepository.save(attachment);
                    savedAttachment.setUrl(String.format("%d/%d/%d/%s.%s", 1900 + date.getYear(), 1 + date.getMonth(), date.getDate(),
                            savedAttachment.getId(),
                            savedAttachment.getExtension()
                    ));
                    folder = folder.getAbsoluteFile();
                    File file1 = new File(folder, String.format("%s.%s", savedAttachment.getId(), savedAttachment.getExtension()));
                    try {
                        photo.transferTo(file1);
                        attachments.add(attachmentRepository.save(savedAttachment));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return attachments.size() > 0 ? attachments : null;
    }

    public ResponseEntity<?> getFile(UUID id) {

        Optional<Attachment> attachment = attachmentRepository.findById(id);
        if (attachment.isPresent()) {
            if (savedType.equals("database")) {
                Optional<AttachmentContent> attachmentContent = attachmentContentRepository.findByAttachmentId(id);
                if (attachmentContent.isPresent()) {
                    return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.get().getContentType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.get().getName() + "\"")
                            .body(attachmentContent.get().getContent());
                } else {
                    throw new IllegalArgumentException("Data not found with photo id!");
                }
            } else {
                FileUrlResource fileUrlResource = null;
                try {
                    fileUrlResource = new FileUrlResource(String.format("%s/%s", uploadFolder, attachment.get().getUrl()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(attachment.get().getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.get().getName() + "\"")
                        .body(fileUrlResource);
            }
        } else {
            throw new IllegalArgumentException("Data not found with photo id!");
        }

    }

    private String getExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
