package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BookRepository;

@Service
public class BookServices {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;
	
	public PagedModel<EntityModel<BookVO>> findAllBooks(Pageable pageable) {
		
		var bookPage = bookRepository.findAll(pageable);
		
		var bookVosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
		bookVosPage.map(p -> p.add(linkTo(methodOn(BookController.class).findBookById(p.getId())).withSelfRel()));
		
		Link link = linkTo(methodOn(BookController.class).findAllBooks(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(bookVosPage, link);
	}
	
	public BookVO findBookById(Long id) {
		
		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findBookById(id)).withSelfRel());
		return vo;
	}
	
	public BookVO createBook(BookVO book) {
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = DozerMapper.parseObject(book, Book.class);
		var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findBookById(vo.getId())).withSelfRel());
		return vo;
		
	}
	
	public BookVO updateBook(BookVO book) {
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = bookRepository.findById(book.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerMapper.parseObject(bookRepository.save(entity), BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findBookById(vo.getId())).withSelfRel());
		
		return vo;
	}

	public void deleteBook(Long id) {
		var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		bookRepository.delete(entity);
	}
}
