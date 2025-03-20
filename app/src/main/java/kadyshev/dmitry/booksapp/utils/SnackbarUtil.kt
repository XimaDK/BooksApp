import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kadyshev.dmitry.booksapp.R

object SnackbarUtil {

    fun showSnackBar(context: Context, rootView: View, message: String, isError: Boolean = false) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)

        val backgroundColor = if (isError) {
            ContextCompat.getColor(context, R.color.red)
        } else {
            ContextCompat.getColor(context, R.color.blue)
        }
        snackbar.setBackgroundTint(backgroundColor)

        val snackbarView = snackbar.view

        val closeIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_close)
        closeIcon?.setTint(ContextCompat.getColor(context, R.color.white))

        snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.apply {
            setCompoundDrawablesWithIntrinsicBounds(null, null, closeIcon, null)

        }
        val params = snackbarView.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = 300
        snackbarView.layoutParams = params

        snackbarView.setOnClickListener {
            snackbar.dismiss()
        }
        snackbar.show()
    }
}
