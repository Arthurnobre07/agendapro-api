package br.com.agendapro.dto;

public record LoginResponse(
    String token,
    String tipo,
    long expiraEm
) {}