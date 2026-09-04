package Projeto;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;



/*Funções que criptografam e descriptografam os dados*/
public class Criptografia 
{
	public static byte [] CriptografarBytes (byte[] entrada) throws Exception
	{
			/*a chave é uma variavel de ambiente*/
			String chaveBase64 = System.getenv("AES_SECRET_KEY");
					
			/*converte de base 64 para bytes*/
			byte[] chaveBytes = Base64.getDecoder().decode(chaveBase64);

			/*converte para um secrete key*/
			SecretKey chave = new javax.crypto.spec.SecretKeySpec( chaveBytes,"AES");

			/*criamos o vetor de inicialização do aes*/
			byte [] iv = new byte [12];

			/*cria um gerador de bytes aleatorios*/
			SecureRandom bytes_aleatorios = new SecureRandom ();

			/*preenche iv com bytes aleatorios*/
			bytes_aleatorios.nextBytes(iv);

			/*criamos uma instancia do algortimo AES com GCM e sem preenchimento de bloco*/
			Cipher cifra = Cipher.getInstance("AES/GCM/NoPadding");

			/*criamos uma instancia das especificações do GCM. Defininos o tamnho da  tag par 16 bytes (128 bits) e especifica o iv do AES*/
			GCMParameterSpec especificacao = new GCMParameterSpec (128, iv);

			/*inicializa o motor em modo criptografar, passando a chave e as especificações*/
			cifra.init(Cipher.ENCRYPT_MODE, chave, especificacao);

			/*criprografamos a entrada*/
			byte [] saida = cifra.doFinal(entrada);

			/*aloca memoria para a saida*/
			byte[] dadosCriptografados = new byte[iv.length + saida.length];

			/*copia o iv*/
			System.arraycopy(iv, 0, dadosCriptografados, 0, iv.length);

			/*copia o texto e tag*/
			System.arraycopy(saida, 0, dadosCriptografados, iv.length, saida.length);

			/*retorna a entrada criptografada*/
			return dadosCriptografados;

	         /* o layout final do banco de dados fica assim:
	                        
	                        [  IV  | Ciphertext |  TAG  ]
	                        (12 B)     (11 B)      (16 B)
	         */
	}

	public static byte [] DescriptografarBytes (byte[]entrada) throws Exception
	{
			/*a chave é uma variavel de ambiente*/
			String chaveBase64 = System.getenv("AES_SECRET_KEY");

			/*converte de base 64 para bytes*/
			byte[] chaveBytes = Base64.getDecoder().decode(chaveBase64);

			/*converte para um secrete key*/
			SecretKey chave = new javax.crypto.spec.SecretKeySpec( chaveBytes,"AES");

			/*alocamos memorias*/
			byte [] iv              = new byte [12];
			byte [] tag             = new byte [16];
			byte [] texto_crifrado  = new byte[entrada.length - iv.length - tag.length];

			/*cortamos os bytes*/
			System.arraycopy (entrada, 0, iv, 0 , iv.length);
			System.arraycopy (entrada, iv.length, texto_crifrado , 0, texto_crifrado.length);
			System.arraycopy (entrada, iv.length + texto_crifrado.length, tag, 0, tag.length);

			/*alocamos memoria para juntar os bytes*/
	     	byte[] textoMaisTag = new byte[texto_crifrado.length + tag.length];
			System.arraycopy(texto_crifrado, 0, textoMaisTag, 0, texto_crifrado.length);
			System.arraycopy(tag, 0, textoMaisTag, texto_crifrado.length, tag.length);		
			
			/*configuramos as especificações de descriptografia*/
			GCMParameterSpec especificacao = new GCMParameterSpec(128, iv);
			Cipher cifra = Cipher.getInstance("AES/GCM/NoPadding");
			cifra.init( Cipher.DECRYPT_MODE, chave, especificacao);

			/*descriptografa*/
			byte[] saida = cifra.doFinal(textoMaisTag);

			/*retorna a entrada descriptografada*/
			return saida;

	}
	public static byte [] GerarHash (String cpf) throws Exception
	{
		/*converte a entrada para bytes*/
		byte [] entrada = cpf.getBytes(StandardCharsets.UTF_8);

		/*obtem a chave HMAC*/
		String chaveBase64 = System.getenv("HMAC_SECRET_KEY");	

		/*converte a chave para bytes*/
	    byte [] chaveBytes = Base64.getDecoder().decode(chaveBase64);

		/*conterte os bytes da chave em um objeto SecretKeySpec*/
   		SecretKeySpec chave = new SecretKeySpec(chaveBytes,"HmacSHA256");

   		/*Inicializa um objeto motor hmac*/
    	Mac mac = Mac.getInstance("HmacSHA256");

    	/*inicializa o motor com a chave (esse codigo pode ser chamdo em CommandLineRunner para optmizar o desemenho)*/
		mac.init(chave);

		/*retorna a hash*/
		return  mac.doFinal(entrada);

	}

}