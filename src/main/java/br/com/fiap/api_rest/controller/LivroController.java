package br.com.fiap.api_rest.controller;

import br.com.fiap.api_rest.dto.LivroRequest;
import br.com.fiap.api_rest.dto.LivroResponse;
import br.com.fiap.api_rest.model.Livro;
import br.com.fiap.api_rest.repository.LivroRepository;
import br.com.fiap.api_rest.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/livros")
public class LivroController {
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private LivroService livroService;

    // CREATE, READ, UPDATE, DELETE
    // POST, GET, PUT, DELETE

    @PostMapping
    public ResponseEntity<Livro> createLivro(@RequestBody LivroRequest livro) {
        Livro livroSalvo = livroRepository.save(livroService.requestToLivro(livro));
        return new ResponseEntity<>(livroSalvo,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> readLivros() {
        List<Livro> livros = livroRepository.findAll();
        List<LivroResponse> listaLivros = new ArrayList<>();

        for (Livro livro : livros) {
            listaLivros.add(livroService.livroToResponse(livro));
        }

        return new ResponseEntity<>(listaLivros, HttpStatus.OK);
    }

    // @PathVariable localhost:8080/livros/1
    // @RequestParam localhost:8080/livros/?id=1
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> readLivro(@PathVariable Long id) {
        Optional<Livro> livro = livroRepository.findById(id);
        if (livro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(livroService.livroToResponse(livro.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> updateLivro(@RequestBody LivroRequest livro,
                                             @PathVariable Long id) {
        Optional<Livro> livroExistente = livroRepository.findById(id);
        if (livroExistente.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Livro livroConvertido = livroService.requestToLivro(livro);
        livroConvertido.setId(livroExistente.get().getId());
        Livro livroSalvo = livroRepository.save(livroConvertido);
        return new ResponseEntity<>(livroSalvo,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        livroRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
