package model;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private ProductDTO productDTO;
    private String quantity;
}
