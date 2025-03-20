package kadyshev.dmitry.booksapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kadyshev.dmitry.booksapp.R
import kadyshev.dmitry.booksapp.databinding.CardBookItemBinding
import kadyshev.dmitry.booksapp.utils.BaseDiffCallback
import kadyshev.dmitry.domain.entities.Book

class BookAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onLikeClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(
    BaseDiffCallback(areItemsSame = { oldItem, newItem -> oldItem.title == newItem.title })
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding =
            CardBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: CardBookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.bookAuthor.text = book.author
            binding.bookTitle.text = book.title
            binding.bookCover.load(book.imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_report_image)
                error(android.R.drawable.stat_notify_error)
            }

            binding.root.setOnClickListener {
                onBookClick(book)
            }

            binding.favoriteButton.setColorFilter(
                if (book.isLiked) {
                    ContextCompat.getColor(binding.favoriteButton.context, R.color.red)
                } else {
                    ContextCompat.getColor(binding.favoriteButton.context, R.color.light_gray)
                }
            )

            binding.favoriteButton.setOnClickListener {
                onLikeClick(book)
            }
        }
    }

}

