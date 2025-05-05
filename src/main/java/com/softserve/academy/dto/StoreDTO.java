package com.softserve.academy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDTO {
    public String name;
    public Long id;
    public String location;
    public String email;
    public String contactNumber;
    public boolean validate(){
        return name!=null && email!=null && location!=null && contactNumber!=null;
    }
}