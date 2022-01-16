package api.model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Product product;
    private Object quantity;
}
