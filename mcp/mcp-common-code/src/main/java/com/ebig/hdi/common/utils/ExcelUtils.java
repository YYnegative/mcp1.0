package com.ebig.hdi.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class ExcelUtils {

    private ExcelUtils() {
    }

    /**
     * 读取excel
     *
     * @param filePath 文件路径
     * @param startrow 读取开始的行数
     * @return 返回一个二维数组（第一维放的是行，第二维放的是列表）
     * @throws Exception
     */
    public static String[][] readExcel(String filePath, int startrow) throws Exception {
        //判断文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件" + filePath + "不存在");
        }
        Sheet sheet = getSheet(filePath);
        String[][] content = getData(startrow, sheet);
        return content;
    }

    /**
     * 读取excel
     *
     * @param is       文件路径
     * @param fileName
     * @param startrow 读取的开始行
     * @return 返回一个二维数组（第一维放的是行，第二维放的是列表）
     * @throws Exception
     */
    public static String[][] readExcelByInput(InputStream is, String fileName, int startrow) throws Exception {
        //文件后缀
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf("."));
        Sheet sheet = null;
        if (".xls".equals(extension)) {//2003
            //获取工作簿
            POIFSFileSystem fs = new POIFSFileSystem(is);
            sheet = new HSSFWorkbook(fs).getSheetAt(0);
        } else if (".xlsx".equals(extension) || ".xlsm".equals(extension)) {
            sheet = new XSSFWorkbook(is).getSheetAt(0);
        } else {
            throw new IOException("文件（" + fileName + ")，无法识别！");
        }
        //获取表单数据
        String[][] content = getData(startrow, sheet);
        return content;
    }

    /**
     * 获取表单数据
     *
     * @param startRow
     * @param sheet
     * @return
     */
    private static String[][] getData(int startRow, Sheet sheet) {
        //得到总行数
        int rowNum = sheet.getLastRowNum() + 1;
        //根据第一行获取行数
        Row row = sheet.getRow(0);
        //获取总列数
        int colNum = row.getPhysicalNumberOfCells();
        //根据行列创建二维数组
        String[][] content = new String[rowNum - startRow][colNum];
        String[] cols = null;
        //循环给二维数组赋值
        for (int i = startRow; i < rowNum; i++) {
            row = sheet.getRow(i);
            cols = new String[colNum];
            for (int j = 0; j < colNum; j++) {
                //获取每个单元格的值
                cols[j] = getCellValue(row.getCell(j));
                //把单元格的值存入二维数组
                content[i - startRow][j] = cols[j];
            }
        }
        return content;
    }

    /**
     * 根据表名获取第一个sheet
     *
     * @param file
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static Sheet getSheet(String file) throws Exception {
        //文件后缀
        String extension = file.lastIndexOf(".") == -1 ? "" : file.substring(file.lastIndexOf("."));
        //创建输入流
        InputStream is = new FileInputStream(file);
        if (".xls".equals(extension)) {//2003
            //获取工作薄
            POIFSFileSystem fs = new POIFSFileSystem(is);
            return new HSSFWorkbook(fs).getSheetAt(0);
        } else if (".xlsx".equals(extension) || ".xlsm".equals(extension)) {
            return new XSSFWorkbook(is).getSheetAt(0);
        } else {
            throw new IOException("文件（" + file + "）,无法识别！");
        }
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = cell.getDateCellValue();
                        result = sdf.format(date);
                    } else {
//                        DecimalFormat df = new DecimalFormat("#");
                        HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                        result = dataFormatter.formatCellValue(cell);
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 函数功能说明 ：根据导出模板获取导出Excel表头 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：lrx <br/>
     * 参数：@param value
     * 参数：@return <br/>
     * return：Map<String[],String[]> <br/>
     *
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String[] getColumnNames(String template) throws IOException {
        if (StringUtil.isEmpty(template)) {
            throw new IOException("请配置导出模板");
        }
        Map<String, String> map = JSON.parseObject(template, new TypeReference<LinkedHashMap>() {
        }, Feature.OrderedField);
        List<String> list = map.entrySet().stream().map(e -> new String(e.getKey())).collect(Collectors.toList());
        return list.stream().toArray(String[]::new);
    }

    /**
     * 函数功能说明 ：根据导出模板获取查询字段名 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：lrx <br/>
     * 参数：@param value
     * 参数：@return <br/>
     * return：Map<String[],String[]> <br/>
     *
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String[] getColumns(String template) throws IOException {
        if (StringUtil.isEmpty(template)) {
            throw new IOException("请配置导出模板");
        }
        Map<String, String> map = JSON.parseObject(template, new TypeReference<LinkedHashMap>() {
        }, Feature.OrderedField);
        List<String> list = map.entrySet().stream().map(e -> new String(e.getValue())).collect(Collectors.toList());
        return list.stream().toArray(String[]::new);
    }

    /**
     * 函数功能说明 ： 导出Excel表,解决浏览器不兼容导致乱码<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：lrx <br/>
     * 参数：@param request
     * 参数：@param filename
     * 参数：@return
     * 参数：@throws IOException <br/>
     * return：String <br/>
     */
    public static String getFilename(HttpServletRequest request, String filename) throws IOException {
        //解决下载文件名称乱码
        String header = request.getHeader("User-Agent").toUpperCase();
        //IE浏览器
        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            filename = URLEncoder.encode(filename, "utf-8");
            //IE下载文件名空格变+号问题
            filename = filename.replace("+", "%20");
        } else {
            //其他浏览器
            filename = new String(filename.getBytes(), "ISO8859-1");
        }
        return filename;
    }

    /**
     * 导出----到固定文件目录
     * 根据传入List数据集合导出Excel表格 生成本地excel
     *
     * @param file        （输出流路径）
     * @param list        任何对象类型的list
     * @param columnNames columnNames（表头名称）
     * @param columns     columns （表头对应的列名）
     * @param sheetName   （sheet名称）
     */
    @SuppressWarnings("rawtypes")
    public static void exportExcelByList(String file, List list, String[] columnNames, String[] columns, String sheetName) {
        OutputStream fos = null;
        try {
            //获取输出流
            fos = new FileOutputStream(file);
            //创建工作簿HSSFWorkbook
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建表单sheet
            HSSFSheet sheet = wb.createSheet(sheetName);
            //创建样式对象
            HSSFCellStyle style = wb.createCellStyle();
            //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
            //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平

            //创建行--表头
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < columnNames.length; i++) {
                //创建列、单元格
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(style);
            }
            //创建数据列
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                //创建行--数据
                HSSFRow listRow = sheet.createRow(i + 1);
                //循环列字段数组
                for (int j = 0; j < columns.length; j++) {
                    //创建列
                    HSSFCell listCell = listRow.createCell(j);
                    //根据反射调用方法
                    Method m = o.getClass().getMethod("get" + upperStr(columns[j]));
                    String value = (String) m.invoke(o);
                    if (value != null) {
                        listCell.setCellValue(value);
                        listCell.setCellStyle(style);
                    } else {
                        listCell.setCellValue("");
                        listCell.setCellStyle(style);
                    }
                    sheet.autoSizeColumn(j + 1, true);
                }
            }
            wb.write(fos);
            System.out.println("生成excel成功：" + file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据传入List数据集合导出Excel表格 返回页面选择保存路径的excel
     *
     * @param response    （响应页面）
     * @param list        数据列表
     * @param columnNames 第一行每列的名
     * @param columns     columnNames对应对象中的属性
     * @param sheetName   工作表名称
     * @param fileName    文件名
     */
    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> list, String[] columnNames, String[] columns, String sheetName, String fileName) {
        OutputStream fos = null;
        try {
            //响应输出流，让用户自己选择保存路径
            response.setCharacterEncoding("UTF-8");
            response.reset();
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", new String(("attachment;filename=" + getFilename(request, fileName) + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls").getBytes(), "utf-8"));
            fos = response.getOutputStream();

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            HSSFCellStyle style = wb.createCellStyle();//样式对象
            //style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < columnNames.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(style);

            }
            for (int i = 0; i < list.size(); i++) {
                HSSFRow listRow = sheet.createRow(i + 1);
                Map<String, Object> map = list.get(i);
                for (int j = 0; j < columns.length; j++) {
                    HSSFCell listCell = listRow.createCell(j);
                    String key = columns[j].substring(columns[j].indexOf(".") + 1, columns[j].length());
                    Object obj = map.get(key);
                    if (obj != null) {
                        listCell.setCellValue(obj.toString());
                        listCell.setCellStyle(style);
                    } else {
                        listCell.setCellValue("");
                        listCell.setCellStyle(style);
                    }
                    sheet.autoSizeColumn(j + 1, true);//自适应，从1开始
                }
            }
            //把工作薄写入到输出流
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把输入字符串的首字母改为大写
     *
     * @param str
     * @return
     */
    private static String upperStr(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 海量数据导出 100万以上
     *
     * @param response    直接响应到浏览器
     * @param list        数据列表
     * @param columnNames 表头数组
     * @param columns     和表头数组对应的字段数组
     * @param sheetName   sheet表单名称
     * @param filename    工作薄名称
     */
    public static void exportBigData(HttpServletResponse response, List list, String[] columnNames, String[] columns, String sheetName, String filename) {
        OutputStream os = null;
        try {
            response.setContentType("application/force-download"); // 设置下载类型
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8") + ".xls"); // 设置文件的名称
            os = response.getOutputStream(); // 输出流
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);//内存中保留 1000 条数据，以免内存溢出，其余写入 硬盘
            //获得该工作区的第一个sheet
            Sheet sheet1 = wb.createSheet(sheetName);
            int excelRow = 0;
            //标题行
            Row titleRow = (Row) sheet1.createRow(excelRow++);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    //明细行
                    Row contentRow = (Row) sheet1.createRow(excelRow++);
                    List<String> reParam = (List<String>) list.get(i);
                    for (int j = 0; j < reParam.size(); j++) {
                        Cell cell = contentRow.createCell(j);
                        cell.setCellValue(reParam.get(j));
                    }
                }
            }
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}