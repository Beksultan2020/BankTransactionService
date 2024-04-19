package com.JavaProject.BankTransactionService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseModel {
    private String fullName;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Limit> limits;
}
