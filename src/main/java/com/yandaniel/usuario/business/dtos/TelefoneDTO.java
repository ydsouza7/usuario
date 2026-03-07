package com.yandaniel.usuario.business.dtos;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TelefoneDTO {

    private long id;
    private String numero;
    private String ddd;

}

