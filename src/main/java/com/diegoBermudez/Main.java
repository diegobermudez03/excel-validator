package com.diegoBermudez;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
                .takeWhile((string)->!string.equals("excel_validator"))
                .map((string)-> string + "\\")
                .collect(Collectors.joining()) + "Prueba\\parte_2\\files";

        final FileInputStream file = createAndOpenCopy(path + "\\Capital.xlsx", path + "\\CapitalCorrecciones.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        int counter = 0;
        final List<RowValidation> newRows = new LinkedList<>();
        while(rowIterator.hasNext()){
            counter++;
            Row row = rowIterator.next();
            if(counter == 1) continue;
            //this means that we reached the end of the written file
            if(row.getCell(4).getCellType() == CellType.BLANK && row.getCell(5).getCellType() == CellType.BLANK) break;
            newRows.add(rowValidator(row));
        }
        file.close();
        try (FileOutputStream outputStream = new FileOutputStream(path + "\\CapitalCorrecciones.xlsx")) {
            System.out.println("escribiendo");
            workbook.write(outputStream);
        }

        createCorrectedExcel(newRows);
        workbook.close();
    }


    private static RowValidation rowValidator(Row row){
        final var validator = new Validator();
        ////////////////////////////// VALIDATION
        String error = "";

        //validate names
        ImmutablePair<String, ImmutablePair<String, String>> resultNames =  validator.validateNames(row.getCell(0), row.getCell(1));
        error += resultNames.getLeft();
        final String firstName = resultNames.getRight().getLeft();
        final String secondName = resultNames.getRight().getRight();

        //validate last names
        ImmutablePair<String, ImmutablePair<String, String>> resultLastNames =  validator.validateNames(row.getCell(2), row.getCell(3));
        error += resultLastNames.getLeft();
        final String firstLastName = resultLastNames.getRight().getLeft();
        final String secondLastName = resultLastNames.getRight().getRight();

        //validate born date
        ImmutablePair<String, LocalDate> resultBornDate = validator.dateValidator(row.getCell(4));
        error += resultBornDate.getLeft();
        final LocalDate bornDate = resultBornDate.getRight();

        //validate medicine
        ImmutablePair<String, String> resultMedicine = validator.sentenceValidator(row.getCell(5));
        error += resultMedicine.getLeft();
        final String medicine = resultMedicine.getRight();

        //validate delivered date
        ImmutablePair<String, LocalDate> resultDeliveredDate = validator.dateValidator(row.getCell(6));
        error += resultDeliveredDate.getLeft();
        final LocalDate deliveredDate = resultDeliveredDate.getRight();

        //validate cause
        ImmutablePair<String, String> resultCause = validator.causeValidator(row.getCell(7));
        error += resultCause.getLeft();
        final String cause = resultCause.getRight();

        //validate id type
        ImmutablePair<String, String> resultTypeId = validator.idTypeValidator(row.getCell(8));
        error += resultTypeId.getLeft();
        final String typeId = resultTypeId.getRight();

        //validate id number
        ImmutablePair<String, Long> resultIdNumber = validator.idNumberValidator(row.getCell(9));
        error += resultIdNumber.getLeft();
        final Long idNumber = resultIdNumber.getRight();

        //validate address
        ImmutablePair<String, String> resultAddress = validator.sentenceValidator(row.getCell(10));
        error += resultAddress.getLeft();
        final String address = resultAddress.getRight();

        //validate locality
        ImmutablePair<String, String> resultLocality = validator.localityValidator(row.getCell(11));
        error += resultLocality.getLeft();
        final String locality = resultLocality.getRight();

        //validate subnetwork
        ImmutablePair<String, String> resultSubNetwork = validator.subNetworkValidator(row.getCell(12));
        error += resultSubNetwork.getLeft();
        final String subNetwork = resultSubNetwork.getRight();

        //validate ordered date
        ImmutablePair<String, LocalDate> resultOrderedDate = validator.dateValidator(row.getCell(13));
        error += resultOrderedDate.getLeft();
        final LocalDate orderDate = resultOrderedDate.getRight();

        //validate subnetwork
        ImmutablePair<String, String> resultPrioritazed = validator.priorizatedPoblationValidator(row.getCell(14), cause);
        error += resultPrioritazed.getLeft();
        final String prioritazed = resultPrioritazed.getRight();

        Cell summaryCell = row.createCell(15);
        summaryCell.setCellValue(error);
        row.getCell(2).setCellValue("aaaaaaaaaaaaaaaaaa");
        System.out.println(error);

        return new RowValidation(
                firstName, secondName, firstLastName, secondLastName,
                bornDate, medicine, deliveredDate, cause,
                typeId, idNumber, address, locality,
                subNetwork, orderDate,prioritazed, !error.isEmpty()
        );
    }


    private static void createCorrectedExcel(List<RowValidation> rows, String path){


    }

    private static FileInputStream createAndOpenCopy(String pathOriginal, String pathCopy) throws IOException {
        FileInputStream file = new FileInputStream(pathOriginal);
        FileOutputStream copy = new FileOutputStream(pathCopy);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = file.read(buffer)) != -1) {
            copy.write(buffer, 0, bytesRead);
        }

        file.close();
        copy.close();

        return new FileInputStream(pathCopy);
    }
}

