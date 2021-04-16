package com.econetwireless.epay.business.services.impl;

import com.econetwireless.epay.business.integrations.api.ChargingPlatform;
import com.econetwireless.epay.business.services.api.CreditsService;
import com.econetwireless.epay.business.utils.CallBacksUtils;
import com.econetwireless.epay.dao.subscriberrequest.api.SubscriberRequestDao;
import com.econetwireless.epay.domain.SubscriberRequest;
import com.econetwireless.utils.enums.ResponseCode;
import com.econetwireless.utils.messages.AirtimeTopupRequest;
import com.econetwireless.utils.messages.AirtimeTopupResponse;
import com.econetwireless.utils.pojo.INCreditRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreditsServiceImplTest {

    @Mock
    private ChargingPlatform chargingPlatform;

    @Mock
    private SubscriberRequestDao subscriberRequestDao;

    private CreditsService creditsService;

    private String partnerCode;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        when(chargingPlatform.creditSubscriberAccount(any(INCreditRequest.class)))
                .thenAnswer(CallBacksUtils.SUBSCRIBER_CREDIT_ANSWER);

        when(subscriberRequestDao.save(any(SubscriberRequest.class)))
                .thenAnswer(CallBacksUtils.SUBSCRIBER_REQUEST_ANSWER);

        partnerCode = "hot-recharge";
        creditsService = new CreditsServiceImpl(chargingPlatform, subscriberRequestDao);
    }

    @Test
    public void testAirtimeCreditSuccessWithAmountGreaterThanOneAndPartnerCodeAndMsisdnProvided() {
        final AirtimeTopupRequest airtimeTopupRequest = new AirtimeTopupRequest();
        airtimeTopupRequest.setPartnerCode(partnerCode);
        airtimeTopupRequest.setReferenceNumber("REF111");
        airtimeTopupRequest.setMsisdn("772213423");
        airtimeTopupRequest.setAmount(4);

        AirtimeTopupResponse airtimeTopupResponse = creditsService.credit(airtimeTopupRequest);

        assertNotNull(airtimeTopupResponse);
        assertEquals(ResponseCode.SUCCESS.getCode(), airtimeTopupResponse.getResponseCode());
        assertTrue(airtimeTopupResponse.getBalance() > 4);
        assertEquals("Successful credit request", airtimeTopupResponse.getNarrative());

        verify(chargingPlatform).creditSubscriberAccount(any(INCreditRequest.class));
        verify(subscriberRequestDao, atMost(2)).save(any(SubscriberRequest.class));

    }

    @Test(expected = NullPointerException.class)
    public void testAirtimeCreditFailWithEmptyRequest() {
        AirtimeTopupResponse airtimeTopupResponse = creditsService.credit(null);
        assertNotNull(airtimeTopupResponse);
        assertEquals(ResponseCode.FAILED.getCode(), airtimeTopupResponse.getResponseCode());
        assertEquals("Invalid request, empty credit request", airtimeTopupResponse.getNarrative());

    }
}
