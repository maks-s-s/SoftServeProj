package com.softserve.academy.dto;


import com.softserve.academy.models.Purchase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private List<Purchase> purchases = new ArrayList<>();

    public boolean validate(){
        return name!=null && email!=null && phoneNumber!=null && password!=null;
    }

}
