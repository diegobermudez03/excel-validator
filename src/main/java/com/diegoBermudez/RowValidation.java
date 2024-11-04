package com.diegoBermudez;

import java.time.LocalDate;

public record RowValidation(
        String firstName,
        String secondName,
        String lastName,
        String secondLastName,
        LocalDate bornDate,
        String medicine,
        LocalDate deliveredDate,
        String cause,
        String docType,
        Long docNumber,
        String address,
        String localityName,
        String subNwtwork,
        LocalDate medicineDate,
        String prioritazed,
        String errorMessage
) {
}
