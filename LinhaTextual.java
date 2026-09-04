package Projeto;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*Essa classe guarda os dados recebidos via API. Posteriormente, os dados convertidos em bytes, criptografados e enviados ao banco de dados*/
public class LinhaTextual 
{
	private String 			nome;
	private	String 			cpf;
	private String 			senha;
	private	String 			numero_cartao;
	private String 			cvv;
	private LocalDate 		data_vencimento;
	private LocalDateTime	data_cadastro;

	/*sets*/
	public void setNome 			(String 		param) 	{ this.nome 				= param; 						}
	public void setCpf  			(String			param) 	{ this.cpf 					= param; 						}
	public void setSenha			(String 		param) 	{ this.senha 				= param; 						}
	public void setNumeroCartao  	(String 		param) 	{ this.numero_cartao 		= param; 						}
	public void setCvv				(String 		param) 	{ this.cvv 					= param; 						}
	public void setDataVecimento	(LocalDate 		param) 	{ this.data_vencimento		= param;						}
	public void setDataCadastro		(LocalDateTime	param) 	{ this.data_cadastro		= param;						}
	
	/*gets*/	
    public String 			getNome() 			{ return this.nome; 			}
    public String  			getCpf() 			{ return this.cpf; 				}
    public String 			getSenha() 			{ return this.senha; 			}
    public String			getNumeroCartao()	{ return this.numero_cartao; 	}
    public String 			getCvv()			{ return this.cvv; 				}	
    public LocalDate		getDataVencimento()	{ return this.data_vencimento; 	}
    public LocalDateTime 	getDataCadastro()	{ return this.data_cadastro; 	}
}