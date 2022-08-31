package com.yuri.simplescqrs.demo.scheduler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControleDTO {
    private Long id;
    private String tableName;
    private Long idRecurso;
    private String tipoAlteracao;
    private boolean sincronizado;
}
