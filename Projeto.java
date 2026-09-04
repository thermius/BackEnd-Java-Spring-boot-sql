package Projeto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataAccessException; 
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;



@RestController
public class Projeto
{

    /*interface do banco de dados*/
    private final InterfacePostgre cliente;

    /*injeção de dependencia*/
    public Projeto (InterfacePostgre novo_cliente)
    {
        this.cliente = novo_cliente;
    }

    /*Metodo GET para /cadastrar. Retorna os dados dos clientes descriptografados com base no cpf*/
    @GetMapping ("/consultar/{cpf}")
    public ResponseEntity <?> ConsultarClientes (@PathVariable("cpf") String cpf)
    {
        byte b_hash_cpf[] = null;

        try                 { b_hash_cpf = Criptografia.GerarHash(cpf); }
        catch (Exception e) { e.printStackTrace();                      }

        Optional <Linha> saida      = cliente.BuscarPorCPF(b_hash_cpf);
        if (saida.isEmpty())        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum cliente encontrado com o CPF informado.");

        Linha linhaEncontrada       = saida.get();

        byte[] b_d_cpf                  = null;
        byte[] b_d_senha                = null;
        byte[] b_d_numero_cartao        = null;
        byte[] b_d_cvv                  = null;
        byte[] b_d_data_vencimento      = null;
        byte[] b_d_data_cadastro        = null;

        /*descriptografa os dados*/
        try
        {
            b_d_cpf               = Criptografia.DescriptografarBytes(linhaEncontrada.getCpf());
            b_d_senha             = Criptografia.DescriptografarBytes(linhaEncontrada.getSenha());
            b_d_numero_cartao     = Criptografia.DescriptografarBytes(linhaEncontrada.getNumeroCartao());
            b_d_cvv               = Criptografia.DescriptografarBytes(linhaEncontrada.getCvv());
            b_d_data_vencimento   = Criptografia.DescriptografarBytes(linhaEncontrada.getDataVencimento());
            b_d_data_cadastro     = Criptografia.DescriptografarBytes(linhaEncontrada.getDataCadastro());
        }

        catch (Exception e) { e.printStackTrace(); }

        String cpfTexto                 = new String(b_d_cpf);
        String senhaTexto               = new String(b_d_senha);
        String cartaoTexto              = new String(b_d_numero_cartao);
        String cvvTexto                 = new String(b_d_cvv);
        String vencimentoTexto          = new String(b_d_data_vencimento);
        String cadastroTexto            = new String(b_d_data_cadastro);
        LocalDate dataVencimento        = LocalDate.parse(vencimentoTexto);
        LocalDateTime dataCadastro      = LocalDateTime.parse(cadastroTexto);

        LinhaTextual resposta = new LinhaTextual();
        resposta.setNome(linhaEncontrada.getNome());
        resposta.setCpf(cpfTexto);
        resposta.setSenha(senhaTexto);
        resposta.setNumeroCartao(cartaoTexto);
        resposta.setCvv(cvvTexto);
        resposta.setDataVecimento(dataVencimento);
        resposta.setDataCadastro(dataCadastro);

        return ResponseEntity.ok(resposta);        
    }

    /*Metodo POST para /cadastrar*/
    @PostMapping("/cadastrar")
    public ResponseEntity<String> CadastrarClientes (@RequestBody LinhaTextual entrada_cliente)
    {

        HexFormat hex = HexFormat.ofDelimiter(" ");

        /*pega as informações*/
        String          nome_cliente                 = entrada_cliente.getNome();
        String          cpf_cliente                  = entrada_cliente.getCpf();
        String          senha_cliente                = entrada_cliente.getSenha();
        String          numero_cartao_cliente        = entrada_cliente.getNumeroCartao();
        String          cvv_cliente                  = entrada_cliente.getCvv();
        LocalDate       data_vencimento_cliente      = LocalDate.now().plusYears(5);
        LocalDateTime   data_cadastro_cliente        = LocalDateTime.now();

        /*Exibe no terminal - so pra debugs*/
        System.out.println("=== DADOS RECEBIDOS VIA POST ===");
        System.out.println("Nome:                   " + nome_cliente);
        System.out.println("CPF:                    " + cpf_cliente);
        System.out.println("Senha:                  " + senha_cliente);
        System.out.println("Número Cartão:          " + numero_cartao_cliente);
        System.out.println("CVV:                    " + cvv_cliente);
        System.out.println("Data Vencimento:        " + data_vencimento_cliente);
        System.out.println("Data Cadastro:          " + data_cadastro_cliente);
        System.out.println("====================================");

        /*converte tudo para byte*/
        byte    []  b_cpf_cliente                  = entrada_cliente.getCpf().getBytes();
        byte    []  b_senha_cliente                = entrada_cliente.getSenha().getBytes();
        byte    []  b_numero_cartao_cliente        = entrada_cliente.getNumeroCartao().getBytes();
        byte    []  b_cvv_cliente                  = entrada_cliente.getCvv().getBytes();
        byte    []  b_data_vencimento_cliente      = LocalDate.now().plusYears(5).toString().getBytes();
        byte    []  b_data_cadastro_cliente        = LocalDateTime.now().toString().getBytes();

        /*exibe os bytes no terminal - so pra debugs*/
        System.out.println("\n=== CONVERSÃO PARA BYTES (HEXADECIMAL) ===");
        System.out.println("Nome Hex:            " + nome_cliente);
        System.out.println("CPF Hex:             " + hex.formatHex(b_cpf_cliente));
        System.out.println("Senha Hex:           " + hex.formatHex(b_senha_cliente));
        System.out.println("Cartão Hex:          " + hex.formatHex(b_numero_cartao_cliente));
        System.out.println("CVV Hex:             " + hex.formatHex(b_cvv_cliente));
        System.out.println("Data Vencimento Hex: " + hex.formatHex(b_data_vencimento_cliente));
        System.out.println("Data Cadastro Hex:   " + hex.formatHex(b_data_cadastro_cliente));
        System.out.println("===========================================\n");

        /*variaveis que irão receber os dados criptografados*/
        byte[] c_b_cpf_cliente              = null;
        byte[] c_b_senha_cliente            = null;
        byte[] c_b_numero_cartao_cliente    = null;
        byte[] c_b_cvv_cliente              = null;
        byte[] c_b_data_vencimento_cliente  = null;
        byte[] c_b_data_cadastro_cliente    = null;
        byte[] b_hash_cpf                   = null;

        /*criptografa as informações*/
        try
        {
            c_b_cpf_cliente                  = Criptografia.CriptografarBytes(b_cpf_cliente);
            c_b_senha_cliente                = Criptografia.CriptografarBytes(b_senha_cliente);
            c_b_numero_cartao_cliente        = Criptografia.CriptografarBytes(b_numero_cartao_cliente);
            c_b_cvv_cliente                  = Criptografia.CriptografarBytes(b_cvv_cliente);
            c_b_data_vencimento_cliente      = Criptografia.CriptografarBytes(b_data_vencimento_cliente);
            c_b_data_cadastro_cliente        = Criptografia.CriptografarBytes(b_data_cadastro_cliente);
            b_hash_cpf                       = Criptografia.GerarHash        (cpf_cliente);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Erro ao criptografar dados. Verifique se as chaves estão carregadas: " + e.getMessage());
        }

        /*Cria uma nova linha com os dados criptografados*/
        Linha novo_registro = new Linha (   nome_cliente,
                                            c_b_cpf_cliente,
                                            c_b_senha_cliente,
                                            c_b_numero_cartao_cliente,
                                            c_b_cvv_cliente,
                                            c_b_data_vencimento_cliente,
                                            c_b_data_cadastro_cliente,
                                            b_hash_cpf);

        /*Salva no banco*/
        try
        {
            cliente.save(novo_registro);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso! ID: " + novo_registro.getId());
        }
        catch (DataAccessException e) 
        { 
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro PostgreSQL: " + e.getCause().getMessage());
        }
        catch (Exception e) 
        {     

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro inesperado: " + e.getMessage());
        } 
        finally
        {
            /*aciona o grab collector*/
            novo_registro = null;
        }
    }
    

}

