package application.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String country;
    String city;
    String indexCity;
    String street;
    String building;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="filial")
    List<Contact> contacts = new ArrayList<>();

    public Filial(String name, String country, String city, String indexCity, String street, String building) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.indexCity = indexCity;
        this.street = street;
        this.building = building;
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }

    public Filial() {
    }
}
