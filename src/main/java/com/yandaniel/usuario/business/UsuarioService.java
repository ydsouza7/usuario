package com.yandaniel.usuario.business;


import com.yandaniel.usuario.business.converter.UsuarioConverter;
import com.yandaniel.usuario.business.dtos.UsuarioDTO;
import com.yandaniel.usuario.infrastructure.entity.Usuario;
import com.yandaniel.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario (UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }
}
    //mesma coisa do de cima porém com um método passo a passo para entender melhor.

//    public UsuarioDTO salvaUsuario (UsuarioDTO usuarioDTO){
//        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
//        usuario = usuarioRepository.save(usuario);
//        return usuarioConverter.paraUsuarioDTO(usuario);


