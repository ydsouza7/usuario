package com.yandaniel.usuario.business;


import com.yandaniel.usuario.business.converter.UsuarioConverter;
import com.yandaniel.usuario.business.dtos.UsuarioDTO;
import com.yandaniel.usuario.infrastructure.entity.Usuario;
import com.yandaniel.usuario.infrastructure.exceptions.ConflictExceptions;
import com.yandaniel.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.yandaniel.usuario.infrastructure.repository.UsuarioRepository;
import com.yandaniel.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictExceptions("Email já cadastrado");
            }
        } catch (ConflictExceptions e) {
            throw new ConflictExceptions("Email já cadastrado" + e.getCause());
        }

    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado")
        );
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){

        //Aqui buscamos o email do usuário através do token (tira a obrigatoriedade de passar o email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        //Buscamos os dados de usuário do banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Salvou os dados do usuario convertido e depois pegou o retorno e converteu para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }


}
    //mesma coisa do de cima porém com um método passo a passo para entender melhor.

//    public UsuarioDTO salvaUsuario (UsuarioDTO usuarioDTO){
//        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
//        usuario = usuarioRepository.save(usuario);
//        return usuarioConverter.paraUsuarioDTO(usuario);


