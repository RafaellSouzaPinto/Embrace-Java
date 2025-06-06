package com.fiap.embrace.embrace.service;

import com.fiap.embrace.embrace.dto.ColetivoDTO;
import com.fiap.embrace.embrace.entities.*;
import com.fiap.embrace.embrace.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColetivoService {

    @Autowired
    private ColetivoRepository repository;

    @Autowired
    private CampanhaRepository campanhaRepo;

    @Autowired
    private AnuncioRepository anuncioRepo;

    @Autowired
    private DoacaoRepository doacaoRepo;

    @Autowired
    private OfertaRepository ofertaRepo;

    public ColetivoDTO salvar(ColetivoDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado para outro Coletivo.");
        }

        Coletivo coletivo = new Coletivo(
                null,
                dto.getNome(),
                dto.getEmail(),
                dto.getSenha(),
                dto.getTelefone(),
                "COLETIVO",
                dto.getRepresentante()
        );
        coletivo = repository.save(coletivo);

        return new ColetivoDTO(
                coletivo.getId(),
                coletivo.getNome(),
                coletivo.getEmail(),
                coletivo.getSenha(),
                coletivo.getTelefone(),
                coletivo.getRepresentante()
        );
    }

    public Page<ColetivoDTO> listar(String nome, Pageable pageable) {
        Page<Coletivo> page;
        if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(coletivo -> new ColetivoDTO(
                coletivo.getId(),
                coletivo.getNome(),
                coletivo.getEmail(),
                coletivo.getSenha(),
                coletivo.getTelefone(),
                coletivo.getRepresentante()
        ));
    }

    public ColetivoDTO buscarPorId(Long id) {
        Optional<Coletivo> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Coletivo não encontrado");
        }
        Coletivo c = opt.get();
        return new ColetivoDTO(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.getSenha(),
                c.getTelefone(),
                c.getRepresentante()
        );
    }

    public ColetivoDTO atualizar(Long id, ColetivoDTO dto) {
        Optional<Coletivo> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Coletivo não encontrado");
        }

        Coletivo existente = opt.get();
        if (!existente.getEmail().equals(dto.getEmail())
                && repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado para outro Coletivo.");
        }

        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setSenha(dto.getSenha());
        existente.setTelefone(dto.getTelefone());
        existente.setRepresentante(dto.getRepresentante());

        Coletivo atualizado = repository.save(existente);
        return new ColetivoDTO(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getEmail(),
                atualizado.getSenha(),
                atualizado.getTelefone(),
                atualizado.getRepresentante()
        );
    }

    @Transactional
    public void deletar(Long id) {
        Optional<Coletivo> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("ID não encontrado");
        }
        Coletivo coletivo = opt.get();

        // Apagar campanhas criadas pelo coletivo
        List<Campanha> campanhas = campanhaRepo.findByCriadorId(coletivo.getId());
        campanhaRepo.deleteAll(campanhas);

        // Apagar anúncios criados pelo coletivo
        List<AnuncioMarketplace> anuncios = anuncioRepo.findByAutorId(coletivo.getId());
        anuncioRepo.deleteAll(anuncios);

        // Apagar doações feitas pelo coletivo
        List<Doacao> doacoes = doacaoRepo.findByDoadorId(coletivo.getId());
        doacaoRepo.deleteAll(doacoes);

        // Apagar ofertas feitas pelo coletivo
        List<Oferta> ofertas = ofertaRepo.findByDoadorId(coletivo.getId());
        ofertaRepo.deleteAll(ofertas);

        // Excluir o coletivo
        repository.deleteById(id);
    }
}
