package com.devs.filabancorev.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devs.filabancorev.dto.FinalizarSenhaDTO;
import com.devs.filabancorev.dto.ProximaSenhaDTO;
import com.devs.filabancorev.dto.SenhaDTO;
import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.enums.TipoSenha;
import com.devs.filabancorev.model.Senha;
import com.devs.filabancorev.repository.SenhaRepository;

@ExtendWith(MockitoExtension.class)
public class SenhaServiceTest {
	
	@Mock
	private SenhaRepository senhaRepository;
	
	@InjectMocks
	private SenhaService senhaService;
	
	@Test
	void deveEmitirSenhaNormal() {
		
		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		     .thenReturn(Optional.empty());

		when(senhaRepository.save(any(Senha.class)))
		    .thenAnswer(invocation -> invocation.getArgument(0));

		SenhaDTO resultado = senhaService
				 .emitirSenha(TipoSenha.NORMAL);

		assertEquals("N001", resultado.getCodigo());

		verify(senhaRepository).save(any(Senha.class));
		            
	}
	
	@Test
	void deveGerarN002QuandoExistirN001() {
		
		Senha senhaExistente = new Senha();
		senhaExistente.setCodigo("N001");

		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		     .thenReturn(Optional.of(senhaExistente));

		when(senhaRepository.save(any(Senha.class)))
		     .thenAnswer(invocation -> invocation.getArgument(0));

		SenhaDTO resultado = senhaService
				 .emitirSenha(TipoSenha.NORMAL);

		assertEquals("N002", resultado.getCodigo());
		
		verify(senhaRepository).save(any(Senha.class));
		
	}
	
	@Test
	void deveEmitirSenhaPreferencial() {
		
		when(senhaRepository.buscarUltimaSenha("PREFERENCIAL"))
		     .thenReturn(Optional.empty());

		when(senhaRepository.save(any(Senha.class)))
		     .thenAnswer(invocation -> invocation.getArgument(0));

		SenhaDTO resultado = senhaService
				 .emitirSenha(TipoSenha.PREFERENCIAL);

		assertEquals("P001", resultado.getCodigo());

		verify(senhaRepository).save(any(Senha.class));
		
		
	}
	
	@Test
	void deveGerarP002QuandoExistirP001() {

		Senha senhaExistente = new Senha();
		senhaExistente.setCodigo("P001");

		when(senhaRepository.buscarUltimaSenha("PREFERENCIAL"))
		     .thenReturn(Optional.of(senhaExistente));

		when(senhaRepository.save(any(Senha.class)))
		     .thenAnswer(invocation -> invocation.getArgument(0));

		SenhaDTO resultado = senhaService
				 .emitirSenha(TipoSenha.PREFERENCIAL);

		assertEquals("P002", resultado.getCodigo());
		
		verify(senhaRepository).save(any(Senha.class));

	}
	
	@Test
	void deveEmitirSenhaComStatusAguardando() {
		
		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		    .thenReturn(Optional.empty());
		
		when(senhaRepository.save(any(Senha.class)))
		    .then(invocation -> invocation.getArgument(0));
		
		senhaService.emitirSenha(TipoSenha.NORMAL);
		
		ArgumentCaptor<Senha> captor = 
				ArgumentCaptor.forClass(Senha.class);
		
		verify(senhaRepository).save(captor.capture());
		
		Senha senhaSalva = captor.getValue();
		
		assertEquals(StatusSenha.AGUARDANDO, senhaSalva.getStatus());
		
	}
	
	@Test
	void devePreencherDataCriacao() {
		
		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		   .thenReturn(Optional.empty());

		when(senhaRepository.save(any(Senha.class)))
		    .thenAnswer(invocation -> invocation.getArgument(0));

		senhaService.emitirSenha(TipoSenha.NORMAL);

		ArgumentCaptor<Senha> captor = ArgumentCaptor.forClass(Senha.class);

		verify(senhaRepository).save(captor.capture());

		Senha senhaSalva = captor.getValue();

		assertNotNull(senhaSalva.getDataCriacao());
	}
	
	@Test
	void devePreencherTipoDaSenha() {
		
		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		.thenReturn(Optional.empty());

		when(senhaRepository.save(any(Senha.class)))
		.thenAnswer(invocation -> invocation.getArgument(0));

		senhaService.emitirSenha(TipoSenha.NORMAL);

		ArgumentCaptor<Senha> captor = ArgumentCaptor
				.forClass(Senha.class);

		verify(senhaRepository).save(captor.capture());

		Senha senhaSalva = captor.getValue();

		assertEquals(TipoSenha.NORMAL, senhaSalva.getTipo());
		    
	}
	
	@Test
	void deveRetornarSenhaDTOCorretamente() {

		when(senhaRepository.buscarUltimaSenha("NORMAL"))
		     .thenReturn(Optional.empty());

		when(senhaRepository.save(any(Senha.class)))
		     .thenAnswer(invocation -> invocation.getArgument(0));

		SenhaDTO resultado = senhaService
				 .emitirSenha(TipoSenha.NORMAL);

		assertEquals("N001", resultado.getCodigo());
		assertNotNull(resultado.getDataCriacao());

	}
	
	@Test
	void deveLancarExcecaoQuandoJaExisteSenhaEmAtendimento() {

		Senha senhaAtendendo = new Senha();
		senhaAtendendo.setStatus(StatusSenha.ATENDENDO);

		when(senhaRepository.buscarSenhaEmAtendimento())
		     .thenReturn(Optional.of(senhaAtendendo));

		RuntimeException excecao = assertThrows(
				RuntimeException.class, 
				() -> senhaService.proximaSenha());

		assertEquals(
		    "Finalize o atendimento atual antes de chamar uma nova senha.",
		    excecao.getMessage()
        );
	}
	
	@Test
	void deveChamarSenhaPreferencialQuandoNaoExisteAtendimento() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		Senha senhaPreferencial = new Senha();
		senhaPreferencial.setCodigo("P001");
		senhaPreferencial.setTipo(TipoSenha.PREFERENCIAL);
		senhaPreferencial.setStatus(StatusSenha.AGUARDANDO);

		when(senhaRepository.buscarProximaPreferencial())
		     .thenReturn(Optional.of(senhaPreferencial));

		ProximaSenhaDTO resultado = senhaService.proximaSenha();

		assertEquals("P001", resultado.getCodigo());
		assertEquals(StatusSenha.ATENDENDO, senhaPreferencial.getStatus());

	}
	
	@Test
	void deveChamarSenhaNormalQuandoNaoExistePreferencial() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		when(senhaRepository.buscarProximaPreferencial())
		    .thenReturn(Optional.empty());

		Senha senhaNormal = new Senha();
		senhaNormal.setCodigo("N001");
		senhaNormal.setTipo(TipoSenha.NORMAL);
		senhaNormal.setStatus(StatusSenha.AGUARDANDO);

		when(senhaRepository.buscarProximaNormal())
		     .thenReturn(Optional.of(senhaNormal));

		ProximaSenhaDTO resultado = senhaService.proximaSenha();

		assertEquals("N001", resultado.getCodigo());
		assertEquals(StatusSenha.ATENDENDO, senhaNormal.getStatus());
	}
	
	@Test
	void deveChamarDuasPreferenciaisAntesDeNormal() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		Senha primeira = new Senha();
		primeira.setCodigo("P001");
		primeira.setTipo(TipoSenha.PREFERENCIAL);
		primeira.setStatus(StatusSenha.AGUARDANDO);

		Senha segunda = new Senha();
		segunda.setCodigo("P002");
		segunda.setTipo(TipoSenha.PREFERENCIAL);
		segunda.setStatus(StatusSenha.AGUARDANDO);

		when(senhaRepository.buscarProximaPreferencial())
		    .thenReturn(
		    		Optional.of(primeira), 
		    		Optional.of(segunda)
		    );

		ProximaSenhaDTO resultado1 = senhaService.proximaSenha();
		ProximaSenhaDTO resultado2 = senhaService.proximaSenha();

		assertEquals("P001", resultado1.getCodigo());
		assertEquals("P002", resultado2.getCodigo());
	}
	
	@Test
	void deveChamarNormalDepoisDeDuasPreferenciais() {

		when(senhaRepository.buscarSenhaEmAtendimento()).thenReturn(Optional.empty());

		Senha primeira = new Senha();
		primeira.setCodigo("P001");
		primeira.setTipo(TipoSenha.PREFERENCIAL);
		primeira.setStatus(StatusSenha.AGUARDANDO);

		Senha segunda = new Senha();
		segunda.setCodigo("P002");
		segunda.setTipo(TipoSenha.PREFERENCIAL);
		segunda.setStatus(StatusSenha.AGUARDANDO);

		Senha normal = new Senha();
		normal.setCodigo("N001");
		normal.setTipo(TipoSenha.NORMAL);
		normal.setStatus(StatusSenha.AGUARDANDO);

		when(senhaRepository.buscarProximaPreferencial())
		     .thenReturn(Optional.of(primeira), Optional.of(segunda));

		when(senhaRepository.buscarProximaNormal())
		     .thenReturn(Optional.of(normal));

		senhaService.proximaSenha();
		senhaService.proximaSenha();

		ProximaSenhaDTO resultado3 = senhaService.proximaSenha();

		assertEquals("N001", resultado3.getCodigo());

	}
	
	@Test
	void deveVoltarParaPreferencialQuandoNaoHouverSenhaNormal() {
		
		when(senhaRepository.buscarSenhaEmAtendimento())
        .thenReturn(Optional.empty());
		
		Senha primeira = new Senha();
	    primeira.setCodigo("P001");
	    primeira.setTipo(TipoSenha.PREFERENCIAL);
	    primeira.setStatus(StatusSenha.AGUARDANDO);

	    Senha segunda = new Senha();
	    segunda.setCodigo("P002");
	    segunda.setTipo(TipoSenha.PREFERENCIAL);
	    segunda.setStatus(StatusSenha.AGUARDANDO);

	    Senha terceira = new Senha();
	    terceira.setCodigo("P003");
	    terceira.setTipo(TipoSenha.PREFERENCIAL);
	    terceira.setStatus(StatusSenha.AGUARDANDO);
	    
	    when(senhaRepository.buscarProximaPreferencial())
	         .thenReturn(
	        		 Optional.of(primeira),
	        		 Optional.of(segunda),
	        		 Optional.of(terceira)
	        		 
	        		 );
	    
	    when(senhaRepository.buscarProximaNormal())
	         .thenReturn(Optional.empty());
	    
	    senhaService.proximaSenha();
	    senhaService.proximaSenha();
	    
	    ProximaSenhaDTO resultado =
	    		senhaService.proximaSenha();
	    
	    assertEquals("P003", resultado.getCodigo());
	    
	    
	}
	
	@Test
	void deveLancarExcecaoQuandoNaoHaSenhasAguardando() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		when(senhaRepository.buscarProximaPreferencial())
		    .thenReturn(Optional.empty());

		when(senhaRepository.buscarProximaNormal())
		    .thenReturn(Optional.empty());

		RuntimeException execao = assertThrows(
				RuntimeException.class, 
				() -> senhaService.proximaSenha());

		assertEquals(
				"Não há senhas aguardando.", 
				execao.getMessage());
	
	}
	
	@Test
	void devePreencherDataInicioAtendimentoAoChamarSenha() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		Senha senhaNormal = new Senha();
		senhaNormal.setCodigo("N001");
		senhaNormal.setTipo(TipoSenha.NORMAL);
		senhaNormal.setStatus(StatusSenha.AGUARDANDO);

		when(senhaRepository.buscarProximaPreferencial())
		     .thenReturn(Optional.empty());

		when(senhaRepository.buscarProximaNormal())
		     .thenReturn(Optional.of(senhaNormal));

		senhaService.proximaSenha();

		assertNotNull(senhaNormal.getDataInicioAtendimento());

	}
	
	@Test
	void deveSalvarSenhaAoChamarProximaSenha() {
		
		 when(senhaRepository.buscarSenhaEmAtendimento())
             .thenReturn(Optional.empty());
		
		when(senhaRepository.buscarProximaPreferencial())
        .thenReturn(Optional.empty());
		
		Senha senhaNormal = new Senha();
	    senhaNormal.setCodigo("N001");
	    senhaNormal.setTipo(TipoSenha.NORMAL);
	    senhaNormal.setStatus(StatusSenha.AGUARDANDO);
	    
	    when(senhaRepository.buscarProximaNormal())
	        .thenReturn(Optional.of(senhaNormal));
	    
	    senhaService.proximaSenha();
	    
	    verify(senhaRepository).save(senhaNormal);
		
	}
	
	@Test
	void deveFinalizarSenhaEmAtendimento() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		when(senhaRepository.finalizarAtendimentoSenha())
		     .thenReturn(Optional.of(senha));

		when(senhaRepository.save(any(Senha.class)))
		     .thenAnswer(invocation -> invocation.getArgument(0));

		FinalizarSenhaDTO resultado = 
				senhaService.finalizarSenha();

		assertEquals(
				StatusSenha.FINALIZADO, 
				resultado.getStatus()
				
		);

	}
	
	@Test
	void devePreencherDataFimAtendimentoAoFinalizarSenha() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		when(senhaRepository.finalizarAtendimentoSenha())
		    .thenReturn(Optional.of(senha));

		when(senhaRepository.save(any(Senha.class)))
		    .thenAnswer(invocation -> invocation.getArgument(0));

		senhaService.finalizarSenha();

		assertNotNull(senha.getDataFimAtendimento());

	}
	
	@Test
	void deveSalvarSenhaAoFinalizarAtendimento() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		when(senhaRepository.finalizarAtendimentoSenha())
		    .thenReturn(Optional.of(senha));

		senhaService.finalizarSenha();

		verify(senhaRepository).save(senha);
	}
	
	@Test
	void deveLancarExcecaoQuandoNaoHaSenhaEmAtendimento() {

		when(senhaRepository.finalizarAtendimentoSenha())
		    .thenReturn(Optional.empty());

		RuntimeException excecao = assertThrows(
				RuntimeException.class, 
				() -> senhaService.finalizarSenha()
		);

		assertEquals(
				"Não há senha em atendimento.", 
				excecao.getMessage()
		);

	}
	
	@Test
	void deveRetornarSenhaAtualEmAtendimento() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.of(senha));

		ProximaSenhaDTO resultado = 
				senhaService.buscarSenhaAtual();

		assertEquals("N001", resultado.getCodigo());

	}
	
	@Test
	void deveLancarExcecaoQuandoNaoHaSenhaAtual() {

		when(senhaRepository.buscarSenhaEmAtendimento())
		    .thenReturn(Optional.empty());

		RuntimeException excecao = assertThrows(
				RuntimeException.class, 
				() -> senhaService.buscarSenhaAtual());

		assertEquals(
				"Nenhuma senha em atendimento", 
				 excecao.getMessage());

	}

}
