package kadyshev.dmitry.booksapp.utils.stateManager

import android.view.View
import kadyshev.dmitry.booksapp.R
import kadyshev.dmitry.booksapp.adapters.BookAdapter
import kadyshev.dmitry.booksapp.databinding.FragmentSearchBinding
import kadyshev.dmitry.booksapp.utils.AnimationManager
import kadyshev.dmitry.domain.entities.Book

class SearchFragmentStateManager(private val binding: FragmentSearchBinding) {

    fun showIdleState() = with(binding) {
        val context = root.context
        textContainer.visibility = View.VISIBLE
        textContainer.text = context.getString(R.string.search_prompt)
        recyclerView.visibility = View.INVISIBLE
        AnimationManager.stopRotation(progressBar)
        progressBar.visibility = View.INVISIBLE
    }

    fun showLoadingState() = with(binding) {
        textContainer.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        AnimationManager.rotateIndefinitely(progressBar)
        recyclerView.visibility = View.INVISIBLE
        btnAgain.visibility = View.INVISIBLE
    }

    fun showEmptyState() = with(binding) {
        val context = root.context
        progressBar.visibility = View.INVISIBLE
        AnimationManager.stopRotation(progressBar)
        textContainer.visibility = View.VISIBLE
        textContainer.text = context.getString(R.string.empty_text)
        btnAgain.visibility = View.INVISIBLE
    }

    fun showErrorState() = with(binding) {
        val context = root.context
        progressBar.visibility = View.INVISIBLE
        AnimationManager.stopRotation(progressBar)
        textContainer.visibility = View.VISIBLE
        textContainer.text = context.getString(R.string.error_text)
        btnAgain.visibility = View.VISIBLE
    }

    fun showSuccessState(books: List<Book>) = with(binding) {
        progressBar.visibility = View.INVISIBLE
        AnimationManager.stopRotation(progressBar)
        textContainer.visibility = View.INVISIBLE
        btnAgain.visibility = View.INVISIBLE

        (recyclerView.adapter as BookAdapter).submitList(books)
        recyclerView.visibility = View.VISIBLE
    }
}
