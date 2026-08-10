package com.devs.filabancorev.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@RestController
@RequestMapping("/api/senha")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SenhaController {

	private SenhaService senhaService;

	@PostMapping("/{tipo}")
	@ResponseStatus(HttpStatus.CREATED)
	public SenhaDTO emitirSenha(@PathVariable TipoSenha tipo) {
		return senhaService.emitirSenha(tipo);

	}

	@PostMapping("/chamar")
	@ResponseStatus(HttpStatus.CREATED)
	public ProximaSenhaDTO proximaSenha() {
		return senhaService.proximaSenha();

	}

	@PostMapping("/finalizar")
	@ResponseStatus(HttpStatus.CREATED)
	public FinalizarSenhaDTO finalizarSenha() {
		return senhaService.finalizarSenha();
	}

}
