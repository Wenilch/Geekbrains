package ru.geekbrains.nds_provider;

public class NdsProviderFactory {

    private final static NdsProvider INSTANCE = new NdsProviderImpl();

    public static NdsProvider getNdsProvider() {
        return new NdsProviderImpl();
    }
}
