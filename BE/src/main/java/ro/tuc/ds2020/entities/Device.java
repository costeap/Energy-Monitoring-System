package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DEVICE")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maximumHourlyEnergyConsumption", nullable = false)
    private String maximumHourlyEnergyConsumption;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id")
    private Client client;

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", maximumHourlyEnergyConsumption='" + maximumHourlyEnergyConsumption + '\'' +
                ", client=" + client +
                '}';
    }
}
