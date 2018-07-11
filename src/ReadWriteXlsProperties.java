/**
 * Created by maxdk9 on 12.08.2017.
 */
import java.io.InputStream;
import java.util.Arrays;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.hssf.model.Workbook;

import org.apache.poi.hssf.record.formula.functions.Row;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ReadWriteXlsProperties {

    HashMap< String, String > propMap = new HashMap< String, String >();

    public static void main(String[] args) {

        //writePropertiesFileToXLS();
        readMyExcelAndSaveToPropertiesFile();
    }


    private static void readMyExcelAndSaveToPropertiesFile() {
        ReadWriteXlsProperties readWriteXlsDemo = new ReadWriteXlsProperties();
        readWriteXlsDemo.readExcel("E:\\1\\result.xls");
        readWriteXlsDemo.writePropertiesToPropertiesFile("E:\\1\\stringresources_ru.properties");


    }



    private  void writePropertiesToPropertiesFile(String pathToPropertiesFile) {
        Properties properties = new Properties();
        for (String key : propMap.keySet()) {
            properties.put(key, propMap.get(key));
        }


        FileOutputStream fr = null;
        try {
            fr = new FileOutputStream(pathToPropertiesFile);
            properties.store(fr, "Properties");
            fr.close();
            System.out.println("After saving properties: " + properties);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void readMyExcel() {
        ReadWriteXlsProperties readWriteXlsDemo = new ReadWriteXlsProperties();
        readWriteXlsDemo.readExcel("D:\\1\\result.xls");
    }

    private static void writePropertiesFileToXLS() {
        ReadWriteXlsProperties readWriteXlsDemo = new ReadWriteXlsProperties();

        // Call method readProperties() it take path to properties file
        readWriteXlsDemo.readProperties("D:\\1\\stringresources_en.properties");

        // Call method writeToExcel() it will take path to excel file
        readWriteXlsDemo.writeToExcel("D:\\1\\test.xls");
    }

    private void readProperties(String propertiesFilePath) {

        // Create a File object taking in path of properties
        // file
        File propertiesFile = new File(propertiesFilePath);

        // If properties file is a file do below stuff
        if(propertiesFile.isFile())
        {
            try
            {
                // Create a FileInputStream for loading the properties file
                FileInputStream fisProp = new FileInputStream(propertiesFile);

                // Create a Properties object and load
                // properties key and value to it through FileInputStream
                Properties properties = new Properties();
                properties.load(fisProp);

                // Create a object of Enumeration and call keys()
                // method over properties object created above
                // it will return us back with a Enumeration types
                Enumeration< Object > keysEnum = properties.keys();

                // Looping over the elements of Enumeration
                while(keysEnum.hasMoreElements())
                {
                    // Extracting the key and respective values from it.
                    String propKey = (String)keysEnum.nextElement();
                    String propValue = (String)properties.getProperty(propKey);

                    // After extracting the key and value from the properties file
                    // we will store the values in a HashMap.
                    propMap.put( propKey.toLowerCase().trim(),propValue.trim());

                }

                // printing the HashMap and closing the file FileInputStream
                System.out.println("Properties Map ... \n" +  propMap);
                fisProp.close();

            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

    }


    private  void readExcel(String excelPath) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            workbook =new HSSFWorkbook(new FileInputStream(excelPath)) ;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        HSSFSheet myExcelSheet = workbook.getSheet("Properties");
        HSSFRow row = myExcelSheet.getRow(0);
        propMap.clear();

        Iterator<HSSFRow> rowIterator = myExcelSheet.iterator();
        while (rowIterator.hasNext()) {
           HSSFRow tekrow= rowIterator.next();
           String key=tekrow.getCell(0).getStringCellValue();
            HSSFCell cell = tekrow.getCell(2);
            String value = "";
            if (cell != null) {
                value=tekrow.getCell(2).getStringCellValue();
            }

            propMap.put(key, value);


        }


        for (String key : propMap.keySet()) {
            System.out.println("Key= " + key+";");
            System.out.println("Value= " + propMap.get(key) );
        }


    }

    private void writeToExcel(String excelPath) {

        // Create a Workbook using HSSFWorkbook object
        HSSFWorkbook workBook = new HSSFWorkbook();

        // Create a sheet with name "properties" by
        // the createSheet method of the Workbook
        HSSFSheet worksheet = workBook.createSheet("Properties");

        // Create a row by calling createRow method of the
        // Worksheet
        HSSFRow row = worksheet.createRow((short) 0);

        // Create a cell style by calling createCellStyle()
        // from the workbook
        HSSFCellStyle cellStyle = workBook.createCellStyle();

        // setting of the foreground and fill pattern by calling methods
        // of HSSFCellStyle as setFillForegroundColor() and setFillPattern()

        cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // Create a HSSFCell from the row object created above
        HSSFCell cell1 = row.createCell(0);

        // Setting the value of the cell as the keys by calling
        // setCellValue() method over the HSSFCell
        cell1.setCellValue(new HSSFRichTextString("Keys"));

        // Giving it the style created above.
        cell1.setCellStyle(cellStyle);

        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue(new HSSFRichTextString("Values"));
        cell2.setCellStyle(cellStyle);

        // Create a Iterator and as  propMap is a HashMap
        // it is converted to a HashSet by calling keySet() method
        // which will return with Set.
        // Iterator object is pointed to keys of Set
        Iterator< String > iterator = propMap.keySet().iterator();

        // Looping across the elements of Iterator
        while(iterator.hasNext())
        {
            // Creating a new row from the worksheet
            // at the last used row + 1 location
            HSSFRow rowOne = worksheet.createRow(worksheet.getLastRowNum()+1);

            // Creating two cells in the row at 0 and 1 position.
            HSSFCell cellZero = rowOne.createCell(0);
            HSSFCell cellOne = rowOne.createCell(1);

            // extracting key and value from the map and set
            String key = (String) iterator.next();
            String value = (String) propMap.get(key);

            // setting the extracted keys and values in the cells
            cellZero.setCellValue(new HSSFRichTextString(key));
            cellOne.setCellValue(new HSSFRichTextString(value));

        }

        try{

            FileOutputStream fosExcel =null;

            // Creating a xls File
            File fileExcel = new File(excelPath);

            // Setting the File to FileOutputStream
            fosExcel = new FileOutputStream(fileExcel);

            // Writing the contents of workbook to the xls
            workBook.write(fosExcel);

            // Flushing the FileOutputStream
            fosExcel.flush();
            // Closing the FileOutputStream
            fosExcel.close();

        }catch(Exception e){

            e.printStackTrace();

        }
    }

}
