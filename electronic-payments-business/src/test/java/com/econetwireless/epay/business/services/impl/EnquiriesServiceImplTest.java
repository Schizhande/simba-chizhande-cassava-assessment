package com.econetwireless.epay.business.services.impl;

import com.econetwireless.epay.business.integrations.api.ChargingPlatform;
import com.econetwireless.epay.business.services.api.EnquiriesService;
import com.econetwireless.epay.business.utils.CallBacksUtils;
import com.econetwireless.epay.dao.subscriberrequest.api.SubscriberRequestDao;
import com.econetwireless.epay.domain.SubscriberRequest;
import com.econetwireless.utils.enums.ResponseCode;
import com.econetwireless.utils.messages.AirtimeBalanceResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class EnquiriesServiceImplTest {

    @Mock
    private ChargingPlatform chargingPlatform;

    @Mock
    private SubscriberRequestDao subscriberRequestDao;

    private EnquiriesService enquiriesService;

    private String partnerCode;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        when(chargingPlatform.enquireBalance(anyString(), anyString()))
                .thenAnswer(CallBacksUtils.SUCCESSFUL_BALANCE_ANSWER);

        when(subscriberRequestDao.save(any(SubscriberRequest.class)))
                .thenAnswer(CallBacksUtils.SUBSCRIBER_REQUEST_ANSWER);

        partnerCode = "hot-recharge";
        enquiriesService = new EnquiriesServiceImpl(chargingPlatform, subscriberRequestDao);
    }

    @Test
    public void testBalanceEnquiryWithPartnerCodeAndMsisdnProvided() {
        AirtimeBalanceResponse response =  enquiriesService.enquire(partnerCode, "771234543");
        assertNotNull(response);
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getResponseCode());
        assertEquals("Successful balance enquiry", response.getNarrative());

        verify(chargingPlatform).enquireBalance(partnerCode,"771234543");
        verify(subscriberRequestDao, atMost(2)).save(any(SubscriberRequest.class));

    }

    @Test
    public void testBalanceEnquiryWithEmptyPartnerCodeAndMsisdnProvided() {
        AirtimeBalanceResponse response =  enquiriesService.enquire("", "");
        assertNotNull(response);
        assertEquals(ResponseCode.INVALID_REQUEST.getCode(), response.getResponseCode());

        verify(chargingPlatform).enquireBalance("","");
        verify(subscriberRequestDao, atMost(2)).save(any(SubscriberRequest.class));

    }

}
