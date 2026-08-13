package com.devs.filabancorev.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
	
	

}
