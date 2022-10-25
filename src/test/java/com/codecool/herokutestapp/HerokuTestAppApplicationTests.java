package com.codecool.herokutestapp;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;

@SpringBootTest
@Log4j2
class HerokuTestAppApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws MetaDataAccessException {
        DatabaseMetaDataCallback names = dbmd -> {
            ResultSet rs = dbmd.getTables(dbmd.getUserName(), null, null, new String[]{"TABLE"});
            ArrayList l = new ArrayList();
            while (rs.next()) {
                //l.add(rs.getMetaData().getColumnName(3));
                l.add(rs.getString("TABLE_NAME"));
            }
            return l;
        };

       log.info(JdbcUtils.extractDatabaseMetaData(dataSource, names));
//        System.out.println(JdbcUtils.extractDatabaseMetaData(dataSource, names));
    }

}

@TestConfiguration
class DataSourceConfig {
    @Bean
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}