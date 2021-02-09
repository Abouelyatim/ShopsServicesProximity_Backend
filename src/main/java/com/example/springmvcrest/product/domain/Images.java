package com.example.springmvcrest.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"product"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String image;

    @ManyToOne
    @JsonBackReference
    private Product product;

    public Images(String image,Product product){
        this.image=image;
        this.product=product;
    }

}
