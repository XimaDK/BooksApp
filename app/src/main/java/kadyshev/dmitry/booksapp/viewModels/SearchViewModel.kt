package kadyshev.dmitry.booksapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kadyshev.dmitry.booksapp.states.SearchFragmentState
import kadyshev.dmitry.booksapp.utils.BookUtils
import kadyshev.dmitry.domain.entities.Book
import kadyshev.dmitry.domain.useCases.FetchBookUseCase
import kadyshev.dmitry.domain.useCases.GetBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.RemoveBooksFromDataSourceUseCase
import kadyshev.dmitry.domain.useCases.SaveBookToDataSourceUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchViewModel(
    private val fetchBookUseCase: FetchBookUseCase,
    private val saveBookToDataSourceUseCase: SaveBookToDataSourceUseCase,
    private val removeBooksFromDataSourceUseCase: RemoveBooksFromDataSourceUseCase,
    private val getBooksFromDataSourceUseCase: GetBooksFromDataSourceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchFragmentState>(SearchFragmentState.Idle)
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    private var authors = ""

    fun updateAuthor(author: String) {
        authors = author
    }

    fun searchBooks(query: String, skipDelay: Boolean = false) {
        if (query.isBlank()) {
            _state.value = SearchFragmentState.Idle
            return
        }

        _state.value = SearchFragmentState.Loading

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                val authorFilter = if (authors.isNotBlank()) {
                    "inauthor:${authors}"
                } else {
                    ""
                }

                val sortByFilters = mutableListOf<String>()
                if (_selectedFilters.value.contains("relevance")) {
                    sortByFilters.add("orderBy:relevance")
                }
                if (_selectedFilters.value.contains("newest")) {
                    sortByFilters.add("orderBy:newest")
                }

                val finalQuery = buildString {
                    append(query)
                    if (authorFilter.isNotEmpty()) append("+$authorFilter")
                    if (sortByFilters.isNotEmpty()) append("+${sortByFilters.joinToString("+")}")
                }

                if (!skipDelay) {
                    delay(2000)
                }
                val response = fetchBookUseCase(finalQuery)

                val likedBooks = getBooksFromDataSourceUseCase().map { it.title }.toSet()

                val updatedResponse = response.map { book ->
                    book.copy(isLiked = likedBooks.contains(book.title))
                }

                if (updatedResponse.isNotEmpty()) {
                    _state.value = SearchFragmentState.Success(updatedResponse)
                } else {
                    _state.value = SearchFragmentState.Empty
                }
            } catch (e: Exception) {
                Log.d("SearchFragment", e.toString())
                if (!isActive) return@launch
                _state.value = SearchFragmentState.Error
            }
        }
    }

    fun onLikeBook(book: Book, onError: (String) -> Unit, onSuccess: (String) -> Unit) {
        BookUtils.toggleBookLike(
            book,
            saveBookToDataSourceUseCase,
            removeBooksFromDataSourceUseCase,
            viewModelScope,
            { updatedBook ->
                val currentState = _state.value
                if (currentState is SearchFragmentState.Success) {
                    val updatedList = currentState.books.map {
                        if (it.title == updatedBook.title) updatedBook else it
                    }
                    _state.value = SearchFragmentState.Success(updatedList)
                }
            },
            onError = { errorMessage ->
                onError(errorMessage)
            },
            onSuccess = { successMessage ->
                onSuccess(successMessage)
            }
        )
    }


    fun resetState() {
        searchJob?.cancel()
        _state.value = SearchFragmentState.Idle
    }

    fun updateFilters(filters: List<String>) {
        _selectedFilters.value = filters
    }
}
