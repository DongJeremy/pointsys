package jp.co.nri.point.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jp.co.nri.point.beans.PaginationResponse;
import jp.co.nri.point.beans.ResultBean;
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

    @GetMapping("/file/download")
    @ResponseBody
    public ResultBean<?> downloadFile(HttpServletResponse response) throws Exception {
        String excelFileName = "Capture001.png";
        try {
            ByteArrayResource result = HttpClientUtil.doExportFile(getTokenString(),
                    getUrlString("/api/v1/files/downloadFile/" + excelFileName), ByteArrayResource.class);
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
            paramsMap.set("username", name);
        }
        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
                getUrlString("/api/v1/files"), paramsMap);
        return response;
    }

//    @GetMapping("/jobs/list")
//    @ResponseBody
//    public PaginationResponse jobList(@RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "status", required = false) String status,
//            @RequestParam(value = "start", defaultValue = "0") int start,
//            @RequestParam(value = "length", defaultValue = "10") int length) {
//        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
//        paramsMap.set("start", String.valueOf(start));
//        paramsMap.set("length", String.valueOf(length));
//        if (status != null) {
//            paramsMap.set("status", status);
//        }
//        if (description != null) {
//            paramsMap.set("description", description);
//        }
//        PaginationResponse response = HttpClientUtil.doGetPageResultBean(restTemplate, getTokenString(),
//                getUrlString("/api/v1/jobs"), paramsMap);
//        return response;
//    }
//
//    @GetMapping("/jobs/beans")
//    @ResponseBody
//    public ResultBean<?> jobBeans() {
//        ResultBean<?> response = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
//                getUrlString("/api/v1/jobs/beans"), List.class);
//        return response;
//    }
//
//    @GetMapping("/jobs/beans/{name}")
//    @ResponseBody
//    public ResultBean<?> jobMethod(@PathVariable String name) {
//        ResultBean<?> response = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
//                getUrlString("/api/v1/jobs/beans/" + name), List.class);
//        return response;
//    }
//
//    @GetMapping("/jobs/cronCheck")
//    @ResponseBody
//    public ResultBean<?> cronCheck(@RequestParam(value = "cron", required = true) String cron) {
//        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
////        ResourceUtils.FILE_URL_PREFIX
////        paramsMap.set("cron", cron);
//        ResultBean<?> response = HttpClientUtil.doGetResultBean(restTemplate, getTokenString(),
//                getUrlString("/api/v1/jobs/cronCheck"), paramsMap);
//        return response;
//    }
//
//    @PostMapping("/jobs/add")
//    @ResponseBody
//    public ResultBean<?> addJob(@RequestBody JobModel jobModel) {
//        return HttpClientUtil.doPostResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/jobs"), jobModel,
//                JobModel.class);
//    }
//
//    @DeleteMapping("/jobs/delete/{id}")
//    @ResponseBody
//    public ResultBean<?> deleteJob(@PathVariable Long id) {
//        return HttpClientUtil.doDeleteResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/jobs/" + id),
//                JobModel.class);
//    }
//
//    @PutMapping("/jobs/update/{id}")
//    @ResponseBody
//    public ResultBean<?> updateJob(@PathVariable Long id, @RequestBody JobModel jobModel) {
//        return HttpClientUtil.doUpdateResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/jobs/" + id),
//                jobModel, JobModel.class);
//    }
//
//    @GetMapping("/jobs/get/{id}")
//    @ResponseBody
//    public ResultBean<?> getJob(@PathVariable(value = "id", required = false) Long id, Model model) {
//        return HttpClientUtil.doGetResultBean(restTemplate, getTokenString(), getUrlString("/api/v1/jobs/" + id),
//                JobModel.class);
//    }

}
