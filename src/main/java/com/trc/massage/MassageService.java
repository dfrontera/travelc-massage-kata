package com.trc.massage;

import com.trc.massage.binding.Response;
import com.trc.massage.model.AvailableMassage;
import com.trc.massage.model.AvailableMassageResponse;
import com.trc.massage.model.AvailablePrice;

import java.time.LocalDate;

public class MassageService {

    private MassageGateway gateway;
    private MassageMapper mapper;

    public MassageService(MassageGateway gateway, MassageMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

    public AvailableMassageResponse getAvailableMassages(LocalDate date) {
        var response = gateway.getMassages(date);
        var availableResponse = new AvailableMassageResponse();
        if (response.getMassages() == null) {
            return availableResponse;
        }
        for (var massage : response.getMassages()) {
            if ("AVAILABLE".equals(massage.getStatus())) {
                availableResponse.getAvailableMassages().add(mapper.mapAvailableMassage(massage, date, LocalDate.now()));
            }
        }
        return availableResponse;
    }

}
