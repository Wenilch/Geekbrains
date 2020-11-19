package ru.geekbrains.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geekbrains.common.services.FileTransferService;
import ru.geekbrains.common.services.FileTransferServiceImpl;

@Configuration
public class SpringContextConfiguration {

    @Bean
    public FileTransferService getFileTransferService(){
        return new FileTransferServiceImpl();
    }
}
