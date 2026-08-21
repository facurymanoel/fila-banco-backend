package com.devs.filabancorev.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.devs.filabancorev.service.SenhaService;

/**
 * Testes unitários para os endpoints
 * da classe {@link SenhaController}.
 * 
 * Utiliza Mockito para simular o comportamento
 * do {@link SenhaService}, permitindo testar
 * o controller de forma isolada.
 */
@ExtendWith(MockitoExtension.class)
public class SenhaControllerTest {
	
	@Mock
	private SenhaService senhaService;
	
	@InjectMocks
	private SenhaController senhaController;
	
	@Test
	void deveEmitirSenhaNormal() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.AGUARDANDO);

		SenhaDTO dto = new SenhaDTO(senha);

		when(senhaService.emitirSenha(TipoSenha.NORMAL))
		    .thenReturn(dto);

		SenhaDTO resultado = 
				senhaController.emitirSenha(TipoSenha.NORMAL);

		assertSame(dto, resultado);

		verify(senhaService).emitirSenha(TipoSenha.NORMAL);

	}
	
	@Test
	void deveEmitirSenhaPreferencial() {

		Senha senha = new Senha();
		senha.setCodigo("P001");
		senha.setTipo(TipoSenha.PREFERENCIAL);
		senha.setStatus(StatusSenha.AGUARDANDO);

		SenhaDTO dto = new SenhaDTO(senha);

		when(senhaService.emitirSenha(TipoSenha.PREFERENCIAL))
		    .thenReturn(dto);

		SenhaDTO resultado = 
			senhaController.emitirSenha(TipoSenha.PREFERENCIAL);

		assertSame(dto, resultado);

		verify(senhaService)
		   .emitirSenha(TipoSenha.PREFERENCIAL);

	}
	
	@Test
	void deveChamarProximaSenha() {
		
		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		ProximaSenhaDTO dto = new ProximaSenhaDTO(senha);

		when(senhaService.proximaSenha())
		     .thenReturn(dto);

		ProximaSenhaDTO resultado = 
			senhaController.proximaSenha();

		assertSame(dto, resultado);

		verify(senhaService).proximaSenha();
	}
	
	@Test
	void deveFinalizarSenha() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.FINALIZADO);

		FinalizarSenhaDTO dto = new FinalizarSenhaDTO(senha);

		when(senhaService.finalizarSenha())
		    .thenReturn(dto);

		FinalizarSenhaDTO resultado = 
				senhaController.finalizarSenha();

		assertSame(dto, resultado);

		verify(senhaService).finalizarSenha();
	}
	
	@Test
	void deveBuscarSenhaAtualNoPainel() {

		Senha senha = new Senha();
		senha.setCodigo("N001");
		senha.setTipo(TipoSenha.NORMAL);
		senha.setStatus(StatusSenha.ATENDENDO);

		ProximaSenhaDTO dto = new ProximaSenhaDTO(senha);

		when(senhaService.buscarSenhaAtual())
		     .thenReturn(dto);

		ProximaSenhaDTO resultado = 
				senhaController.painel();

		assertSame(dto, resultado);

		verify(senhaService).buscarSenhaAtual();

	}

}
