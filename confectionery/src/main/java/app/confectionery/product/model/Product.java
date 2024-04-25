package app.confectionery.product.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}
