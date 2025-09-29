package com.pm.patient_service.grpc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpc.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
     
    public BillingServiceGrpcClient(
        @Value("${billing.service.address:localhost}") String serverAddress,
        @Value("${billing.service.grpc.port:9001}") int serverPort
    ){
        log.info("Connecting to billing service at {} : {}",serverAddress,serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount (String patientId,String name,String email){
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setEmail(email).setName(name).build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        
        log.info("Recevied response from billing service via grpc : {} ",response);

        return response;
    }
}
