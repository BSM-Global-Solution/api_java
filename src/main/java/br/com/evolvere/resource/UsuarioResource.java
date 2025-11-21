package br.com.evolvere.resource;

import br.com.evolvere.bo.UsuarioBO;
import br.com.evolvere.to.UsuarioTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private UsuarioBO usuarioBO = new UsuarioBO();

    // Listar todos
    @GET
    @Path("/usuarios")
    public Response listarUsuarios() {
        try {
            List<UsuarioTO> usuarios = usuarioBO.findAll();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\": \"Falha ao buscar usuários\"}")
                    .build();
        }
    }


    // Criar usuários
    @POST
    @Path("/usuarios")
    public Response criarUsuario(UsuarioTO usuario) {
        try {
            UsuarioTO usuarioSalvo = usuarioBO.save(usuario);

            if (usuarioSalvo != null) {
                return Response.status(Response.Status.CREATED).entity(usuarioSalvo).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensagem\": \"Não foi possível criar o usuário\"}")
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\": \"Falha ao criar usuário\"}")
                    .build();
        }
    }


    // Atualizar por id
    @PUT
    @Path("/usuarios/{id}")
    public Response atualizarUsuario(@PathParam("id") int id, UsuarioTO usuario) {
        try {
            usuario.setId(id); // Define o id vindo da URL

            UsuarioTO usuarioAtualizado = usuarioBO.update(usuario);

            if (usuarioAtualizado != null) {
                return Response.ok(usuarioAtualizado).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensagem\": \"Usuário não encontrado\"}")
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\": \"Falha ao atualizar usuário\"}")
                    .build();
        }
    }


    // Remover (por id)
    @DELETE
    @Path("/usuarios/{id}")
    public Response removerUsuario(@PathParam("id") int id) {
        try {
            boolean removido = usuarioBO.delete(id);

            if (removido) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensagem\": \"Usuário não encontrado\"}")
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\": \"Falha ao remover usuário\"}")
                    .build();
        }
    }


    // Login
    @POST
    @Path("/login")
    public Response login(UsuarioTO credenciais) {
        try {
            UsuarioTO usuario = usuarioBO.login(
                    credenciais.getEmail(),
                    credenciais.getSenha()
            );

            if (usuario != null) {
                return Response.ok(usuario).build();
            }

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensagem\": \"Email ou senha incorretos\"}")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"erro\": \"Falha no processo de login\"}")
                    .build();
        }
    }
}