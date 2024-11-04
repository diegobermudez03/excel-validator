package com.diegoBermudez;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class Validator {


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
}
