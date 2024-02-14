package com.trc.massage.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AvailableMassage {

    private String name;
    private AvailablePrice price;
    private Duration duration;
    private List<String> cancellationPolicies = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvailablePrice getPrice() {
        return price;
    }

    public void setPrice(AvailablePrice price) {
        this.price = price;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public List<String> getCancellationPolicies() {
        return cancellationPolicies;
    }
}
