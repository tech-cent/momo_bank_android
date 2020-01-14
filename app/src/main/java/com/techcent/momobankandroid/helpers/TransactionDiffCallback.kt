package com.techcent.momobankandroid.helpers

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.techcent.momobankandroid.models.Transaction

class TransactionDiffCallback(
    private val oldList: List<Transaction>,
    private val newList: List<Transaction>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id === newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
        oldList[oldPosition] == newList[newPosition]

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}