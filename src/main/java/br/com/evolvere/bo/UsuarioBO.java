package br.com.evolvere.bo;

import br.com.evolvere.dao.UsuarioDAO;
import br.com.evolvere.to.UsuarioTO;

import java.util.List;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public List<UsuarioTO> findAll() {
        return usuarioDAO.findAll();
    }

    public UsuarioTO save(UsuarioTO usuario) {

        // Impedir criar e-mail duplicado
        if (usuarioDAO.emailExists(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        return usuarioDAO.save(usuario);
    }

    public UsuarioTO update(UsuarioTO usuario) {

        // Se o email foi alterado, verificar se já existe
        UsuarioTO existente = usuarioDAO.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioDAO.emailExists(usuario.getEmail()) &&
                existente != null &&
                existente.getId() != usuario.getId()) {

            throw new RuntimeException("E-mail já está sendo usado por outro usuário!");
        }

        return usuarioDAO.update(usuario);
    }

    public boolean delete(int id) {
        return usuarioDAO.delete(id);
    }

    public UsuarioTO login(String email, String senha) {
        return usuarioDAO.login(email, senha);
    }
}