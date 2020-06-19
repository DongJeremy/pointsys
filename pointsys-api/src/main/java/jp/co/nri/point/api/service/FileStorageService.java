package jp.co.nri.point.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    Resource loadFileAsResource(String fileName);

    String storeFile(MultipartFile file);

}
