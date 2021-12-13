package ch.erard22.demo;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Product extends PanacheEntity {

    public String name;

}
