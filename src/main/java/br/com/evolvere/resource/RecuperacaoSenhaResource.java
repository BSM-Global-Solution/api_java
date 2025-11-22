package br.com.evolvere.resource;

import br.com.evolvere.bo.RecuperacaoSenhaBO;
import br.com.evolvere.to.RecuperacaoSenhaTO;

import jakarta.ws.rs.*;                 // << trocou de javax para jakarta
import jakarta.ws.rs.core.MediaType;   // << idem
import jakarta.ws.rs.core.Response;    // << idem

import java.util.HashMap;
import java.util.Map;

@Path("/recuperacao-senha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecuperacaoSenhaResource {

    private final RecuperacaoSenhaBO bo = new RecuperacaoSenhaBO();

    // DTOs internos do resource
    public static class EsqueciSenhaRequest {
        public String email;

        public EsqueciSenhaRequest() {}

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class RedefinirSenhaRequest {
        public String email;
        public String token;
        public String novaSenha;

        public RedefinirSenhaRequest() {}

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNovaSenha() {
            return novaSenha;
        }

        public void setNovaSenha(String novaSenha) {
            this.novaSenha = novaSenha;
        }
    }

    @POST
    @Path("/solicitar")
    public Response solicitar(EsqueciSenhaRequest req) {
        try {
            RecuperacaoSenhaTO rec = bo.solicitarRecuperacao(req.email, 15); // expira em 15 minutos

            Map<String, Object> body = new HashMap<>();
            body.put("mensagem", "Token de recuperação gerado com sucesso.");
            body.put("token", rec.getToken());

            return Response.ok(body).build();

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }

    @POST
    @Path("/redefinir")
    public Response redefinir(RedefinirSenhaRequest req) {
        try {
            bo.redefinirSenha(req.email, req.token, req.novaSenha);

            Map<String, Object> body = new HashMap<>();
            body.put("mensagem", "Senha redefinida com sucesso.");

            return Response.ok(body).build();

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
    }
}
