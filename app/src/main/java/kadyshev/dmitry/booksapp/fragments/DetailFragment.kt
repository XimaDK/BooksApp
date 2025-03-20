package kadyshev.dmitry.booksapp.fragments

import SnackbarUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import kadyshev.dmitry.booksapp.R
import kadyshev.dmitry.booksapp.databinding.FragmentDetailBinding
import kadyshev.dmitry.booksapp.viewModels.DetailViewModel
import kadyshev.dmitry.domain.entities.Book
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailBinding == null")

    private val viewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)

        val args = DetailFragmentArgs.fromBundle(requireArguments())
        val bookImgUrl = args.bookImgUrl
        val bookTitle = args.bookTitle
        val bookAuthor = args.bookAuthor
        val bookDescription = args.bookDescription
        val publishedDate = args.publishedDate
        val isLiked = args.isLiked

        updateLikeButtonState(isLiked)

        with(binding) {
            tvTitle.text = bookTitle
            tvAuthor.text = bookAuthor
            tvDescription.text = bookDescription
            tvYear.text = getString(R.string.year, publishedDate)
            bookCover.load(bookImgUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_report_image)
                error(android.R.drawable.stat_notify_error)
            }
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnLike.setOnClickListener {
                val book = Book(bookImgUrl, bookTitle, bookAuthor, bookDescription, publishedDate, isLiked)
                viewModel.onLikeBook(book,
                    onError = { errorMessage ->
                        SnackbarUtil.showSnackBar(
                            requireContext(),
                            binding.root,
                            errorMessage,
                            isError = true
                        )
                    },
                    onSuccess = { successMessage, isLikedAfterUpdate ->
                        SnackbarUtil.showSnackBar(
                            requireContext(),
                            binding.root,
                            successMessage,
                            isError = false
                        )
                        updateLikeButtonState(isLikedAfterUpdate)
                    }
                )
            }

            return binding.root
        }
    }

    private fun updateLikeButtonState(isLiked: Boolean) {
        if (isLiked) {
            binding.btnLike.setColorFilter(resources.getColor(R.color.red, null))
        } else {
            binding.btnLike.clearColorFilter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
