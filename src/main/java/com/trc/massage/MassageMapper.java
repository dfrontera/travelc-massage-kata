package com.trc.massage;

import com.trc.massage.binding.CancellationPolicy;
import com.trc.massage.binding.Massage;
import com.trc.massage.model.AvailableMassage;
import com.trc.massage.model.AvailablePrice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MassageMapper {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public AvailableMassage mapAvailableMassage(Massage massage, LocalDate quotedDate, LocalDate currentDate) {
        var availableMassage = new AvailableMassage();
        availableMassage.setName(massage.getName());
        availableMassage.setPrice(new AvailablePrice());
        availableMassage.getPrice().setCurrency(massage.getPrice().getCurrency());
        availableMassage.getPrice().setAmount(massage.getPrice().getAmount());
        availableMassage.setDuration(massage.getDuration());

        LocalDate lastFreeCancellationDate = getLastFreeCancellationDate(massage.getCancellationPolicies(), quotedDate);

        availableMassage.getCancellationPolicies().add("Sin gastos de cancelación hasta el %s.".formatted(dateFormatter.format(lastFreeCancellationDate)));

        for (var index = 0; index < massage.getCancellationPolicies().size(); index++) {
            var cancelationPolicy = massage.getCancellationPolicies().get(index);
            var startDate = cancelationPolicy.getDate();
            var endDate = quotedDate.minusDays(1);
            if (massage.getCancellationPolicies().size() > index + 1) {
                endDate = massage.getCancellationPolicies().get(index + 1).getDate().minusDays(1);
            }

            var price = cancelationPolicy.getPrice();
            availableMassage.getCancellationPolicies().add("Entre el %s y el %s: %s %s.".formatted(dateFormatter.format(startDate), dateFormatter.format(endDate), price.getAmount(), price.getCurrency()));
        }


        availableMassage.getCancellationPolicies().add("Desde el %s: no reembolsable.".formatted(dateFormatter.format(quotedDate)));

        return availableMassage;
    }

    private LocalDate getLastFreeCancellationDate(List<CancellationPolicy> cancellationPolicies, LocalDate quotedDate) {
        return cancellationPolicies.stream().map(CancellationPolicy::getDate).min(LocalDate::compareTo).orElse(quotedDate).minusDays(1);
    }
}
