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

    //this stores the available causes and also it says for each one if its
    //prioritazed or not
    private static Map<String, Boolean> causeAndPrioritazed = new HashMap<>();
    private static Map<String, String> identifications = new HashMap<>();
    private static Set<String> localities = new HashSet<>();



    public static void main(String[] args) throws IOException {
        initializeCauses();
        initializeIdentifications();
        initializeLocalities();


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


    private static void initializeCauses(){
        causeAndPrioritazed.put("1. PERSONA MAYOR DE 60 AÑOS", true);
        causeAndPrioritazed.put("2. PERSONA CON ENFERMEDAD CRÓNICA", true);
        causeAndPrioritazed.put("3. PERSONA CON DISCAPACIDAD", true);
        causeAndPrioritazed.put("4. GESTANTE", true);
        causeAndPrioritazed.put("5. USUARIO QUE INTERPUSO PQRS", false);
        causeAndPrioritazed.put("6. OTRO", false);
    }


    private static void initializeIdentifications(){
        identifications.put("CC", "Cedula de ciudadania");
        identifications.put("TI", "Tarjeta de identidad");
        identifications.put("RC", "Registro civil");
        identifications.put("CE", "Cedula de extranjeria");
        identifications.put("PEP", "Permiso especial de permanencia");
        identifications.put("DNI", "Documento Nacional de identidad");
        identifications.put("Salvoconducto", "SCR");
        identifications.put("PA", "Pasaporte");
    }

    private static void initializeLocalities(){
        localities.add("USAQUÉN");
        localities.add("CHAPINERO");
        localities.add("SANTA FE");
        localities.add("SAN CRISTÓBAL");
        localities.add("USME");
        localities.add("TUNJUELITO");
        localities.add("BOSA");
        localities.add("KENNEDY");
        localities.add("FONTIBÓN");
        localities.add("ENGATIVÁ");
        localities.add("SUBA");
        localities.add("BARRIOS UNIDOS");
        localities.add("TEUSAQUILLO");
        localities.add("LOS MÁRTIRES");
        localities.add("ANTONIO NARIÑO");
        localities.add("PUENTE ARANDA");
        localities.add("LA CANDELARIA");
        localities.add("RAFAEL URIBE URIBE");
        localities.add("CIUDAD BOLÍVAR");
        localities.add("SUMAPAZ");
    }
}

