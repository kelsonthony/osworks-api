package com.kelsonthony.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelsonthony.osworks.api.exception.EntidadeNaoEncontradaException;
import com.kelsonthony.osworks.api.exception.NegocioException;
import com.kelsonthony.osworks.api.model.Comentario;
import com.kelsonthony.osworks.domain.model.Cliente;
import com.kelsonthony.osworks.domain.model.OrdemServico;
import com.kelsonthony.osworks.domain.model.StatusOrdemServico;
import com.kelsonthony.osworks.domain.repository.ClienteRepository;
import com.kelsonthony.osworks.domain.repository.ComentarioRepository;
import com.kelsonthony.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado."));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
		
	}
	
	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço nao encontrada"));
	}
	
	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);
	}

	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		//ordemServico.setStatus(StatusOrdemServico.FINALIZADA);
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}
}
