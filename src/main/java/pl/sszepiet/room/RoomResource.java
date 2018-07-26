package pl.sszepiet.room;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
public class RoomResource extends ResourceSupport {

    private UUID roomId;

    private String city;

    private BigDecimal dailyPrice;
}
