package com.diegoBermudez;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.*;
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

        final FileInputStream file = createAndOpenCopy(path + "\\SUR.xlsx", path + "\\SURCorrecciones.xlsx");

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
        try (FileOutputStream outputStream = new FileOutputStream(path + "\\SURCorrecciones.xlsx")) {
            System.out.println("escribiendo");
            workbook.write(outputStream);
        }

        createCorrectedExcel(newRows, path+ "\\corregido.xlsx");
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

        row.createCell(15).setCellValue(error);

        return new RowValidation(
                firstName, secondName, firstLastName, secondLastName,
                bornDate, medicine, deliveredDate, cause,
                typeId, idNumber, address, locality,
                subNetwork, orderDate,prioritazed, error
        );
    }


    private static void createCorrectedExcel(List<RowValidation> rows, String path) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Corrected");

        //define a date cell style with the "dd/MM/yyyy" format and font size 9
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        Font dateFont = workbook.createFont();
        dateFont.setFontHeightInPoints((short) 9);
        dateCellStyle.setFont(dateFont);

        //define a general cell style with font size 9
        CellStyle generalCellStyle = workbook.createCellStyle();
        Font generalFont = workbook.createFont();
        generalFont.setFontHeightInPoints((short) 9);
        generalCellStyle.setFont(generalFont);

        //define header style with background color and font size 9
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 9);
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //set column widths
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }
        sheet.setColumnWidth(5, 35 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 35 * 256);
        for (int i = 8; i < 16; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }
        sheet.setColumnWidth(16, 40 * 256);

        //create header row with titles
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Primer nombre", "Segundo nombre", "Primer apellido", "Segundo apellido", "Fecha de nacimiento",
                "Medicina entregada", "Fecha de entrega", "Causa de entrega", "Tipo de documento", "Numero de documento",
                "Direccion", "Localidad", "Subred", "Fecha de orden", "Poblacion priorizada", "Error encontrado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        //populate data rows
        int counter = 1;
        for (final RowValidation rec : rows) {
            Row row = sheet.createRow(counter);

            //set cell values and apply generalCellStyle or dateCellStyle where appropriate
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(rec.firstName());
            cell0.setCellStyle(generalCellStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(rec.secondName());
            cell1.setCellStyle(generalCellStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(rec.lastName());
            cell2.setCellStyle(generalCellStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(rec.secondLastName());
            cell3.setCellStyle(generalCellStyle);

            Cell bornDateCell = row.createCell(4);
            if (rec.bornDate() != null) {
                bornDateCell.setCellValue(convertToDate(rec.bornDate()));
                bornDateCell.setCellStyle(dateCellStyle);
            }

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(rec.medicine());
            cell5.setCellStyle(generalCellStyle);

            Cell deliveredCell = row.createCell(6);
            if (rec.deliveredDate() != null) {
                deliveredCell.setCellValue(convertToDate(rec.deliveredDate()));
                deliveredCell.setCellStyle(dateCellStyle);
            }

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(rec.cause());
            cell7.setCellStyle(generalCellStyle);

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(rec.docType());
            cell8.setCellStyle(generalCellStyle);

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(rec.docNumber());
            cell9.setCellStyle(generalCellStyle);

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(rec.address());
            cell10.setCellStyle(generalCellStyle);

            Cell cell11 = row.createCell(11);
            cell11.setCellValue(rec.localityName());
            cell11.setCellStyle(generalCellStyle);

            Cell cell12 = row.createCell(12);
            cell12.setCellValue(rec.subNwtwork());
            cell12.setCellStyle(generalCellStyle);

            Cell medicineDateCell = row.createCell(13);
            if (rec.medicineDate() != null) {
                medicineDateCell.setCellValue(convertToDate(rec.medicineDate()));
                medicineDateCell.setCellStyle(dateCellStyle);
            }

            Cell cell14 = row.createCell(14);
            cell14.setCellValue(rec.prioritazed());
            cell14.setCellStyle(generalCellStyle);

            Cell cell15 = row.createCell(15);
            cell15.setCellValue(rec.error());
            cell15.setCellStyle(generalCellStyle);

            counter++;
        }

        try (FileOutputStream out = new FileOutputStream(new File(path))) {
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

