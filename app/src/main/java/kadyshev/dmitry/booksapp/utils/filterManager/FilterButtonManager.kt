package kadyshev.dmitry.booksapp.utils.filterManager

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.flexbox.FlexboxLayout
import kadyshev.dmitry.booksapp.R

class FilterButtonManager(
    private val context: Context,
    private val filterMap: Map<String, String>
) {

    fun createFilterButtons(
        filters: List<String>,
        onFilterRemove: (String) -> Unit,
        addToLayout: (AppCompatButton) -> Unit
    ) {
        filters.forEach { filter ->
            val button = AppCompatButton(context).apply {
                val filterText = filterMap[filter] ?: filter
                text = filterText
                background =
                    ContextCompat.getDrawable(context, R.drawable.filter_button_background)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setPadding(16, 8, 16, 8)
                textSize = 14f
                typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
                isAllCaps = false
                minHeight = 0
                minimumHeight = 0

                val closeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close)
                closeIcon?.setBounds(0, 0, 24, 24)
                setCompoundDrawables(null, null, closeIcon, null)
                compoundDrawablePadding = 8
                setOnClickListener {
                    onFilterRemove(filter)
                }

                val layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.marginEnd = 16
                layoutParams.topMargin = 16
                this.layoutParams = layoutParams
            }
            addToLayout(button)
        }
    }

}
