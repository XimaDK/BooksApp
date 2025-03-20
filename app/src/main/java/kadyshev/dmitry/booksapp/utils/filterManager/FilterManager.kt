package kadyshev.dmitry.booksapp.utils.filterManager

import kadyshev.dmitry.booksapp.viewModels.SearchViewModel

class FilterManager(private val viewModel: SearchViewModel) {


    fun removeFilter(filter: String) {
        val currentFilters = viewModel.selectedFilters.value
        val updatedFilters = currentFilters - filter
        viewModel.updateFilters(updatedFilters)
    }

    fun getFilters(): List<String> {
        return viewModel.selectedFilters.value
    }
}
