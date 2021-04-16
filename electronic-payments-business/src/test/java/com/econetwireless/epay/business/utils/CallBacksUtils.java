package com.econetwireless.epay.business.utils;

import com.econetwireless.epay.domain.RequestPartner;
import com.econetwireless.epay.domain.SubscriberRequest;
import com.econetwireless.in.webservice.BalanceResponse;
import com.econetwireless.in.webservice.CreditRequest;
import com.econetwireless.in.webservice.CreditResponse;
import com.econetwireless.utils.enums.ResponseCode;
import com.econetwireless.utils.pojo.INBalanceResponse;
import com.econetwireless.utils.pojo.INCreditRequest;
import com.econetwireless.utils.pojo.INCreditResponse;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.negate;

public final class CallBacksUtils {

    private static final double BALANCE = 100;

    public static final Answer<CreditResponse> CREDIT_SUBSCRIBER_ACCOUNT_ANSWER = invocationOnMock -> {
        Object[] arguments = invocationOnMock.getArguments();
        if (arguments != null && arguments.length > 0) {
            CreditRequest creditRequest = (CreditRequest) arguments[0];
            final CreditResponse creditResponse = new CreditResponse();
            if (creditRequest == null) {
                creditResponse.setResponseCode(ResponseCode.FAILED.getCode());
                creditResponse.setNarrative("Invalid request, empty credit request");
                return creditResponse;
            }
            creditResponse.setMsisdn(creditRequest.getMsisdn());
            creditResponse.setBalance(creditRequest.getAmount() + BALANCE);
            creditResponse.setResponseCode(ResponseCode.SUCCESS.getCode());
            creditResponse.setNarrative("Successful credit request");
            return creditResponse;
        }
        return null;
    };

    public static final Answer<BalanceResponse> ENQUIRE_BALANCE_ANSWER = invocationOnMock -> {
        Object[] arguments = invocationOnMock.getArguments();
        if (!isNull(arguments) && arguments.length == 2) {
            String partnerCode = (String) arguments[0];
            String msisdn = (String) arguments[1];
            BalanceResponse balanceResponse = new BalanceResponse();
            if (msisdn == null || msisdn.trim().isEmpty()) {
                balanceResponse.setNarrative("Invalid request, missing mobile number");
                balanceResponse.setResponseCode(ResponseCode.INVALID_REQUEST.getCode());
                return balanceResponse;
            }
            if (partnerCode == null || partnerCode.trim().isEmpty()) {
                balanceResponse.setNarrative("Invalid request, missing partner code");
                balanceResponse.setResponseCode(ResponseCode.INVALID_REQUEST.getCode());
                return balanceResponse;
            }
            balanceResponse.setAmount(BALANCE);
            balanceResponse.setMsisdn(msisdn);
            balanceResponse.setResponseCode(ResponseCode.SUCCESS.getCode());
            balanceResponse.setNarrative("Successful balance enquiry");
            return balanceResponse;
        }
        return null;
    };

    public static final Answer<INCreditResponse> SUBSCRIBER_CREDIT_ANSWER = invocation -> {
        Object[] arguments = invocation.getArguments();
        if (arguments != null && arguments.length > 0) {
            INCreditRequest creditRequest = (INCreditRequest) arguments[0];
            final INCreditResponse creditResponse = new INCreditResponse();
            if (creditRequest == null) {
                creditResponse.setResponseCode(ResponseCode.FAILED.getCode());
                creditResponse.setNarrative("Invalid request, empty credit request");
                return creditResponse;
            }
            creditResponse.setMsisdn(creditRequest.getMsisdn());
            creditResponse.setBalance(creditRequest.getAmount() + BALANCE);
            creditResponse.setResponseCode(ResponseCode.SUCCESS.getCode());
            creditResponse.setNarrative("Successful credit request");
            return creditResponse;
        }
        return null;
    };

    public static final Answer<SubscriberRequest> SUBSCRIBER_REQUEST_ANSWER = invocation -> {
        Object[] arguments = invocation.getArguments();
        if (negate(isNull(arguments)) && arguments.length > 0) {
            SubscriberRequest subscriberRequest = (SubscriberRequest) arguments[0];
            if (isNull(subscriberRequest)) {
                return null;
            }
            if (isNull(subscriberRequest.getId())) {
                subscriberRequest.setId(new Random().nextLong());
            }
            if (isNull(subscriberRequest.getDateCreated())) {
                subscriberRequest.setDateCreated(new Date());
            }
            subscriberRequest.setDateLastUpdated(new Date());
            return subscriberRequest;
        }
        return null;
    };

    public final static Answer<INBalanceResponse> SUCCESSFUL_BALANCE_ANSWER = invocation -> {
        Object[] arguments = invocation.getArguments();
        if (!isNull(arguments) && arguments.length == 2) {
            String partnerCode = (String) arguments[0];
            String msisdn = (String) arguments[1];
            INBalanceResponse balanceResponse = new INBalanceResponse();
            if (partnerCode == null || partnerCode.trim().isEmpty()) {
                balanceResponse.setNarrative("Invalid request, missing partner code");
                balanceResponse.setResponseCode(ResponseCode.INVALID_REQUEST.getCode());
                return balanceResponse;
            }
            if (msisdn == null || msisdn.trim().isEmpty()) {
                balanceResponse.setNarrative("Invalid request, missing mobile number");
                balanceResponse.setResponseCode(ResponseCode.INVALID_REQUEST.getCode());
                return balanceResponse;
            }
            balanceResponse.setAmount(BALANCE);
            balanceResponse.setMsisdn(msisdn);
            balanceResponse.setResponseCode(ResponseCode.SUCCESS.getCode());
            balanceResponse.setNarrative("Successful balance enquiry");
            return balanceResponse;
        }
        return null;
    };

    public static final Answer<List<SubscriberRequest>> SUCCESSFUL_REQUEST_ANSWER = invocationOnMock -> {
        Object[] arguments = invocationOnMock.getArguments();
        if (negate(isNull(arguments)) && arguments.length > 0) {
            List<SubscriberRequest> subscriberRequests = new ArrayList<>();
            String partnerCode = (String) arguments[0];
            if (partnerCode != null) {
                List<String> partners = Arrays.asList("hot-recharge", "paynow");
                return partners.stream()
                        .map(partner -> {
                            SubscriberRequest request = new SubscriberRequest();
                            request.setPartnerCode(partner);
                            request.setDateLastUpdated(new Date());
                            request.setDateCreated(new Date());
                            request.setId(new Random().nextLong());
                            int msisdn = 770_000_000 + new Random().nextInt(789_999_999);
                            request.setMsisdn(String.valueOf(msisdn));
                            request.setBalanceAfter(10);
                            request.setBalanceBefore(5);
                            request.setAmount(5);
                            return request;
                        }).collect(Collectors.toList());
            }
            return subscriberRequests;
        } else {
            return null;
        }
    };

    public static final Answer<RequestPartner> REQUEST_PARTNER_ANSWER = invocationOnMock -> {
        Object[] arguments = invocationOnMock.getArguments();
        if (!isNull(arguments) && arguments.length > 0) {
            String partnerCode = (String) arguments[0];
            if (partnerCode != null) {
                RequestPartner partner = new RequestPartner();
                partner.setId(1L);
                partner.setCode(partnerCode);
                partner.setDescription("This is a test request partner");
                partner.setName("Test Name");
                return partner;
            }
            return null;
        }
        return null;
    };
}
