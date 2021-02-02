package com.example.springmvcrest.store.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Data
@EqualsAndHashCode(exclude = "stores")
@Entity
@NoArgsConstructor
public class CategoryStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Store> stores=new HashSet<Store>();

    public CategoryStore(String name) {
        this.name = name;
    }
}