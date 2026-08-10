package com.devs.filabancorev.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devs.filabancorev.dto.ProximaSenhaDTO;
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
	
	private int contadorPreferencial = 0;
	
	public ProximaSenhaDTO proximaSenha() {
		 
		  Optional<Senha> senhaAtendendo = senhaRepository
				                          .buscarSenhaEmAtendimento();
		  
		  if(senhaAtendendo.isPresent()) {
			 
			   throw new RuntimeException("Finalize o atendimento atual antes de chamar uma nova senha.");
		  }
		  
		  Optional<Senha> senhaEncontrada;
		  
		  if(contadorPreferencial < 2) {
			  
			  senhaEncontrada = senhaRepository
					            .buscarProximaPreferencial();
			  
			  if(senhaEncontrada.isPresent()) {
				   
				  contadorPreferencial++;
				  
			  }else {
				  
				  senhaEncontrada = senhaRepository
						            .buscarProximaNormal();
			  }
			  
		  }else {
			  
			   senhaEncontrada = senhaRepository
					             .buscarProximaNormal();
			   
			   if(senhaEncontrada.isPresent()) {
				   
				    contadorPreferencial = 0;
			   
			   }else {
				   
				   senhaEncontrada = senhaRepository.buscarProximaPreferencial();
			   }
		  }
		  
		  if(senhaEncontrada.isEmpty()) {
			  
			  throw new RuntimeException("Não há senhas aguardando.");
		  }
		  
		  Senha senha = senhaEncontrada.get();
		  senha.setStatus(StatusSenha.ATENDENDO);
		  senha.setDataInicioAtendimento(LocalDateTime.now());
		  senhaRepository.save(senha);
		  ProximaSenhaDTO dto = new ProximaSenhaDTO(senha);
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
