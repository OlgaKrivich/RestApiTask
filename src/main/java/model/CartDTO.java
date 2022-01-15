package model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private ProductDTO product;
    private Object quantity;
}
