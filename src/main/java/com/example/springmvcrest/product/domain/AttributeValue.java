package com.example.springmvcrest.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"attribute"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;



    @OneToMany(mappedBy = "attributeValue")
    @IndexedEmbedded
    Set<ProductVariantAttributeValue> productVariantAttributeValues=new HashSet<>();;

    @ManyToOne()
    @IndexedEmbedded
    private Attribute attribute;

    public AttributeValue(Attribute attribute){
        this.attribute=attribute;

    }

}
