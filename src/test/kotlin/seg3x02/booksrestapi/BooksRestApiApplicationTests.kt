package seg3x02.booksrestapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import seg3x02.booksrestapi.entities.Author
import seg3x02.booksrestapi.entities.Book
import seg3x02.booksrestapi.repository.AuthorRepository
import java.util.*
import org.mockito.Mockito.`when`

@SpringBootTest
@AutoConfigureMockMvc
class BooksRestApiApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun contextLoads() {
    }

    @Test
    fun `get author by id returns author when exists`() {
        // Arrange
        val author = Author().apply {
            id = 1L
            firstName = "John"
            lastName = "Doe"
            books = mutableListOf(Book().apply {
                id = 1L
                title = "Test Book"
                category = "Tech"
                cost = 29.99
            })
        }

        `when`(authorRepository.findById(1L)).thenReturn(Optional.of(author))

        // Act & Assert
        mockMvc.perform(get("/books-api/authors/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.books[0].title").value("Test Book"))
    }

    @Test
    fun `get author by id returns 404 when author does not exist`() {
        // Arrange
        `when`(authorRepository.findById(999L)).thenReturn(Optional.empty())

        // Act & Assert
        mockMvc.perform(get("/books-api/authors/999")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `get authors by name returns matching authors`() {
        // Arrange
        val author = Author().apply {
            id = 1L
            firstName = "John"
            lastName = "Doe"
        }

        `when`(authorRepository.findAuthorsByName("John", "Doe"))
            .thenReturn(listOf(author))

        // Act & Assert
        mockMvc.perform(get("/books-api/authors")
            .param("firstName", "John")
            .param("lastName", "Doe")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._embedded.authors[0].id").value(1))
            .andExpect(jsonPath("$._embedded.authors[0].firstName").value("John"))
            .andExpect(jsonPath("$._embedded.authors[0].lastName").value("Doe"))
    }

    @Test
    fun `get all authors returns list of authors`() {
        // Arrange
        val authors = listOf(
            Author().apply {
                id = 1L
                firstName = "John"
                lastName = "Doe"
            },
            Author().apply {
                id = 2L
                firstName = "Jane"
                lastName = "Smith"
            }
        )

        `when`(authorRepository.findAll()).thenReturn(authors)

        // Act & Assert
        mockMvc.perform(get("/books-api/authors")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._embedded.authors.length()").value(2))
            .andExpect(jsonPath("$._embedded.authors[0].firstName").value("John"))
            .andExpect(jsonPath("$._embedded.authors[1].firstName").value("Jane"))
    }

    @Test
    fun `get author books returns list of books`() {
        // Arrange
        val author = Author().apply {
            id = 1L
            firstName = "John"
            lastName = "Doe"
            books = mutableListOf(
                Book().apply {
                    id = 1L
                    title = "Book 1"
                },
                Book().apply {
                    id = 2L
                    title = "Book 2"
                }
            )
        }

        `when`(authorRepository.findById(1L)).thenReturn(Optional.of(author))

        // Act & Assert
        mockMvc.perform(get("/books-api/authors/1/books")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Book 1"))
            .andExpect(jsonPath("$[1].title").value("Book 2"))
    }
}
