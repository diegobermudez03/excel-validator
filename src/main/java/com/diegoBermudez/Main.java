package com.diegoBermudez;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    public static void main(String[] args) throws IOException {
        Path currentPath = Paths.get("");
        String projectPath = currentPath.toAbsolutePath().toString();
        final String path = Arrays.stream(projectPath.split("\\\\"))
                .peek(System.out::println)
                .takeWhile((string)->!string.equals("excel_validator"))
                .map((string)-> string + "\\")
                .collect(Collectors.joining()) + "Prueba\\parte_2\\files\\Capital.xlsx";
        System.out.println(path);
        FileInputStream file = new FileInputStream(path);

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.iterator();
            while(cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                switch(cell.getCellType()){
                    case CellType.NUMERIC -> System.out.println("es numerico");
                    //case CellType.
                }
                System.out.println(cell.getCellType());
            }
        }
    }
}

