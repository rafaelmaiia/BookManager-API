package br.com.erudio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.BookServices;
import br.com.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookController {

	@Autowired
	BookServices bookService;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds all Book", 
		description = "Finds all Book",
		tags = {"Book"}, 
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
				content = {
					@Content(
						mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public List<BookVO> findAllBooks() {
		var allBooks = bookService.findAllBooks();
		return allBooks;
	}
	
	@GetMapping(value = "/{id}", 
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a Book", 
		description = "Finds a Book",
		tags = {"Book"}, 
		responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
				content = @Content(schema = @Schema(implementation = BookVO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public BookVO findBookById(@PathVariable(value = "id") Long id) {
		return bookService.findBookById(id);
	}
	
	@PostMapping(
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
		@Operation(summary = "Adds a new Book", 
			description = "Adds a new Book by passing in a JSON, XML or YML representation of the person!",
			tags = {"Book"}, 
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = @Content(schema = @Schema(implementation = BookVO.class))
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public BookVO createBook(@RequestBody BookVO book) {
		return bookService.createBook(book);
	}
	
	@PutMapping(
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a Book", 
		description = "Updates a Book by passing in a JSON, XML or YML representation of the person!",
		tags = {"Book"}, 
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200", 
				content = @Content(schema = @Schema(implementation = BookVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public BookVO updateBook(@RequestBody BookVO book) {
		return bookService.updateBook(book);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a Book", 
		description = "Deletes a Book by passing in a JSON, XML or YML representation of the person!",
		tags = {"Book"}, 
		responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Long id) {
		bookService.deleteBook(id);
		
		return ResponseEntity.noContent().build();
	}
}
