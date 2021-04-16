package com.econetwireless.epay.business.services.impl;

import com.econetwireless.epay.business.services.api.ReportingService;
import com.econetwireless.epay.business.utils.CallBacksUtils;
import com.econetwireless.epay.dao.subscriberrequest.api.SubscriberRequestDao;
import com.econetwireless.epay.domain.SubscriberRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ReportingServiceImplTest {

    @Mock
    private SubscriberRequestDao subscriberRequestDao;

    private ReportingService reportingService;

    private String partnerCode;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        when(subscriberRequestDao.findByPartnerCode(anyString()))
                .thenAnswer(CallBacksUtils.SUCCESSFUL_REQUEST_ANSWER);

        reportingService = new ReportingServiceImpl(subscriberRequestDao);
        partnerCode = "hot-recharge";
    }

    @Test
    public void testFindSubscriberRequestsByPartnerCode_withNullPartnerCode() {
        List<SubscriberRequest> subscriberRequests = reportingService.findSubscriberRequestsByPartnerCode(null);
        assertNotNull(subscriberRequests);
        assertEquals(0, subscriberRequests.size());
    }

    @Test
    public void testFindSubscriberRequestsByPartnerCode_Success() {
        List<SubscriberRequest> subscriberRequests = reportingService.findSubscriberRequestsByPartnerCode(partnerCode);
        assertNotNull(subscriberRequests);
        assertEquals(2, subscriberRequests.size());
    }
}
