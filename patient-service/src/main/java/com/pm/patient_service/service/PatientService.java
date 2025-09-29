package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.KafkaProducer;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;

@Service
public class PatientService {

    private PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository,BillingServiceGrpcClient billingServiceGrpcClient,KafkaProducer kafkaProducer){
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientRepository = patientRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with this email "+
            "already exists"+patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO)); 

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id,
    PatientRequestDTO patientRequestDTO){
        
        Patient patient = patientRepository.findById(id).orElseThrow(
            ()-> new PatientNotFoundException("Patient not Found : "+id));
    
        if ( patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),patient.getId())){
            throw new EmailAlreadyExistsException("A patient with this email "+
            "already exists"+patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setEmail(patientRequestDTO.getEmail());

        Patient updatePatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatePatient);
    }

    @DeleteMapping
    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
