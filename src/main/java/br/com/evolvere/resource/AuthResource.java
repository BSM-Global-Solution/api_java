package br.com.evolvere.resource;

import br.com.evolvere.bo.AutenticacaoEmailBO;
import br.com.evolvere.bo.UsuarioBO;
import br.com.evolvere.to.AutenticacaoEmailTO;
import br.com.evolvere.to.UsuarioTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AutenticacaoEmailBO authBO = new AutenticacaoEmailBO();
    private final UsuarioBO usuarioBO = new UsuarioBO();

    // DTOs usados apenas aqui

    public static class RegistroDTO {
        public String nome;
        public String email;
        public String senha;
        public String dataNascimento;
    }

    public static class ConfirmarCodigoDTO {
        public String nome;
        public String email;
        public String senha;
        public String dataNascimento;
        public String codigo;
    }

    public static class ReenviarCodigoDTO {
        public String email;
    }

    // ------------------- Endpoints -------------------

    // Passo 1: gerar código e enviar e-mail
    @POST
    @Path("/registrar")
    public Response registrar(RegistroDTO dto) {
        try {
            // Verifica se email já está cadastrado como usuário
            if (usuarioBO.emailJaExiste(dto.email)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("erro", "E-mail já cadastrado"))
                        .build();
            }

            AutenticacaoEmailTO auth = authBO.gerarERegistrarCodigo(dto.email, "CRIACAO_CONTA", 15);

            // Aqui entra seu serviço de e-mail (no seu caso, o front vai usar EmailJS)
            System.out.println("Código de verificação para " + dto.email + ": " + auth.getCodigo());

            Map<String, String> resp = new HashMap<>();
            resp.put("mensagem", "Código enviado para o e-mail informado.");
            resp.put("codigo", auth.getCodigo()); // front usa esse código com EmailJS
            return Response.ok(resp).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("erro", "Falha ao gerar código de autenticação"))
                    .build();
        }
    }

    // NOVO: Reenviar código
    @POST
    @Path("/reenviar-codigo")
    public Response reenviarCodigo(ReenviarCodigoDTO dto) {
        try {
            // Se o email já está cadastrado como usuário, não faz sentido reenviar código de criação
            if (usuarioBO.emailJaExiste(dto.email)) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("erro", "E-mail já cadastrado"))
                        .build();
            }

            // Gera novo código e salva na tb_autenticacao_email
            AutenticacaoEmailTO auth = authBO.reenviarCodigo(dto.email, "CRIACAO_CONTA", 15);

            // Log para debug
            System.out.println("Reenvio - código de verificação para " + dto.email + ": " + auth.getCodigo());

            // Devolve o código pro front usar no EmailJS
            Map<String, String> resp = new HashMap<>();
            resp.put("mensagem", "Novo código gerado com sucesso.");
            resp.put("codigo", auth.getCodigo());

            return Response.ok(resp).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity(Map.of("erro", "Falha ao reenviar código de autenticação"))
                    .build();
        }
    }

    // Passo 2: confirmar código e criar usuário
    @POST
    @Path("/confirmar-codigo")
    public Response confirmarCodigo(ConfirmarCodigoDTO dto) {

        try {
            // 1. Validar código
            AutenticacaoEmailTO auth = authBO.validarCodigo(dto.email, dto.codigo, "CRIACAO_CONTA");

            // 2. Converter data
            LocalDate dataNasc = parseData(dto.dataNascimento);

            // 3. Montar UsuarioTO
            UsuarioTO novo = new UsuarioTO();
            novo.setNome(dto.nome);
            novo.setEmail(dto.email);
            novo.setSenha(dto.senha);
            novo.setDataDeNascimento(dataNasc);

            // 4. Criar usuário usando a regra existente
            UsuarioTO criado = usuarioBO.save(novo);

            // 5. Marcar código como usado
            authBO.marcarComoUsado(auth.getId());

            return Response.status(Response.Status.CREATED).entity(criado).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("erro", e.getMessage()))
                    .build();
        }
    }

    // Helper para converter String -> LocalDate
    private LocalDate parseData(String data) throws ParseException {
        // espera "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(data).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }
}