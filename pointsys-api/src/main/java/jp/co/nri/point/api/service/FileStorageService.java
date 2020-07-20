package jp.co.nri.point.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import jp.co.nri.point.base.BaseService;
import jp.co.nri.point.domain.FileStorage;

public interface FileStorageService extends BaseService<FileStorage> {

    Resource loadFileAsResource(String fileName);

    String storeFile(MultipartFile file, String targetFilename);

    void deleteByUuid(String uuidString);

}
