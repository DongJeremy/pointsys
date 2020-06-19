package jp.co.nri.point.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import jp.co.nri.point.annotation.ExcelInfo;

public class ExcelUtil<T> {

    private static final String TITLE_FONT_TYPE = "Arial Unicode MS";
    private static final short TITLE_FONT_SIZE = 12;
    private static final String TITLE_BACK_COLOR = "C1FBEE";

    private static final String CONTENT_FONT_TYPE = "Arial Unicode MS";
    private static final short CONTENT_FONT_SIZE = 12;

    /**
     * 创建单元格
     * 
     * @param row
     * @param c
     * @param cellValue
     * @param style
     */
    private static void createCell(Row row, int c, String cellValue, CellStyle style) {
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
    }

    private static HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle = (HSSFCellStyle) setFontAndBorder(wb, titleStyle, TITLE_FONT_TYPE, (short) TITLE_FONT_SIZE);
        titleStyle = (HSSFCellStyle) setColor(wb, titleStyle, TITLE_BACK_COLOR, (short) 10);
        // 居中显示
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        return titleStyle;
    }

    private static HSSFCellStyle createContentStyle(HSSFWorkbook wb) {
        HSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle = (HSSFCellStyle) setFontAndBorder(wb, dataStyle, CONTENT_FONT_TYPE, (short) CONTENT_FONT_SIZE);
        // 居中显示
        return dataStyle;
    }

    /**
     * EXCEL表格导出
     * 
     * @param titles
     * @param list
     * @return
     */
    private final static <T> Workbook export(List<T> dataList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        sheet.autoSizeColumn(1);
        try {
            // 创建表头行
            Row row = sheet.createRow(0);
            // 设置样式
            HSSFCellStyle titleStyle = createTitleStyle(wb);
            // 获取实体所有属性
            Field[] fields = dataList.get(0).getClass().getDeclaredFields();
            // 列索引
            int index = 0;
            // 列名称
            String name = "";
            ExcelInfo excelInfo;
            // 创建表头
            for (Field f : fields) {
                // 是否是注解
                if (f.isAnnotationPresent(ExcelInfo.class)) {
                    // 获取注解
                    excelInfo = f.getAnnotation(ExcelInfo.class);
                    // 获取列索引
                    index = excelInfo.columnIndex();
                    // 列名称
                    name = excelInfo.columnName();
                    // 创建单元格
                    createCell(row, index, name, titleStyle);
                }
            }

            // 通过反射获取数据并写入到excel中
            if (dataList != null && dataList.size() > 0) {
                HSSFCellStyle dataStyle = createContentStyle(wb);
                int rowIndex = 1;
                for (Object obj : dataList) {
                    // 创建新行，索引加1,为创建下一行做准备
                    row = sheet.createRow(rowIndex++);
                    for (Field f : fields) {
                        // 设置属性可访问
                        f.setAccessible(true);
                        // 判断是否是注解
                        if (f.isAnnotationPresent(ExcelInfo.class)) {
                            // 获取注解
                            excelInfo = f.getAnnotation(ExcelInfo.class);
                            // 获取列索引
                            index = excelInfo.columnIndex();
                            try {
                                // 创建单元格 f.get(obj)从obj对象中获取值设置到单元格中
                                createCell(row, index, String.valueOf(f.get(obj)), dataStyle);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param is
     * @param out
     * @throws IOException
     */
    private static void write(InputStream is, ServletOutputStream out) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    /**
     * 
     * @param filename
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getFileName(String filename) throws UnsupportedEncodingException {
        String fileNameString = filename == null || filename.equals("") ? "一覧" : filename;
        return new String((fileNameString + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
    }

    /**
     * 设置字体并加外边框
     * 
     * @param style 样式
     * @param style 字体名
     * @param style 大小
     * @return
     */
    private static CellStyle setFontAndBorder(HSSFWorkbook workbook, CellStyle style, String fontName, short size) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
        font.setBold(false);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN); // 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderTop(BorderStyle.THIN);// 上边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        return style;
    }

    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     * 
     * @param style 保证style统一
     * @param color 颜色：66FFDD
     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    private static CellStyle setColor(HSSFWorkbook workbook, CellStyle style, String color, short index) {
        if (color != "" && color != null) {
            // 转为RGB码
            int r = Integer.parseInt((color.substring(0, 2)), 16); // 转为16进制
            int g = Integer.parseInt((color.substring(2, 4)), 16);
            int b = Integer.parseInt((color.substring(4, 6)), 16);
            // 自定义cell颜色
            HSSFPalette palette = workbook.getCustomPalette();
            palette.setColorAtIndex((short) index, (byte) r, (byte) g, (byte) b);

            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(index);
        }
        return style;
    }

    /**
     * export to excel
     * 
     * @param list
     * @param excelFileName
     * @param response
     * @throws IOException
     */
    public final static <T> void exportToFile(List<T> list, String excelFileName, HttpServletResponse response)
            throws IOException {
        String fileName = getFileName(excelFileName);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        export(list).write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        write(is, out);
    }

    public final static <T> ByteArrayResource exportToFile(List<T> list) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        export(list).write(os);
        byte[] content = os.toByteArray();
        return new ByteArrayResource(content);
    }

    /**
     * 获取workbook
     * 
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    private static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        // 根据excel文件版本获取工作簿
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            wb = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("解析的文件格式有误!");
        }
        return wb;
    }

    /**
     * 
     * @param obj
     * @param f
     * @param wookbook
     * @param cell
     */
    private static void setFieldValue(Object obj, Field f, Workbook wookbook, Cell cell) {
        try {
            // cell.setCellFormula(CellType.STRING);
            if (f.getType() == byte.class || f.getType() == Byte.class) {
                f.set(obj, Byte.parseByte(cell.getStringCellValue()));
            } else if (f.getType() == int.class || f.getType() == Integer.class) {
                f.set(obj, Integer.parseInt(cell.getStringCellValue()));
            } else if (f.getType() == long.class || f.getType() == Long.class) {
                f.set(obj, Long.parseLong(cell.getStringCellValue()));
            } else if (f.getType() == Double.class || f.getType() == double.class) {
                f.set(obj, Double.parseDouble(cell.getStringCellValue()));
            } else if (f.getType() == BigDecimal.class) {
                f.set(obj, new BigDecimal(cell.getStringCellValue()));
            } else {
                f.set(obj, cell.getStringCellValue());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * EXCEL表格导入
     * 
     * @param <T>
     * @param file
     * @param clazz
     * @return
     * @throws IOException
     * @throws Exception
     */
    public final static <T> List<T> importFromFile(MultipartFile file, Class<T> clazz) throws IOException, Exception {
        // 存储excel数据
        List<T> list = new ArrayList<>();

        Workbook workbook = getWorkbook(file.getInputStream(), file.getOriginalFilename());
        // 得到一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取行总数
        int rows = sheet.getLastRowNum() + 1;

        Row row;
        // 获取类所有属性
        Field[] fields = clazz.getDeclaredFields();

        T obj = null;
        int coumnIndex = 0;
        Cell cell = null;
        ExcelInfo excelInfo = null;

        for (int i = 1; i < rows; i++) {
            // 获取excel行
            row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            // 创建实体
            obj = clazz.getDeclaredConstructor().newInstance();
            for (Field f : fields) {
                // 设置属性可访问
                f.setAccessible(true);
                // 判断是否是注解
                if (f.isAnnotationPresent(ExcelInfo.class)) {
                    // 获取注解
                    excelInfo = f.getAnnotation(ExcelInfo.class);
                    // 获取列索引
                    coumnIndex = excelInfo.columnIndex();
                    // 获取单元格
                    cell = row.getCell(coumnIndex);
                    // 设置属性
                    setFieldValue(obj, f, workbook, cell);
                }
            }
            // 添加到集合中
            list.add(obj);
        }
        try {
            // 释放资源
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
