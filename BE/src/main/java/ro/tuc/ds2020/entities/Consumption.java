package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Consumption {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "energyConsumption", nullable = false)
    private String energyConsumption;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "device_id")
    private Device device;

    @Override
    public String toString() {
        return "Consumption{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", energyConsumption='" + energyConsumption + '\'' +
                '}';
    }
}
