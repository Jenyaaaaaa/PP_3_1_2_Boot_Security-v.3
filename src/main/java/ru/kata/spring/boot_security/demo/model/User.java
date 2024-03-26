package ru.kata.spring.boot_security.demo.model;

//import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;


}




//import jakarta.persistence.Column;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Table;
//import org.springframework.data.annotation.Id;
//
//
//   // @Data
//   // @Entity
//    @Table(name = "users")
//    public class User {
//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private Long id;
//
//        @Column(name = "first_name")
//        private String firstName;
//        @Column(name = "last_name")
//        private String lastName;
//
//        public void setId(Long id) {
//            this.id = id;
//        }
//
//        public Long getId() {
//            return id;
//        }
//    }


