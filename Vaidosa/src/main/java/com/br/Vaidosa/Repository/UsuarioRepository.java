package com.br.Vaidosa.Repository;

import com.br.Vaidosa.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String login);


}
