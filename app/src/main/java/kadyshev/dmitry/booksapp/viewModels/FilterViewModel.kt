package kadyshev.dmitry.booksapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterViewModel : ViewModel() {

    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters: StateFlow<List<String>> = _selectedFilters.asStateFlow()

    private val _selectedAuthors = MutableStateFlow("")
    val selectedAuthors: StateFlow<String> = _selectedAuthors.asStateFlow()

    fun updateAuthor(author: String) {
        _selectedAuthors.value = author
    }

    fun toggleFilter(filter: String) {
        val currentFilters = _selectedFilters.value.toMutableList()
        if (currentFilters.contains(filter)) {
            currentFilters.remove(filter)
        } else {
            currentFilters.add(filter)
        }
        _selectedFilters.value = currentFilters
    }

    fun getCombinedFilters(): List<String> {
        val filters = _selectedFilters.value.toMutableList()
        if (_selectedAuthors.value.isNotBlank()) {
            filters.add(_selectedAuthors.value)
        }
        return filters
    }

}
