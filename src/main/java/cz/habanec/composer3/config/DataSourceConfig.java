package cz.habanec.composer3.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
//    @Bean
//    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("nazev z paty");
////        dataSourceBuilder.url("jdbc:mysql://localhost:3306/composer");
////        dataSourceBuilder.username("root");
////        dataSourceBuilder.password("lupen");
//        return dataSourceBuilder.build();
//    }
}
