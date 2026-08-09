package com.devs.filabancorev.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devs.filabancorev.dto.SenhaDTO;
import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.enums.TipoSenha;
import com.devs.filabancorev.model.Senha;
import com.devs.filabancorev.repository.SenhaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SenhaService {
	
	private final SenhaRepository senhaRepository;
	
	public SenhaDTO emitirSenha(TipoSenha tipo) {
		
		 Senha senha = new Senha();
		 
		 senha.setTipo(tipo);
		 
		 gerarCodigo(senha);
		 
		 preencherDadosIniciais(senha);
		 Senha salvar = senhaRepository.save(senha);
		 SenhaDTO dto = new SenhaDTO(salvar);
		 return dto;
	}
	
	private void preencherDadosIniciais(Senha senha) {
		senha.setStatus(StatusSenha.AGUARDANDO);
		senha.setDataCriacao(LocalDateTime.now());
	}

	private void gerarCodigo(Senha senha) {
		 
		 String codigo;
		 TipoSenha tipo = senha.getTipo();
		 String prefixo;
		 
	     Optional<Senha>ultimaSenha = senhaRepository
	    		                      .buscarUltimaSenha(tipo.name());
	     
	     if(ultimaSenha.isPresent()) {
	    	 Senha ultima = ultimaSenha.get();
	    	 String codigoAnterior = ultima.getCodigo();
	    	 
	    	 String numeroTexto = codigoAnterior.replaceAll("[^0-9]", "");
	    	 int numero = Integer.parseInt(numeroTexto);
	    	 numero++;
	    	 String numeroFormatado = String.format("%03d", numero);
	    	 
	    	 if(tipo == TipoSenha.NORMAL) {
	    		 prefixo = "N";
	    		 codigo = prefixo + numeroFormatado;
	    	 }else {
	    		 prefixo = "P";
	    		 codigo = prefixo + numeroFormatado; 
	    	 }
	    	 senha.setCodigo(codigo);
	     
	     }else {
	    	 String codigoInicial;
	    	 
	    	 if(tipo == TipoSenha.NORMAL) {
	    		 codigoInicial = "N001";
	    	 }else {
	    		 codigoInicial = "P001";
	    	 }
	    	 
	    	 senha.setCodigo(codigoInicial);
	     }
		
	}

}
