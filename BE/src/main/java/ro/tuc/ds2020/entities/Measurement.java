package ro.tuc.ds2020.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class Measurement implements Serializable {
    private Timestamp timestamp;

    @Type(type = "uuid-binary")
    private UUID device_id;

    private float measurementValue;

    public Measurement(Timestamp timestamp, UUID device_id, float measurementValue) {
        this.timestamp = timestamp;
        this.device_id = device_id;
        this.measurementValue = measurementValue;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "timestamp=" + timestamp +
                ", device_id=" + device_id +
                ", measurementValue=" + measurementValue +
                '}';
    }
}
