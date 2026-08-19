package com.devs.filabancorev.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devs.filabancorev.dto.FinalizarSenhaDTO;
import com.devs.filabancorev.dto.ProximaSenhaDTO;
import com.devs.filabancorev.dto.SenhaDTO;
import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.enums.TipoSenha;
import com.devs.filabancorev.model.Senha;
import com.devs.filabancorev.repository.SenhaRepository;

import lombok.RequiredArgsConstructor;
/**
 * Service responsável pelo gerenciamento das regras
 * de negócio relacionadas às senhas de atendimento.
 * 
 * É responsável por controlar a emissão, chamada e 
 * finalização da senhas, além de consultar a senha
 * atualmente em atendimento.
 * 
 * Também realiza a geração sequencial dos códigos
 * das senhas Normais e Preferenciais.
 */
@Service
@RequiredArgsConstructor
public class SenhaService {

	private final SenhaRepository senhaRepository;
	
	/**
	 * Emite uma nova senha de acordo como tipo informado.
	 * 
	 * A senha recebe um código sequencial, status {@code AGUARDANDO}
	 * e a data de criação. Após ser salva no banco de dados,
	 * a entidade é convertida para {@link SenhaDTO}.
	 * 
	 * @param tipo tipo da senha a ser emitida
	 *             (NORMAL ou PREFERENCIAL)
	 * @return DTO contendo os dados da senha emitida.
	 */
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

	/**
	 * Seleciona a próxima senha para atendimento.
	 * 
	 * Antes de realizar uma nova chamada, verifica
	 * se já existe uma senha em atendimento. Caso
	 * exista, a operação é interrompida até que
	 * o atendimento atual seja finalizado.
	 * 
	 * A seleção das senhas segue uma regra de prioridade:
	 * são chamadas até duas senhas preferenciais antes de 
	 * uma senha Normal, quando houver senhas disponíveis.
	 * 
	 * A senha selecionada recebe o status {@code ATENDENDO}
	 * e tem registrada a data de início de atendimento.
	 * 
	 * @return DTO contendo os dados da senha chamada
	 * @throws RuntimeException caso já exista uma senha
	 *         em atendimento ou não existam senhas aguardando.
	 */
	public ProximaSenhaDTO proximaSenha() {

		Optional<Senha> senhaAtendendo = senhaRepository.buscarSenhaEmAtendimento();

		if (senhaAtendendo.isPresent()) {

			throw new RuntimeException("Finalize o atendimento atual antes de chamar uma nova senha.");
		}

		Optional<Senha> senhaEncontrada;

		if (contadorPreferencial < 2) {

			senhaEncontrada = senhaRepository.buscarProximaPreferencial();

			if (senhaEncontrada.isPresent()) {

				contadorPreferencial++;

			} else {

				senhaEncontrada = senhaRepository.buscarProximaNormal();
			}

		} else {

			senhaEncontrada = senhaRepository.buscarProximaNormal();

			if (senhaEncontrada.isPresent()) {

				contadorPreferencial = 0;

			} else {

				senhaEncontrada = senhaRepository.buscarProximaPreferencial();
			}
		}

		if (senhaEncontrada.isEmpty()) {

			throw new RuntimeException("Não há senhas aguardando.");
		}

		Senha senha = senhaEncontrada.get();
		senha.setStatus(StatusSenha.ATENDENDO);
		senha.setDataInicioAtendimento(LocalDateTime.now());
		senhaRepository.save(senha);
		ProximaSenhaDTO dto = new ProximaSenhaDTO(senha);
		return dto;

	}

	/**
	 * Finaliza o atendimento da senha atualmente 
	 * em atendimento.
	 * 
	 * A senha tem seu status alterado para {@code FINALIZADO}
	 * e recebe a data de término do atendimento.
	 * 
	 * @return DTO contendo os dados da senha finalizada
	 * @throws RuntimException caso não exista uma senha
	 *         em atendimento.
	 */
	public FinalizarSenhaDTO finalizarSenha() {

		Optional<Senha> finalizarSenha = senhaRepository
				                         .finalizarAtendimentoSenha();

		if (finalizarSenha.isEmpty()) {

			throw new RuntimeException("Não há senha em atendimento.");
		}

		Senha senha = finalizarSenha.get();
		senha.setStatus(StatusSenha.FINALIZADO);
		senha.setDataFimAtendimento(LocalDateTime.now());
		senhaRepository.save(senha);
		FinalizarSenhaDTO dto = new FinalizarSenhaDTO(senha);
		return dto;

	}
	
	/**
	 * Busca a senha que está atualmente em atendimento.
	 * 
	 * @return DTO contento os dados da senha em atendimento
	 * @throws RuntimeException caso não exista nenhuma
	 *         senha em atendimento.
	 */
	public ProximaSenhaDTO buscarSenhaAtual() {

		Optional<Senha> senha = senhaRepository
				                .buscarSenhaEmAtendimento();

		if (senha.isEmpty()) {

			throw new RuntimeException("Nenhuma senha em atendimento");
		}

		return new ProximaSenhaDTO(senha.get());
	}
	
	/**
	 * Preenche os dados iniciais da senha antes da 
	 * persistência.
	 * 
	 * Define o status inicial como {@code AGUARDANDO}
	 * e registra a data e hora de criação da senha.
	 * 
	 * @param senha senha que receberá os dados iniciais.
	 */
     private void preencherDadosIniciais(Senha senha) {
		senha.setStatus(StatusSenha.AGUARDANDO);
		senha.setDataCriacao(LocalDateTime.now());
	}

     /**
      * Gera o código sequencial da senha de acordo com
      * seu tipo.
      * 
      * O código utiliza os prefixos {@code N} para 
      * senha Normais e {@code P} para preferenciais,
      * seguido de uma numeração com três digitos.
      * 
      * Quando já existe uma senha do mesmo tipo emitida
      * no dia, o número do último código é incrementado.
      * Caso não exista a sequência é iniciada em {@code N001}
      * ou {@code P001}.
      * 
      * @param senha senha que receberá o código gerado.
      */
	private void gerarCodigo(Senha senha) {

		String codigo;
		TipoSenha tipo = senha.getTipo();
		String prefixo;

		Optional<Senha> ultimaSenha = senhaRepository.buscarUltimaSenha(tipo.name());

		if (ultimaSenha.isPresent()) {
			Senha ultima = ultimaSenha.get();
			String codigoAnterior = ultima.getCodigo();

			String numeroTexto = codigoAnterior.replaceAll("[^0-9]", "");
			int numero = Integer.parseInt(numeroTexto);
			numero++;
			String numeroFormatado = String.format("%03d", numero);

			if (tipo == TipoSenha.NORMAL) {
				prefixo = "N";
				codigo = prefixo + numeroFormatado;
			} else {
				prefixo = "P";
				codigo = prefixo + numeroFormatado;
			}
			senha.setCodigo(codigo);

		} else {
			String codigoInicial;

			if (tipo == TipoSenha.NORMAL) {
				codigoInicial = "N001";
			} else {
				codigoInicial = "P001";
			}

			senha.setCodigo(codigoInicial);
		}

	}

}
