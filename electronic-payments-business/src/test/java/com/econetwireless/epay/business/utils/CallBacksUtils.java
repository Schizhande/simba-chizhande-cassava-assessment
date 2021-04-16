package com.econetwireless.epay.business.utils;

import com.econetwireless.epay.domain.SubscriberRequest;
import com.econetwireless.utils.enums.ResponseCode;
import com.econetwireless.utils.pojo.INBalanceResponse;
import com.econetwireless.utils.pojo.INCreditRequest;
import com.econetwireless.utils.pojo.INCreditResponse;
import org.apache.commons.lang3.StringUtils;
import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.Random;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.negate;

public final class CallBacksUtils {

    private static final double BALANCE = 100;

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


}
