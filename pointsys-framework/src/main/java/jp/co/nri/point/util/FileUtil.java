package jp.co.nri.point.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

public class FileUtil {

    public static void saveExcelToFile(InputStream is, HttpServletResponse response, String filename)
            throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + URLEncoder.encode(filename, "utf-8") + ".xls");
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}
