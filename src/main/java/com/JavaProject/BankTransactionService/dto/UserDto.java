package com.JavaProject.BankTransactionService.dto;


import com.JavaProject.BankTransactionService.model.Limit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private List<TransactionDto> transactionDtos;
    private List<Limit> limits;
}
