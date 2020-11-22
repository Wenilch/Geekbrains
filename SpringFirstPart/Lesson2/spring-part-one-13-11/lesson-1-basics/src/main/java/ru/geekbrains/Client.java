package ru.geekbrains;

import ru.geekbrains.nds_provider.NdsProvider;
import ru.geekbrains.nds_provider.NdsProviderImpl;

public class Client {

    public static void main(String[] args) {
        NdsProvider ndsProvider = new NdsProviderImpl();
        CalcNds calcNds = new CalcNds(ndsProvider);
        Double result = calcNds.priceWithNds(1000.);
        System.out.println(result);
    }
}
