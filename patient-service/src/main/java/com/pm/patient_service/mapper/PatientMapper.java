package com.pm.patient_service.mapper;

import java.time.LocalDate;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponseDTO;
    }


    public static Patient toModel(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisterDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));

        return patient;
    }

}
