package kadyshev.dmitry.booksapp.fragments

import SnackbarUtil
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kadyshev.dmitry.booksapp.R
import kadyshev.dmitry.booksapp.adapters.BookAdapter
import kadyshev.dmitry.booksapp.databinding.FragmentSearchBinding
import kadyshev.dmitry.booksapp.states.SearchFragmentState
import kadyshev.dmitry.booksapp.utils.filterManager.FilterButtonManager
import kadyshev.dmitry.booksapp.utils.filterManager.FilterManager
import kadyshev.dmitry.booksapp.utils.stateManager.SearchFragmentStateManager
import kadyshev.dmitry.booksapp.viewModels.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var bookAdapter: BookAdapter
    private lateinit var dimOverlay: View
    private lateinit var stateManager: SearchFragmentStateManager
    private var lastQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateManager = SearchFragmentStateManager(binding)

        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
        setupRetryButton()
        setupBackPressedDispatcher()
        setupFragmentResultListener()
        setupFilterButton()
    }


    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            dimOverlay = requireActivity().findViewById(R.id.dim_overlay)
            dimOverlay.visibility = View.VISIBLE
            val filterFragment = FilterFragment()
            filterFragment.show(childFragmentManager, filterFragment.tag)
        }
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            FilterFragment.FILTER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedFilters =
                bundle.getStringArrayList(FilterFragment.SELECTED_FILTERS) ?: ArrayList()
            val selectedAuthor = bundle.getString(FilterFragment.SELECTED_AUTHOR) ?: ""
            viewModel.updateFilters(selectedFilters)
            viewModel.updateAuthor(selectedAuthor)
            showFilter(selectedFilters)
            dimOverlay.visibility = View.INVISIBLE
            updateFilterIcon()

        }

        childFragmentManager.setFragmentResultListener(
            FilterFragment.FILTER_DISMISSED_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            dimOverlay.visibility = View.INVISIBLE
        }
    }

    private fun showFilter(filters: List<String>) {
        binding.linearFilters.removeAllViews()

        val filterMap = mapOf(
            FilterFragment.RELEVANCE to "Лучшее совпадение",
            FilterFragment.NEWEST to "По дате"
        )

        val filterButtonManager = FilterButtonManager(requireContext(), filterMap)
        val filterManager = FilterManager(viewModel)

        filterButtonManager.createFilterButtons(filters, { filter ->
            filterManager.removeFilter(filter)
            showFilter(filterManager.getFilters())
            viewModel.searchBooks(binding.searchBar.text.toString())
            updateFilterIcon()
        }) { button ->
            binding.linearFilters.addView(button)
        }
    }


    private fun setupBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.searchBar.hasFocus()) {
                        hideKeyboard()
                        binding.searchBar.clearFocus()
                    } else {
                        parentFragmentManager.popBackStack()
                    }
                }
            })
    }


    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            onBookClick = { book ->
                val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    book.imageUrl,
                    book.author,
                    book.title,
                    book.publicationDate,
                    book.description,
                    book.isLiked
                )
                findNavController().navigate(action)
            },
            onLikeClick = { book ->
                viewModel.onLikeBook(book,
                    onError = { errorMessage ->
                        SnackbarUtil.showSnackBar(
                            requireContext(),
                            binding.root,
                            errorMessage,
                            isError = true
                        )
                    },
                    onSuccess = { successMessage ->
                        SnackbarUtil.showSnackBar(
                            requireContext(),
                            binding.root,
                            successMessage,
                            isError = false
                        )
                    }
                )
            }
        )
        binding.recyclerView.adapter = bookAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    private fun setupSearchBar() {
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                val query = binding.searchBar.text.toString().trim()
                if (query != lastQuery) {
                    lastQuery = query
                    viewModel.searchBooks(query, true)
                }
                true
            } else {
                false
            }
        }

        binding.searchBar.doAfterTextChanged { editable ->
            val query = editable?.toString()?.trim() ?: ""
            if (query.isNotEmpty() && query != lastQuery) {
                lastQuery = query
                viewModel.searchBooks(query)
            } else if (query.isEmpty()) {
                viewModel.resetState()
            }
        }

    }

    private fun updateFilterIcon() {
        val currentFilters = viewModel.selectedFilters.value

        if (currentFilters.isEmpty()) {
            val defaultIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter)
            defaultIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
            binding.btnFilter.setImageDrawable(defaultIcon)
        } else {
            val closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter)
            closeIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.blue))
            binding.btnFilter.setImageDrawable(closeIcon)
        }
    }


    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
        binding.searchBar.clearFocus()
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is SearchFragmentState.Idle -> stateManager.showIdleState()
                        is SearchFragmentState.Loading -> stateManager.showLoadingState()
                        is SearchFragmentState.Success -> stateManager.showSuccessState(state.books)
                        is SearchFragmentState.Empty -> stateManager.showEmptyState()
                        is SearchFragmentState.Error -> stateManager.showErrorState()
                    }
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding.btnAgain.setOnClickListener {
            val query = binding.searchBar.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.state.value !is SearchFragmentState.Success) {
            val query = binding.searchBar.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchBooks(query)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}