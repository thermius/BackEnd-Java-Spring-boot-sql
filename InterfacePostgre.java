package Projeto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;       
import org.springframework.data.repository.query.Param;

@RepositoryRestResource (collectionResourceRel= "saida_repositorio" )
public interface InterfacePostgre extends CrudRepository <Linha,Long> 
{
	@Query (value="SELECT * from clientes_tb WHERE hash_cpf = :hashCpf", nativeQuery = true)
	Optional<Linha> BuscarPorCPF(@Param("hashCpf") byte[] hashCpf);
}