package com.econetwireless.epay.business.integrations.impl;

import com.econetwireless.epay.business.integrations.api.ChargingPlatform;
import com.econetwireless.epay.business.utils.CallBacksUtils;
import com.econetwireless.epay.domain.SubscriberRequest;
import com.econetwireless.in.webservice.CreditRequest;
import com.econetwireless.in.webservice.IntelligentNetworkService;
import com.econetwireless.utils.enums.ResponseCode;
import com.econetwireless.utils.pojo.INBalanceResponse;
import com.econetwireless.utils.pojo.INCreditRequest;
import com.econetwireless.utils.pojo.INCreditResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ChargingPlatformImplTest {

    @Mock
    private IntelligentNetworkService intelligentNetworkService;

    private ChargingPlatform chargingPlatform;

    private String partnerCode;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        when(intelligentNetworkService.creditSubscriberAccount(any(CreditRequest.class)))
                .thenAnswer(CallBacksUtils.CREDIT_SUBSCRIBER_ACCOUNT_ANSWER);
        when(intelligentNetworkService.enquireBalance(anyString(), anyString()))
                .thenAnswer(CallBacksUtils.ENQUIRE_BALANCE_ANSWER);

        partnerCode = "hot-recharge";

        chargingPlatform = new ChargingPlatformImpl(intelligentNetworkService);
    }

    @Test
    public void testEnquireBalanceWithPartnerCodeAndMsidn() {
        INBalanceResponse response = chargingPlatform.enquireBalance(partnerCode, "772123123");
        assertEquals(response.getNarrative(), "Successful balance enquiry");
        assertEquals(response.getMsisdn(), "772123123");
        assertEquals(response.getResponseCode(), ResponseCode.SUCCESS.getCode());
        assertTrue(response.getAmount() > 0);
    }

    @Test
    public void testEnquireBalanceWithMissingMobileNumber() {
        INBalanceResponse response = chargingPlatform.enquireBalance(partnerCode, null);
        assertEquals(response.getNarrative(), "Invalid request, missing mobile number");
        assertEquals(response.getResponseCode(), ResponseCode.INVALID_REQUEST.getCode());
    }

    @Test
    public void testEnquireBalanceWithMissingPartnerCode() {
        INBalanceResponse response = chargingPlatform.enquireBalance(null, "772123123");
        assertEquals(response.getNarrative(), "Invalid request, missing partner code");
        assertEquals(response.getResponseCode(), ResponseCode.INVALID_REQUEST.getCode());
    }

    @Test
    public void testCreditSubscriberAccount() {
        INCreditRequest creditRequest = new INCreditRequest();
        creditRequest.setPartnerCode(partnerCode);
        creditRequest.setReferenceNumber("REF11210");
        creditRequest.setMsisdn("772213423");
        creditRequest.setAmount(10);
        INCreditResponse inCreditResponse = chargingPlatform.creditSubscriberAccount(creditRequest);

        assertNotNull(inCreditResponse);
        assertEquals(ResponseCode.SUCCESS.getCode(), inCreditResponse.getResponseCode());
        assertTrue(inCreditResponse.getBalance() > 4);
        assertEquals("Successful credit request", inCreditResponse.getNarrative());
    }

}
