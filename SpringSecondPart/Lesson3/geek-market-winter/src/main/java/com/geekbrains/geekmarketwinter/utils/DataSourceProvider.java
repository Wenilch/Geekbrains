package com.geekbrains.geekmarketwinter.utils;

public class DataSourceProvider {

//@Configuration
//@PropertySource("classpath:connection.properties")
//public class DataSourceProvider implements TransactionManagementConfigurer {

//    @Value("${datasource.username}")
//    private String USER_NAME;
//    @Value("${datasource.password}")
//    private String PASSWORD;
//    @Value("${datasource.url}")
//    private String DATASOURCE_URL;
//    @Value("${datasource.driver-class-name}")
//    private String DRIVER_CLASS_NAME;
//
//   // @Bean
//    @Override
//    @NonNull
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setUsername(USER_NAME);
//        config.setPassword(PASSWORD);
//        config.setJdbcUrl(DATASOURCE_URL);
//        config.setDriverClassName(DRIVER_CLASS_NAME);
//
//        return new HikariDataSource(config);
//    }
//
//    @Bean
//    public Sql2o sql2o() {
//        return new Sql2o(dataSource());
//    }
}
