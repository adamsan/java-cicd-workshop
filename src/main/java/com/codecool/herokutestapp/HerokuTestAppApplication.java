package com.codecool.herokutestapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class HerokuTestAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HerokuTestAppApplication.class, args);
    }

}

@Service
record PersonService(JdbcTemplate jdbcTemplate) {
    public List<Person> findAll() {
        RowMapper<Person> mapper = (r, i) -> new Person(r.getInt("id"), r.getString("name"));
        List<Person> persons = jdbcTemplate.query("select * from person", mapper);
        return persons;
    }
}

@RestController("/")
record PersonController(PersonService personService) {
    @GetMapping("persons")
    public List<Person> findAll() {
        //return List.of(new Person(1, "A"), new Person(2, "B"));
        return personService.findAll();
    }
}

@AllArgsConstructor
@Data
class Person {
    private int id;
    private String name;
}