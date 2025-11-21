package br.com.evolvere.bo;

import br.com.evolvere.dao.UsuarioDAO;
import br.com.evolvere.to.UsuarioTO;

import java.util.List;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;

    // Construtor
    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Listar todos
    public List<UsuarioTO> findAll() {
        System.out.println("BO: Buscando todos usuários...");
        return usuarioDAO.findAll();
    }

    // Salvar
    public UsuarioTO save(UsuarioTO usuario) {
        System.out.println("BO: Salvando usuário " + usuario.getNome());
        return usuarioDAO.save(usuario);
    }

    // Atualizar (por id)
    public UsuarioTO update(UsuarioTO usuario) {
        System.out.println("BO: Atualizando usuário ID " + usuario.getId());
        return usuarioDAO.update(usuario);
    }

    // Deletar (por id)
    public boolean delete(int id) {
        System.out.println("BO: Removendo usuário com ID " + id);
        return usuarioDAO.delete(id);
    }


    // Login
    public UsuarioTO login(String email, String senha) {
        System.out.println("BO: Tentando login para " + email);
        return usuarioDAO.login(email, senha);
    }
}