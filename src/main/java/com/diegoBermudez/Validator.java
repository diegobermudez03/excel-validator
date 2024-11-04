package com.diegoBermudez;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Validator {


    //this stores the available causes and also it says for each one if its
    //prioritazed or not
    private Map<String, Boolean> causeAndPrioritazed = new HashMap<>();
    private Map<String, String> identifications = new HashMap<>();
    private Set<String> localities = new HashSet<>();

    Validator(){
        initializeCauses();
        initializeIdentifications();
        initializeLocalities();
    }


    public ImmutablePair<Boolean, ImmutablePair<String, String>> validateNames(Cell firstName, Cell secondName){
        boolean error = false;
        String correctedFirstName = "";
        String correctedSecondName = "";
        if(firstName.getCellType() != CellType.STRING ) error = true;
        else correctedFirstName = firstName.getStringCellValue();
        if(secondName.getCellType() != CellType.STRING && secondName.getCellType() != CellType.BLANK) error = true;
        else correctedSecondName = secondName.getStringCellValue();

        if(correctedFirstName.isEmpty() && !correctedSecondName.isEmpty()){
            correctedFirstName = correctedSecondName;
            correctedSecondName = "";
        }
        return new ImmutablePair<>(
                error,
                new ImmutablePair<String, String>(correctedFirstName, correctedSecondName)
        );
    }

    public ImmutablePair<Boolean, LocalDate> dateValidator(Cell dateCell){
        LocalDate date = null;
        boolean error = true;
        if (HSSFDateUtil.isCellDateFormatted(dateCell)) {
            error = false;
            date = LocalDate.ofInstant(dateCell.getDateCellValue().toInstant(), ZoneId.systemDefault());
        }
        return new ImmutablePair<>(error, date);
    }




    private void initializeCauses(){
        causeAndPrioritazed.put("1. PERSONA MAYOR DE 60 AÑOS", true);
        causeAndPrioritazed.put("2. PERSONA CON ENFERMEDAD CRÓNICA", true);
        causeAndPrioritazed.put("3. PERSONA CON DISCAPACIDAD", true);
        causeAndPrioritazed.put("4. GESTANTE", true);
        causeAndPrioritazed.put("5. USUARIO QUE INTERPUSO PQRS", false);
        causeAndPrioritazed.put("6. OTRO", false);
    }


    private void initializeIdentifications(){
        identifications.put("CC", "Cedula de ciudadania");
        identifications.put("TI", "Tarjeta de identidad");
        identifications.put("RC", "Registro civil");
        identifications.put("CE", "Cedula de extranjeria");
        identifications.put("PEP", "Permiso especial de permanencia");
        identifications.put("DNI", "Documento Nacional de identidad");
        identifications.put("Salvoconducto", "SCR");
        identifications.put("PA", "Pasaporte");
    }

    private void initializeLocalities(){
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
