package com.trc.massage;

import com.trc.massage.binding.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MassageServiceTest {

    @Test
    public void given_massage_response_should_return_available_massage_response() {
        var date = LocalDate.of(2024, 10, 2);
        var gateway = mock(MassageGateway.class);
        var mapper = mock(MassageMapper.class);
        var service = new MassageService(gateway, mapper);
        when(gateway.getMassages(date)).thenReturn(getMassageResponse("/massage_response.json"));

        var response = service.getAvailableMassages(date);

        assertThat(response, is(notNullValue()));
        assertThat(response.getAvailableMassages(), hasSize(6));
    }

    @Test
    public void given_massage_error_response_should_return_empty_response_massage() {
        var date = LocalDate.of(2024, 10, 2);
        var gateway = mock(MassageGateway.class);
        var mapper = mock(MassageMapper.class);
        var service = new MassageService(gateway, mapper);
        when(gateway.getMassages(date)).thenReturn(getMassageResponse("/error_response.json"));

        var response = service.getAvailableMassages(date);

        assertThat(response.getAvailableMassages(), hasSize(0));
    }

    private static Response getMassageResponse(String fileName) {
        try {
            return ObjectMapperFactory.create().readerFor(Response.class).createParser(MassageServiceTest.class.getResourceAsStream(fileName)).readValueAs(Response.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
