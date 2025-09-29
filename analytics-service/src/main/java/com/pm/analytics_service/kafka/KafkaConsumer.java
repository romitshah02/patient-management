package com.pm.analytics_service.kafka;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;

import patient.events.PatientEvent;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics="patient",groupId="analytcis-service")
    public void consumeEvent(byte[] event){
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            log.info("Received patient event: [Patientid={},PatientName={},PatientEmail:{}]",
            patientEvent.getPatientId(),patientEvent.getName(),patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error Deserializing event  {}", e.getMessage());
        }
    }
}
