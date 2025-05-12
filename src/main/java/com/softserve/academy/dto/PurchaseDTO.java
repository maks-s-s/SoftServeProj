package com.softserve.academy.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDTO {
    public Long customerId;
    public Long productId;
    public int quantity;
    public LocalDateTime time;

    public boolean validate(){
        return customerId!=null&&productId!=null&&quantity>0;
    }
    public void setTime(){time=LocalDateTime.now();}
}
