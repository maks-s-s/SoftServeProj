package com.softserve.academy.dto;

import com.softserve.academy.model.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private List<Purchase> purchases = new ArrayList<>();

    public boolean validate(){
        return name!=null && email!=null && phoneNumber!=null&&password!=null;}
}
