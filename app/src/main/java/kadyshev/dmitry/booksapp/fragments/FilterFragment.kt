package kadyshev.dmitry.booksapp.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kadyshev.dmitry.booksapp.R
import kadyshev.dmitry.booksapp.databinding.FragmentFilterBinding
import kadyshev.dmitry.booksapp.viewModels.FilterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment : DialogFragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding
        get() = _binding ?: throw RuntimeException(ERROR_BINDING)

    private val filterViewModel: FilterViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupFilterSelection()
        setupEditText()
        setupButtons()
    }

    private fun setupEditText() {
        binding.edSearchAuthor.doAfterTextChanged { text ->
            lifecycleScope.launch {
                delay(500)
                filterViewModel.updateAuthor(text.toString())
                updateApplyButtonState(text?.isNotEmpty() == true || filterViewModel.selectedFilters.value.isNotEmpty())
            }
        }
    }

    private fun setupButtons() = with(binding) {
        btnApply.setOnClickListener {
            setFragmentResult(
                FILTER_KEY,
                bundleOf(
                    SELECTED_FILTERS to ArrayList(filterViewModel.getCombinedFilters()),
                    SELECTED_AUTHOR to filterViewModel.selectedAuthors.value
                )
            )
            dismiss()
        }
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.selectedFilters.collect { filters ->
                    updateApplyButtonState(binding.edSearchAuthor.text.isNotEmpty() || filters.isNotEmpty())
                }
            }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult(FILTER_DISMISSED_KEY, Bundle())
    }

    private fun setupFilterSelection() {
        binding.btnSortByDate.setOnClickListener { toggleFilter(it as AppCompatButton) }
        binding.btnSortByBestMatch.setOnClickListener { toggleFilter(it as AppCompatButton) }
    }

    private fun toggleFilter(button: AppCompatButton) {
        val filterText = button.text.toString()

        val filter = when (filterText) {
            getString(R.string.sort_by_best_match) -> RELEVANCE
            getString(R.string.sort_by_date) -> NEWEST
            else -> ""
        }

        if (filter.isNotEmpty()) {
            filterViewModel.toggleFilter(filter)
        }

        button.isSelected = filterViewModel.selectedFilters.value.contains(filter)
    }


    private fun updateApplyButtonState(isEnabled: Boolean) {

        binding.btnApply.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_search)
        binding.btnApply.backgroundTintList = ContextCompat.getColorStateList(
            requireContext(),
            if (isEnabled) R.color.blue else R.color.light_gray
        )
        val textColor = if (isEnabled) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.dark_gray)
        }
        binding.btnApply.setTextColor(textColor)

        binding.btnApply.isEnabled = isEnabled
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setGravity(Gravity.TOP)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (resources.displayMetrics.heightPixels * 0.4).toInt()
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val SELECTED_AUTHOR = "selectedAuthor"
        const val SELECTED_FILTERS = "selectedFilters"
        const val FILTER_KEY = "filter_key"
        const val FILTER_DISMISSED_KEY = "filter_dismissed"
        const val NEWEST = "newest"
        const val RELEVANCE = "relevance"
        const val ERROR_BINDING = "FragmentFilterBinding == null"
    }
}
