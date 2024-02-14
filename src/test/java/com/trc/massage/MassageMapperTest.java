package com.trc.massage;

import com.trc.massage.binding.Massage;
import com.trc.massage.binding.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;

public class MassageMapperTest {

    private final MassageMapper mapper = new MassageMapper();

    @Test
    public void given_massage_should_map_available_massage() {
        var massageResponse = getMassageResponse();
        var quoteDate = LocalDate.of(2024, 10, 2);
        var currentDate = LocalDate.of(2024, 2, 14);

        testMapper(massageResponse.getMassages().get(0), quoteDate, currentDate, "Masaje sueco", 25.0, 30);
        testMapper(massageResponse.getMassages().get(1), quoteDate, currentDate, "Masaje de Aromaterapia con luces de colores", 300.0, 85);
        testMapper(massageResponse.getMassages().get(2), quoteDate, currentDate, "Masaje Deportivo 60 minutos", 60.0, 60);
    }

    @Test
    public void given_massage_with_empty_cancellation_policies_should_map_default_cancellation_policies() {
        var massageResponse = getMassageResponse();
        var quoteDate = LocalDate.of(2024, 10, 2);
        var currentDate = LocalDate.of(2024, 2, 14);

        var availableMassage = mapper.mapAvailableMassage(massageResponse.getMassages().get(1), quoteDate, currentDate);

        assertThat(availableMassage.getCancellationPolicies(), hasSize(2));
        assertThat(availableMassage.getCancellationPolicies().get(0), is("Sin gastos de cancelación hasta el 01 oct 2024."));
        assertThat(availableMassage.getCancellationPolicies().get(1), is("Desde el 02 oct 2024: no reembolsable."));
    }

    @Test
    public void given_massage_with_cancellation_policies_should_map_cancellation_policies() {
        var massageResponse = getMassageResponse();
        var quoteDate = LocalDate.of(2024, 10, 2);
        var currentDate = LocalDate.of(2024, 2, 14);

        var availableMassage = mapper.mapAvailableMassage(massageResponse.getMassages().get(7), quoteDate, currentDate);

        assertThat(availableMassage.getCancellationPolicies(), hasSize(5));
        assertThat(availableMassage.getCancellationPolicies().get(0), is("Sin gastos de cancelación hasta el 16 sept 2024."));
        assertThat(availableMassage.getCancellationPolicies().get(1), is("Entre el 17 sept 2024 y el 26 sept 2024: 15.0 EUR."));
        assertThat(availableMassage.getCancellationPolicies().get(2), is("Entre el 27 sept 2024 y el 29 sept 2024: 25.0 EUR."));
        assertThat(availableMassage.getCancellationPolicies().get(3), is("Entre el 30 sept 2024 y el 01 oct 2024: 40.0 EUR."));
        assertThat(availableMassage.getCancellationPolicies().get(4), is("Desde el 02 oct 2024: no reembolsable."));
    }

    private void testMapper(Massage massage, LocalDate quoteDate, LocalDate currentDate, String name, double amount, int minutes) {
        var availableMassage = mapper.mapAvailableMassage(massage, quoteDate, currentDate);

        assertThat(availableMassage.getName(), is(name));
        assertThat(availableMassage.getPrice().getAmount(), is(amount));
        assertThat(availableMassage.getPrice().getCurrency(), is("EUR"));
        assertThat(availableMassage.getDuration(), is(Duration.ofMinutes(minutes)));
    }

    private static Response getMassageResponse() {
        try {
            return ObjectMapperFactory.create().readerFor(Response.class).createParser(MassageMapperTest.class.getResourceAsStream("/massage_response.json")).readValueAs(Response.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
