package com.econetwireless.epay.business.services.impl;

import com.econetwireless.epay.business.services.api.PartnerCodeValidator;
import com.econetwireless.epay.business.utils.CallBacksUtils;
import com.econetwireless.epay.dao.requestpartner.api.RequestPartnerDao;
import com.econetwireless.utils.execeptions.EpayException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class PartnerCodeValidatorImplTest {

    @Mock
    RequestPartnerDao requestPartnerDao;

    private PartnerCodeValidator partnerCodeValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(requestPartnerDao.findByCode(anyString()))
                .thenAnswer(CallBacksUtils.REQUEST_PARTNER_ANSWER);

        partnerCodeValidator = new PartnerCodeValidatorImpl(requestPartnerDao);
    }

    @Test(expected = EpayException.class)
    public void testNullPartnerCode() {
        partnerCodeValidator.validatePartnerCode(null);
    }

    @Test
    public void testNoneNullPartnerCode() {
        boolean isValidPartnerCode = partnerCodeValidator.validatePartnerCode("Hot-recharge");
        assertTrue(isValidPartnerCode);
    }

}
