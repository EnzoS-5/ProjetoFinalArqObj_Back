package com.example.ProjetoFinalArqObj.User;

public record UserResponseDTO(
        Integer id,
        String nome,
        String email,
        int xp,
        int streak
) {
    public static UserResponseDTO of(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getXp(),
                user.getStreak()
        );
    }
}
