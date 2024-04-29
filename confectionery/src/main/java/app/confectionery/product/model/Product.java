package app.confectionery.product.model;


import app.confectionery.cart_item.model.CartItem;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "cartItem")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String category;

    private double price;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_data_id", referencedColumnName = "id")
    private FileData image;

    @Transient
    private String imagePath;

//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @ToString.Exclude
//    private CartItem cartItem;


}
