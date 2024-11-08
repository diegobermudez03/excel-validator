package com.diegoBermudez;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Validator {


    //this stores the available causes and also it says for each one if its
    //prioritazed or not
    private Map<String, Boolean> causeAndPrioritazed = new HashMap<>();
    private Map<String, String> identifications = new HashMap<>();
    private Set<String> localities = new HashSet<>();
    private Set<String> subNetworks = new HashSet<>();

    Validator(){
        initializeCauses();
        initializeIdentifications();
        initializeLocalities();
        InitializeSubNetworks();
    }


    public ImmutablePair<String, ImmutablePair<String, String>> validateNames(Cell firstName, Cell secondName){
        String error = "";
        String correctedFirstName = "";
        String correctedSecondName = "";
        if(firstName.getCellType() != CellType.STRING ) error += "Tipo de columna de primer nombre invalido |";
        else correctedFirstName = firstName.getStringCellValue();
        if(secondName.getCellType() != CellType.STRING && secondName.getCellType() != CellType.BLANK) error += "Tipo de columna de segundo nombre invalido |";
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

    public ImmutablePair<String, LocalDate> dateValidator(Cell dateCell){
        LocalDate date = null;
        String error = "";
        if(dateCell.getCellType() == CellType.STRING)
            return new ImmutablePair<>("Fecha en formato texto |", null);
        if (DateUtil.isCellDateFormatted(dateCell)) {
            error = "";
            Date javaDate = dateCell.getDateCellValue();
            if(javaDate != null)
                date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return new ImmutablePair<>(error, date);
    }

    public ImmutablePair<String, String> sentenceValidator(Cell dataCell){
        if(dataCell.getCellType() == CellType.STRING){
            return new ImmutablePair<>("", dataCell.getStringCellValue());
        }
        return new ImmutablePair<>("Columna que deberia contener texto no contiene |", "");
    }

    public ImmutablePair<String, String> causeValidator(Cell dataCell){
        if(dataCell.getCellType() != CellType.STRING){
            return new ImmutablePair<>("La columna causa no es de tipo texto |", "");
        }
        final String cause = dataCell.getStringCellValue();

        if(!causeAndPrioritazed.containsKey(cause)){
            String corrected = causeAndPrioritazed.entrySet().stream()
                    .filter((entrySet)->entrySet.getKey().contains(cause))
                    .map((entrySet)->entrySet.getKey())
                    .findFirst().orElse("");
            return new ImmutablePair<>("La causa ingresada no cumple las reglas |", corrected);
        }
        return new ImmutablePair<>("", cause);
    }

    public ImmutablePair<String, String> idTypeValidator(Cell dataCell){
        if(dataCell.getCellType() != CellType.STRING){
            return new ImmutablePair<>("Tipo de Id no esta en formato general |", "");
        }
        final String id = dataCell.getStringCellValue();

        if(!identifications.containsKey(id.trim())){
            String corrected = identifications.entrySet().stream()
                    .filter((entrySet)->entrySet.getValue().contains(id))
                    .map((entrySet)->entrySet.getValue())
                    .findFirst().orElse("");
            return new ImmutablePair<>("El tipo de ID ingresado no sigue las reglas |", corrected);
        }
        return new ImmutablePair<>("", id);
    }

    public ImmutablePair<String, Long> idNumberValidator(Cell dataCell){
        try{
            if (dataCell.getCellType() == CellType.STRING) {
                return new ImmutablePair<>("", Long.parseLong(dataCell.getStringCellValue()));
            } else if (dataCell.getCellType() == CellType.NUMERIC) {
                return new ImmutablePair<>("", Long.parseLong(String.valueOf((long) dataCell.getNumericCellValue())));
            }
            else{
                return new ImmutablePair<>("Numero de ID no es legible |", 0l);
            }
        }catch (Exception e){
            return new ImmutablePair<>("El numero de ID es ilegible | ", 0l);
        }
    }

    public ImmutablePair<String, String> localityValidator(Cell dataCell){
        if(dataCell.getCellType() != CellType.STRING){
            return new ImmutablePair<>("La localidad no esta en formato texto |", "");
        }
        if(!localities.contains(dataCell.getStringCellValue())){
            String corrected = localities.contains(dataCell.getStringCellValue().toUpperCase()) ? dataCell.getStringCellValue().toUpperCase(): "";
            return new ImmutablePair<>("La localiad no sigue las reglas |", corrected);
        }
        return new ImmutablePair<>("", dataCell.getStringCellValue());
    }

    public ImmutablePair<String, String> subNetworkValidator(Cell dataCell){
        if(dataCell.getCellType() != CellType.STRING){
            return new ImmutablePair<>("La subred no esta en formato general |", "");
        }
        if(!subNetworks.contains(dataCell.getStringCellValue())){
            System.out.println(dataCell.getStringCellValue());
            String corrected = subNetworks.contains(dataCell.getStringCellValue().toUpperCase().trim()) ? dataCell.getStringCellValue().toUpperCase(): "";
            return new ImmutablePair<>("La subred no cumple con las reglas |", corrected);
        }
        return new ImmutablePair<>("", dataCell.getStringCellValue());
    }

    public ImmutablePair<String, String> priorizatedPoblationValidator(Cell dataCell, String cause){
        if(dataCell.getCellType() != CellType.STRING){
            return new ImmutablePair<>("La poblacion priorizada no esta en formato general |", "");
        }
        final String value = dataCell.getStringCellValue().toLowerCase();
        if(!value.equals("si") && !value.equals("no")){
            return new ImmutablePair<>("El valor de poblacion priorizada no sigue las reglas \"Si\" \"No\"", "");
        }
        if(!causeAndPrioritazed.containsKey(cause)){
            return new ImmutablePair<>("Como la causa no es valida entonces no se puede validar poblacion priorizada |", "");
        }
        if(causeAndPrioritazed.get(cause) && value.equals("no")){
            return new ImmutablePair<>("La persona deberia estar en poblacion priorizada por su causa |", "Si");
        }
        if(!causeAndPrioritazed.get(cause) && value.equals("si")){
            return new ImmutablePair<>("La persona NO deberia estar en poblacion priorizada por su causa |", "No");
        }
        return new ImmutablePair<>("", value);
    }


    private void initializeCauses(){
        causeAndPrioritazed.put("1. PERSONA MAYOR DE 60 AÑOS", true);
        causeAndPrioritazed.put("2. PERSONA CON ENFERMEDAD CRÓNICA", true);
        causeAndPrioritazed.put("3. PERSONA CON DISCAPACIDAD", true);
        causeAndPrioritazed.put("4. GESTANTE", true);
        causeAndPrioritazed.put("5. USUARIO QUE INTERPUSO PQRS", false);
        causeAndPrioritazed.put("6. OTRO", false);
    }

    private void InitializeSubNetworks(){
        subNetworks.add("SUR");
        subNetworks.add("NORTE");
        subNetworks.add("SUR OCCIDENTE");
        subNetworks.add("CENTRO ORIENTE");
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
