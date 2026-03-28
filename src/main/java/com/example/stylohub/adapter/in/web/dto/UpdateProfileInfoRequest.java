package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileInfoRequest(
        @NotBlank(message = "Nome de exibição é obrigatório.")
        @Size(max = 80, message = "Nome de exibição pode ter no máximo 80 caracteres.")
        String displayName,

        @Size(max = 160, message = "Bio pode ter no máximo 160 caracteres.")
        String bio
) {}
