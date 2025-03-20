package kadyshev.dmitry.booksapp.utils

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<T : Any>(
    private val areItemsSame: (T, T) -> Boolean,
    private val areContentsSame: (T, T) -> Boolean = { old, new -> old == new }
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsSame(oldItem, newItem)
}