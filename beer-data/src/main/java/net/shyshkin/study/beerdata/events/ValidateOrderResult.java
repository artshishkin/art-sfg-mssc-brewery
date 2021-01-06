package net.shyshkin.study.beerdata.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderResult implements Serializable {

    static final long serialVersionUID = -7010464803302368356L;

    private UUID orderId;
    private Boolean isValid;

}
