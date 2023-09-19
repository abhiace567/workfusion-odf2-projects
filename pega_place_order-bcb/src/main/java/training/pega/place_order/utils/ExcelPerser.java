package training.pega.place_order.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelPerser {

	public List<Map<String, String>> parseExcel(String filePath) throws IOException {
		List<Map<String, String>> data = new ArrayList<>();

		try (XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(filePath))) {
			XSSFSheet sheet = workBook.getSheetAt(0);
			List<String> headers = Arrays.asList("item", "type", "quantity");

			for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
				Map<String, String> rowData = new HashMap<>();
				Row row = sheet.getRow(rownum);
				for (int cellnum = 0; cellnum < headers.size(); cellnum++) {
					Cell cell = row.getCell(cellnum);
					if (cell == null || cell.getCellType() == CellType.BLANK)
						rowData.put(headers.get(cellnum), "");
					else if(cell.getCellType() == CellType.STRING)
						rowData.put(headers.get(cellnum), cell.getStringCellValue());
					else if (cell.getCellType() == CellType.NUMERIC)
						rowData.put(headers.get(cellnum), "" + (int) cell.getNumericCellValue());
				}

				data.add(rowData);
			}
		} catch (IOException ie) {
			throw new IOException("File not found");
		}

		return data;
	}
	
	public List<Map<String, String>> parseExcel(byte[] file) throws IOException {
		List<Map<String, String>> data = new ArrayList<>();

		try (XSSFWorkbook workBook = new XSSFWorkbook(new ByteArrayInputStream(file))) {
			XSSFSheet sheet = workBook.getSheetAt(0);
			List<String> headers = Arrays.asList("item", "type", "quantity");

			for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
				Map<String, String> rowData = new HashMap<>();
				Row row = sheet.getRow(rownum);
				for (int cellnum = 0; cellnum < headers.size(); cellnum++) {
					Cell cell = row.getCell(cellnum);
					if (cell == null || cell.getCellType() == CellType.BLANK)
						rowData.put(headers.get(cellnum), "");
					else if(cell.getCellType() == CellType.STRING)
						rowData.put(headers.get(cellnum), cell.getStringCellValue());
					else if (cell.getCellType() == CellType.NUMERIC)
						rowData.put(headers.get(cellnum), "" + (int) cell.getNumericCellValue());
				}

				data.add(rowData);
			}
		} catch (IOException ie) {
			throw new IOException("File not found");
		}

		return data;
	}
}