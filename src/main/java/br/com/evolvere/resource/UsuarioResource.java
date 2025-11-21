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
            return Response.ok(usuarioBO.findAll()).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"erro\": \"Falha ao buscar usuários\"}").build();
        }
    }


    // Criar usuário
    @POST
    @Path("/usuarios")
    public Response criarUsuario(UsuarioTO usuario) {
        try {
            UsuarioTO criado = usuarioBO.save(usuario);
            return Response.status(Response.Status.CREATED).entity(criado).build();

        } catch (RuntimeException ex) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"erro\": \"" + ex.getMessage() + "\"}")
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"erro\": \"Falha ao criar usuário\"}")
                    .build();
        }
    }


    // Atualizar usuário
    @PUT
    @Path("/usuarios/{id}")
    public Response atualizarUsuario(@PathParam("id") int id, UsuarioTO usuario) {
        try {
            usuario.setId(id);
            UsuarioTO atualizado = usuarioBO.update(usuario);

            if (atualizado != null) {
                return Response.ok(atualizado).build();
            }

            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensagem\": \"Usuário não encontrado\"}")
                    .build();

        } catch (RuntimeException ex) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"erro\": \"" + ex.getMessage() + "\"}")
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"erro\": \"Falha ao atualizar usuário\"}")
                    .build();
        }
    }


    // Remover usuário
    @DELETE
    @Path("/usuarios/{id}")
    public Response removerUsuario(@PathParam("id") int id) {
        try {
            if (usuarioBO.delete(id)) {
                return Response.noContent().build();
            }

            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"mensagem\": \"Usuário não encontrado\"}")
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"erro\": \"Falha ao remover usuário\"}")
                    .build();
        }
    }


    // Login
    @POST
    @Path("/login")
    public Response login(UsuarioTO credenciais) {
        try {
            UsuarioTO usuario = usuarioBO.login(credenciais.getEmail(), credenciais.getSenha());

            if (usuario != null) {
                return Response.ok(usuario).build();
            }

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensagem\": \"Email ou senha incorretos\"}")
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"erro\": \"Falha no processo de login\"}")
                    .build();
        }
    }
}