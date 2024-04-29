package app.confectionery.address.model;

import app.confectionery.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String addressName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

}
