package kadyshev.dmitry.booksapp.fragments

import SnackbarUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kadyshev.dmitry.booksapp.adapters.BookAdapter
import kadyshev.dmitry.booksapp.databinding.FragmentFavoriteBinding
import kadyshev.dmitry.booksapp.states.FavoriteBooksState
import kadyshev.dmitry.booksapp.viewModels.FavoriteViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoriteBinding == null")

    private lateinit var bookAdapter: BookAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)


        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        setupObservers()
        bookAdapter = BookAdapter(
            onBookClick = { book ->
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
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
                favoriteViewModel.onLikeBook(book,
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
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = bookAdapter
        return binding.root
    }

    private fun setupObservers() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteBooksState.collect { state ->
                    when (state) {
                        is FavoriteBooksState.Error -> {}
                        FavoriteBooksState.Idle -> {}
                        is FavoriteBooksState.Success -> showContent(state)
                    }
                }
            }
        }
    }

    private fun showContent(state: FavoriteBooksState.Success) {
        bookAdapter.submitList(state.books)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
