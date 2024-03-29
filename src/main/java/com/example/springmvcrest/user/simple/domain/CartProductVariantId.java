package com.example.springmvcrest.user.simple.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CartProductVariantId implements Serializable {
    private Long cartId;
    private Long cartProductVariantId;
}
