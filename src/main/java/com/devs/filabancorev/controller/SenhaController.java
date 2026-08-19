package com.devs.filabancorev.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devs.filabancorev.dto.FinalizarSenhaDTO;
import com.devs.filabancorev.dto.ProximaSenhaDTO;
import com.devs.filabancorev.dto.SenhaDTO;
import com.devs.filabancorev.enums.TipoSenha;
import com.devs.filabancorev.service.SenhaService;

import lombok.AllArgsConstructor;
/**
 * Controller responsável por expor os endpoints
 * relacionados ao gerenciamento das senhas.
 * 
 * Atua como camada de entrada da API REST,
 * delegando as regras de negócio para a camada
 * de serviço ({@link SenhaService}).
 */

@RestController
@RequestMapping("/api/senha")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SenhaController {

	private SenhaService senhaService;
	
	/**
	 * Emite uma nova senha de acordo com o tipo informado.
	 * 
	 * @param tipo tipo da senha a ser emitida
	 *        (NORMAL ou PREFERENCIAL)
	 * @return dados da senha emitida
	 */
    @PostMapping("/{tipo}")
	@ResponseStatus(HttpStatus.CREATED)
	public SenhaDTO emitirSenha(@PathVariable TipoSenha tipo) {
		return senhaService.emitirSenha(tipo);

	}

    /**
     * Chama a próxima senha para atendimento.
     * 
     * O serviço verifica a existência de uma senha
     * em atendimento e aplica a regra de prioridade
     * entre senhas preferenciais e normais.
     * 
     * @return dados da senha chamada para atendimento.
     * @throws RuntimeException caso já exista uma senha
     *         em atendimento ou não existam senhas aguardando.
     */
	@PostMapping("/chamar")
	@ResponseStatus(HttpStatus.CREATED)
	public ProximaSenhaDTO proximaSenha() {
		return senhaService.proximaSenha();

	}
	
	/**
	 * Finaliza o atendimento da senha atualmente em atendimento.
	 * 
	 * @return dados da senha após a finalização do atendimento.
	 * @throws RuntimeException caso não exista uma senha
	 *         em atendimento.
	 */
    @PostMapping("/finalizar")
	@ResponseStatus(HttpStatus.CREATED)
	public FinalizarSenhaDTO finalizarSenha() {
		return senhaService.finalizarSenha();
	}
    
    /**
     * Consulta a senha atualmente em atendimento.
     * 
     * Esse endpoint é utilizado para obter dados
     * da senha que está sendo atendida no momento.
     * 
     * @return dados da senha atualmente em atendimento.
     * @throws RuntimeException caso não exista uma senha
     *         em atendimento.
     */
	@GetMapping("/painel")
	public ProximaSenhaDTO painel() {
		return senhaService.buscarSenhaAtual();
	}

}
