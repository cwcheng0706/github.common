/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月16日 下午1:35:39
 */
package com.zy.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.zy.entity.ExcelBean;
import com.zy.util.Constant;
import com.zy.util.ZipCompressorByAnt;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月16日 下午1:35:39
 */
public class ResolveExcelServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(ResolveExcelServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5581778457485035065L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fileName = req.getParameter("fileName");
		String tempFolderName = req.getParameter("tempFolderName");

		if (null == fileName || "".equals(fileName.trim())) {
			logger.error("解析传入的参数fileName为空。。");
		} else {
			fileName = new String(fileName.getBytes("ISO8859-1"), "UTF-8");
		}

		if (null == tempFolderName || "".equals(tempFolderName.trim())) {
			logger.error("解析传入的参数tempFolderName为空。。");
		}

		// 要处理的Excel文件实际路径
		String filePath = this.getServletConfig().getServletContext().getRealPath("/");
		// 工作目录
		String workFolder = filePath + Constant.FOLDER + File.separator + String.valueOf(tempFolderName).trim();

		filePath += Constant.FOLDER + File.separator + String.valueOf(tempFolderName).trim() + File.separator
				+ String.valueOf(fileName).trim();

		logger.debug("需要解析的文件路径: " + filePath);

		File excelFile = new File(filePath);
		if (excelFile.exists()) {

			List<ExcelBean> list = resolve(excelFile);

			//根据资费过滤
			Map<String, List<ExcelBean>> map = filterByPay(list);

			//根据地市及计费属性过滤
			Object[][] objs = filterByCityAndPayProp(map);
			
			String zipFile = workFolder +  File.separator + tempFolderName +".zip";
			workFolder = workFolder + File.separator + "workspace";
			logger.debug("工作目录：" + workFolder);
			
			//生成txt文件
			geneFileByObjs(objs, workFolder,tempFolderName);
			
			//开始打包
			packageZip(workFolder ,zipFile);
			
		} else {
			logger.error("此文件不存在");
		}

		req.getRequestDispatcher("/listExcelServlet").forward(req, resp);

	}
	
	/**
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2014年1月17日 上午10:51:44
	 */
	private void packageZip(String workDir,String zipFile) {

		
		logger.debug("zipFile:" +zipFile);
		logger.debug("workDir:" +workDir);
		
		ZipCompressorByAnt zip = new ZipCompressorByAnt(zipFile);
		zip.compress(workDir);
		
		
	}

	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月16日 下午2:14:24
	 * @param excelFile
	 */
	private List<ExcelBean> resolve(File excelFile) {
		List<ExcelBean> list = new ArrayList<ExcelBean>();
		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		try {
			fis = new FileInputStream(excelFile);
			fs = new POIFSFileSystem(fis);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// first sheet
			HSSFSheet sheet = wb.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();
			HSSFRow row = null;
			HSSFCell cell = null;
			int columnNum = 0;

			String temp = "";

			DecimalFormat df = new DecimalFormat("#");
			// 读取Excel表格
			for (int i = 1; i <= lastRow; i++) { // 行循环
				row = sheet.getRow(i);
				columnNum = row.getLastCellNum();

				ExcelBean bean = new ExcelBean();
				bean.setLine(String.valueOf(i + 1));

				for (int j = 0; j < columnNum; j++) { // 列循环
					temp = "";
					cell = row.getCell(j);

					// System.out.println(cell.getCellType());
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_BLANK:
						temp = "";
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						temp = String.valueOf(df.format(cell.getNumericCellValue()));
						break;
					case HSSFCell.CELL_TYPE_STRING:
						temp = cell.getStringCellValue();
						break;
					default:
						logger.error("行【" + i + "】列【" + j + "】没有匹配相关的类型");
						break;
					}
					temp = temp.trim();

					switch (j) {
					case 0: // 号码
						bean.setNumber(temp);
						break;
					case 1:// 计费属性
						bean.setPayProp(temp);
						break;
					case 2:// 省份
						bean.setProvince(temp);
						break;
					case 3:// 地市
						bean.setCity(temp);
						break;
					case 4:// 主号码
						bean.setMainNumber(temp);
						break;
					case 5:// 资费(元)
						bean.setCharge(temp);
						break;
					case 6:// 业务
						bean.setBusiness(temp);
						break;
					case 7:// 营销日期(yyyy-mm-dd)
						bean.setMarketingDate(temp);
						break;
					case 8:// 营销来源
						bean.setMarktingSource(temp);
						break;
					case 9:// 订购操作员
						bean.setOrderUser(temp);
						break;
					case 10:// 状态
						bean.setState(temp);
						break;
					default:
						break;
					}
				}

				// System.out.println(bean.print());
				list.add(bean);
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}
	
	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月16日 下午3:52:43
	 * @param list
	 * @return
	 */
	private Map<String, List<ExcelBean>> filterByPay(List<ExcelBean> list) {
		Map<String, List<ExcelBean>> ret = null;
		String pay = "";
		if (null != list && 0 < list.size()) {
			ret = new HashMap<String, List<ExcelBean>>();
			for (int i = 0; i < list.size(); i++) {
				pay = "";
				ExcelBean bean = list.get(i);
				pay = bean.getCharge();
				if (null == ret.get(pay)) {
					ret.put(pay, new ArrayList<ExcelBean>());
				}
				ret.get(pay).add(bean);

			}
		}
		return ret;
	}
	
	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月16日 下午4:16:56
	 * @param map
	 */
	private Object[][] filterByCityAndPayProp(Map<String, List<ExcelBean>> map) {
		Object[][] objs = null;
		if (null != map) {
			Set<String> set = map.keySet();
			int keySize = set.size();
			List<ExcelBean> list = null;
			String temp = "";

			if (0 < keySize) {
				objs = new Object[keySize][2];
				int index = 0;
				for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
					String key = iterator.next();
					objs[index][0] = key;

					list = map.get(key);
					temp = "";
					Map<String, List<ExcelBean>> temMap = null;
					if (null != list && 0 < list.size()) {
						temMap = new HashMap<String, List<ExcelBean>>();

						for (int i = 0; i < list.size(); i++) {
							ExcelBean bean = (ExcelBean) list.get(i);

							temp = String.valueOf(bean.getCity()) + "_" + String.valueOf(bean.getPayProp());
							if (null == temMap.get(temp)) {
								temMap.put(temp, new ArrayList<ExcelBean>());
							}
							temMap.get(temp).add(bean);

						}
						objs[index][1] = temMap;
					}
					index++;
				}

			}

		}
		return objs;
	}
	
	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月16日 下午5:01:41
	 * @param objs
	 */
	@SuppressWarnings("unchecked")
	private void geneFileByObjs(Object[][] objs, String workFolder,String tempFolderName) {
		String dirName = "";
		// int total = 0 ;
		File dir = null;
		File file = null;
		if (null != objs && 0 < objs.length) {
//			workFolder = workFolder + File.separator + "workspace";
//			logger.debug("工作目录：" + workFolder);
			for (int i = 0; i < objs.length; i++) {
				dirName = String.valueOf(objs[i][0]);
				Map<String, List<ExcelBean>> map = (Map<String, List<ExcelBean>>) objs[i][1];
				// System.out.println("dirName:" + dirName + "==============");
				// 按资费创建目录
				dir = new File(workFolder + File.separator + dirName);
				if (!dir.exists()) {
					dir.mkdirs();
					logger.debug("目录【" + dir.getPath() + "】创建成功！");
				} else {
					logger.warn("目录【" + dir.getPath() + "】已存在！");
				}

				Set<String> keySet = map.keySet();
				for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
					String key = iterator.next();
					List<ExcelBean> beans = map.get(key);
					if (null != beans) {
						// System.out.println("【" + key + "】--->" +
						// beans.size());

						// 创建处理后的文件 以【地市_计费属性_号码总数.txt】 规则创建的文件名
						file = new File(dir.getPath() + File.separator + key + "_" + beans.size() + ".txt");
						if (!file.exists()) {
							
							writeFile(file, beans);

							logger.debug("文件【" + file.getPath() + "】写入成功！");

						} else {
							logger.error("文件【" + file.getPath() + "】已存在！不进行写入操作！");
						}

					} else {
						logger.error("【" + key + "】 对应的List<ExcelBean>记录为空");
					}
					// total = total + beans.size();
				}
			}
			
		}
		// System.out.println("total: " + total);
	}

	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月17日 上午9:29:41
	 * @param file
	 * @param beans
	 */
	private void writeFile(File file, List<ExcelBean> beans) {
		String nubmber = "";
		Writer out = null;
		BufferedWriter bw = null;
		try {
			out = new FileWriter(file);
			bw = new BufferedWriter(out);
			for (int i = 0; i < beans.size(); i++) {
				nubmber = "";
				ExcelBean bean = beans.get(i);
				nubmber = bean.getNumber();
				if (null != bean && null != nubmber && !"".equals(nubmber)) {
					bw.write(nubmber + "\r\n");
				}
			}

			bw.flush();
		} catch (Exception e) {
			logger.error("写文件【" + file.getPath() + "】出现异常。。。");
			logger.error(e);
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

}
