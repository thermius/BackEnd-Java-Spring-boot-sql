package Projeto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "clientes_tb")
public class Linha
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String 	nome;
	private	byte []	cpf;
	private byte []	senha;	
	private	byte []	numero_cartao;
	private byte []	cvv;
	private byte []	data_vencimento_cartao;
	private byte []	data_cadastro;
	private byte [] hash_cpf;

	/*construtor obrigatorio*/
	public Linha() {}
	
	/*Construtor que recebe os dados em formato byte e cria o objeto Linha*/
	public Linha(String nome, byte []... lista_de_bytes) 
	{	
		int index 						= 0;
		this.nome 						= nome;
		this.cpf 						= lista_de_bytes[index++];	
		this.senha 						= lista_de_bytes[index++];	
		this.numero_cartao 				= lista_de_bytes[index++];	
		this.cvv 						= lista_de_bytes[index++];	
		this.data_vencimento_cartao 	= lista_de_bytes[index++];	
		this.data_cadastro 				= lista_de_bytes[index++];	
		this.hash_cpf 					= lista_de_bytes[index++];	

	}

	/*sets*/
	public void setNome 			(String 		param) 	{ this.nome 						= param; 						}
	public void setCpf  			(byte []  		param) 	{ this.cpf 							= param; 						}
	public void setSenha			(byte []  		param) 	{ this.senha 						= param; 						}
	public void setNumeroCartao  	(byte []  		param) 	{ this.numero_cartao 				= param; 						}
	public void setCvv				(byte []  		param) 	{ this.cvv 							= param; 						}
	public void setDataVecimento	(byte [] 		param) 	{ this.data_vencimento_cartao		= param;						}
	public void setDataCadastro		(byte [] 	 	param) 	{ this.data_cadastro				= param;						}
	public void setHashCpf			(byte [] 	 	param) 	{ this.hash_cpf						= param;						}
	
	/*gets*/	
	public Long 		getId() 			{ return this.id; 						}
    public String 		getNome() 			{ return this.nome; 					}
    public byte []  	getCpf() 			{ return this.cpf; 						}
    public byte []  	getSenha() 			{ return this.senha; 					}
    public byte []		getNumeroCartao()	{ return this.numero_cartao; 			}
    public byte []  	getCvv()			{ return this.cvv; 						}	
    public byte [] 		getDataVencimento()	{ return this.data_vencimento_cartao; 	}
    public byte []  	getDataCadastro()	{ return this.data_cadastro; 			}
    public byte []  	getHashCpf()		{ return this.hash_cpf; 				}
}




