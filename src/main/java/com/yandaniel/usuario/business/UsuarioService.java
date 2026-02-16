package com.yandaniel.usuario.business;


import com.yandaniel.usuario.infrastructure.entity.Usuario;
import com.yandaniel.usuario.infrastructure.exceptions.ConflictExceptions;
import com.yandaniel.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.yandaniel.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public Usuario salvaUsuario(Usuario usuario) {
        try {
            emailExist(usuario.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioRepository.save(usuario);
        } catch (ConflictExceptions e) {
            throw new ConflictExceptions("Email já cadastrado", e.getCause());

        }
    }

    public void emailExist(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictExceptions("Email já cadastrado");
            }
        }catch (ConflictExceptions e){
            throw new ConflictExceptions("Email já cadastrado" + e.getCause());
        }

    }

    public boolean verificaEmailExistente (String email) {
        return usuarioRepository.existsByEmail(email);

    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}