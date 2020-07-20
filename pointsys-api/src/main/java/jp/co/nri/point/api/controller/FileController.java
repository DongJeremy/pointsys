package jp.co.nri.point.api.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jp.co.nri.point.annotation.OperationLog;
import jp.co.nri.point.api.service.FileStorageService;
import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.FileStorage;
import jp.co.nri.point.pagination.PaginationHandler;
import jp.co.nri.point.util.FileSizeUtil;

@Tag(name = "文件处理")
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "上传文件")
    @PostMapping("/uploadFile")
    public ResultBean<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUuid = UUID.randomUUID().toString();
        String fileName = fileStorageService.storeFile(file, fileUuid);

        // update database
        String fileSize = FileSizeUtil.formatFileSize(file.getSize());
        FileStorage targetFileStorage = new FileStorage(fileName, fileUuid, fileSize, file.getContentType());
        long effectRow = fileStorageService.save(targetFileStorage);
        if (effectRow == 0) {
            return ResultBean.errorResult("update employee fail.");
        }

        return ResultBean.successResult(targetFileStorage);
    }

    @Operation(summary = "上传多个文件")
    @PostMapping("/uploadMultipleFiles")
    public ResultBean<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return ResultBean.successResult(
                Arrays.asList(files).stream().map(file -> uploadFile(file).getData()).collect(Collectors.toList()));
    }

    @Operation(summary = "下载文件")
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @OperationLog("获取文件列表")
    @Operation(summary = "获取文件列表")
    @GetMapping
    public PaginationResponse listFiles(PaginationRequest request) {
        int offset = request.getStart() / request.getLength() + 1;
        PaginationResponse pageResponse = new PaginationHandler(req -> fileStorageService.count(req.getParams()),
                req -> fileStorageService.list(req.getParams(), offset, req.getLength())).handle(request);
        return pageResponse;
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    public ResultBean<?> deleteEmployee(@PathVariable String id) {
        fileStorageService.deleteByUuid(id);
        logger.info("delete employee successful.");
        return ResultBean.successResult();
    }
}
