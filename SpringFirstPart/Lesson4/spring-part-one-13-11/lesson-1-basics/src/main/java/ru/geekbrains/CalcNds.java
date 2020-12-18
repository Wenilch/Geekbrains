package ru.geekbrains;

import ru.geekbrains.nds_provider.NdsProvider;

public class CalcNds {

    private final NdsProvider ndsProvider;

    public CalcNds(NdsProvider ndsProvider) {
        this.ndsProvider = ndsProvider;
    }

    public Double priceWithNds(Double price) {
        return price * (1. + ndsProvider.getNdsValue());
    }

}
