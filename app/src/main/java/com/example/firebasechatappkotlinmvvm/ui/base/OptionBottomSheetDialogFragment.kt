package com.example.firebasechatappkotlinmvvm.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.firebasechatappkotlinmvvm.R
import com.example.firebasechatappkotlinmvvm.util.CommonUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.framgnet_avatar_option.*

class OptionBottomSheetDialogFragment(
    val optionTitleResIds: List<Int>,
    val onOptionItemClick: OnItemWithPositionClickListener
): BottomSheetDialogFragment() {

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.framgnet_avatar_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
    }

    private fun setupViews(view: View) {
        mListView.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            CommonUtil.toStrings(optionTitleResIds, requireContext()))
        mListView.setOnItemClickListener { _, _, position, _ ->
            dismiss()
            onOptionItemClick.onItemWithPositionClicked(position)
        }
    }

}
