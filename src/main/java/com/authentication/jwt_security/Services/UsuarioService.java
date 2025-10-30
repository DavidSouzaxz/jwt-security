package com.authentication.jwt_security.Services;

import com.authentication.jwt_security.config.JwtUtil;
import com.authentication.jwt_security.dtos.LoginRequestDTO;
import com.authentication.jwt_security.dtos.UsuarioRequestDTO;
import com.authentication.jwt_security.dtos.UsuarioResponseDTO;
import com.authentication.jwt_security.entity.Usuario;
import com.authentication.jwt_security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUltil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::ConvertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( "Is not existing User"));

        return ConvertToDTO(usuario);
    }

    public UsuarioResponseDTO create(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioRequestDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));
        usuarioRepository.save(usuario);
        return ConvertToDTO(usuario);

    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO usuario) {
        Usuario usuarioExisting = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException( "Is not existing User"));

        usuarioExisting.setEmail(usuario.getEmail());
        usuarioExisting.setPassword(usuario.getPassword());
        usuarioRepository.save(usuarioExisting);

        return ConvertToDTO(usuarioExisting);

    }

    public String login(LoginRequestDTO loginRequestDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }


        return jwtUltil.generateToken(usuario.getEmail());
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO ConvertToDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getEmail());
        dto.setPassword(usuario.getPassword());

        return dto;
    }
}
