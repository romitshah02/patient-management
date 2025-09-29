package com.pm.billing_service.grpc;

import net.devh.boot.grpc.server.service.GrpcService;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase{
    
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class); 

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
    StreamObserver<billing.BillingResponse> responseObserver){
        
        log.info("Create billing account request received {}",billingRequest.toString());

         BillingResponse response = BillingResponse.newBuilder().setAccountId("1234").setStatus("ACTIVE").build();

         responseObserver.onNext(response);
         responseObserver.onCompleted();
    }
}
