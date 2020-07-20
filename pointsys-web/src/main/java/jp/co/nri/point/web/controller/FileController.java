package jp.co.nri.point.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.domain.Employee;
import jp.co.nri.point.util.FileUtil;
import jp.co.nri.point.web.util.HttpClientUtil;

@Controller
public class FileController extends BaseController {

    @GetMapping("/main/fileView")
    public String mailListView() {
        return "pages/file/file-list";
    }

    @PostMapping("/file/upload")
    public @ResponseBody ResultBean<?> uploadFile(@RequestParam("file") MultipartFile file)
            throws IOException, Exception {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("file", file.getResource());

        return HttpClientUtil.doImportFile(getTokenString(), getUrlString("/api/v1/files/uploadFile"), parts,
                String.class);
    }

    @GetMapping("/file/download/{fileName}")
    @ResponseBody
    public ResultBean<?> downloadFile(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        String excelFileName = "Capture001.png";
        try {
            ByteArrayResource result = HttpClientUtil.doExportFile(getTokenString(),
                    getUrlString("/api/v1/files/downloadFile/" + fileName), ByteArrayResource.class);
            FileUtil.saveInputStreamToFile(result.getInputStream(), response, excelFileName);
        } catch (Exception e) {
            throw e;
        }
        return ResultBean.successResult();
    }

    @GetMapping("/file/list")
    @ResponseBody
    public PaginationResponse fileList(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("start", String.valueOf(start));
        paramsMap.set("length", String.valueOf(length));
        if (name != null) {
            paramsMap.set("name", name);
        }
        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/files"), paramsMap);
        return response;
    }

    @DeleteMapping("/file/delete/{id}")
    @ResponseBody
    public ResultBean<?> deleteEmployee(@PathVariable String id) {
        return HttpClientUtil.doDeleteResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/files/" + id), Employee.class);
    }

    @PostMapping("/file/batch/delete")
    @ResponseBody
    public ResultBean<?> deleteBatchEmployee(@RequestBody List<String> ids) {
        return HttpClientUtil.doPostListResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/files/batch/delete"), ids, String.class);
    }

}
