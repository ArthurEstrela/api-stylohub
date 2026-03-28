package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.Size;

public record UpdateSeoRequest(
        @Size(max = 60, message = "Título SEO pode ter no máximo 60 caracteres.")
        String seoTitle,

        @Size(max = 160, message = "Descrição SEO pode ter no máximo 160 caracteres.")
        String seoDescription
) {}
