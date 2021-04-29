package com.meloda.lineqrreader.fragment.ui

import android.content.Context
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.CollectingActivity
import com.meloda.lineqrreader.activity.InventoryActivity
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.adapter.OnItemLongClickListener
import com.meloda.lineqrreader.extensions.LiveDataExtensions.removeAll
import com.meloda.lineqrreader.extensions.LiveDataExtensions.requireValue
import com.meloda.lineqrreader.extensions.StringExtensions.upperCase
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.InventoryItem
import com.meloda.lineqrreader.scanner.ScannerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import kotlin.random.Random

class CollectingUnassembledPresenter :
    MvpPresenter<CollectingUnassembledView>(),
    OnItemLongClickListener {

    private lateinit var adapter: InventoryAdapter
    private lateinit var context: Context

    private var scanUtil: ScannerUtil? = null
    private var isButtonPressed = false

    private val titles = listOf(
        "Дверь межкомнатная",
        "Стул деревянный кухонный",
        "Микроволновая печь",
        "Холодильник белый",
        "Чайник красный",
        "Сменный фильтр",
        "Стиральная машина",
        "Хлеб кирпич",
        "Майонез",
        "Молоко 1 л.",
        "Масло сливочное 250 гр",
        "Футболка белая",
        "Шорты синие",
        "Трико зелёное",
        "Кроссовки белые",
        "Рюкзак розовый"
    )

    private val alphabet = listOf(
        "а",
        "б",
        "в",
        "г",
        "д",
        "е",
        "ж",
        "з",
        "и",
        "к",
        "л",
        "м",
        "н",
        "о",
        "п",
        "р",
        "с",
        "т",
        "у",
        "ф",
        "х",
        "ц",
        "ч",
        "ш",
        "щ",
        "ы",
        "э",
        "ю",
        "я"
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.prepareViews()

        viewState.createAdapter(object : OnCompleteListener<InventoryAdapter> {
            override fun onComplete(item: InventoryAdapter) {
                context = item.context

                initAdapter(item)
                createItems()
            }
        })
    }

    private fun initAdapter(adapter: InventoryAdapter) {
        this.adapter = adapter.also {
            it.itemLongClickListener = this
        }
    }

    private fun createItems() {
        val items = arrayListOf<InventoryItem>()

        for (i in 0..Random.nextInt(1, 25)) {
            items.add(
                InventoryItem(
                    (i + 1),
                    titles[Random.nextInt(0, titles.size - 1)],
                    Random.nextInt(1, 10),
                    alphabet[Random.nextInt(
                        0,
                        alphabet.size - 1
                    )].upperCase() + Random.nextInt(1000, 9999)
                )
            )
        }

        (context as CollectingActivity).setItemsSize(items.size)

        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    fun setAdapterError(position: Int, errorText: String) {
        adapter.setError(position, errorText)
    }

    fun initScanner() {
        scanUtil = ScannerUtil(context, object : ScannerResultListener {
            override fun onResult(sym: String, content: String) {
                presenterScope.launch(Dispatchers.Main) {
                    with(context as CollectingActivity) {
                        addElement(content)
                    }

                }

                //TODO: move item to assembled and update progressbar
            }
        })

        scanUtil?.init()
    }

    fun releaseScanner() {
        scanUtil?.release()
    }

    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN
        ) {
            if (isButtonPressed) return true

            scanUtil?.startDecoding()
            isButtonPressed = true
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            (context as InventoryActivity).onBackPressed()
            return true
        }

        return false
    }

    fun onKeyUp(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN
        ) {
            if (!isButtonPressed) return true

            scanUtil?.stopDecoding()
            isButtonPressed = false
            return true
        }

        return false
    }

    private fun removeItemsFromAdapter(items: MutableList<InventoryItem>) {
        adapter.values.removeAll(items)
        adapter.notifyDataSetChanged()
    }

    fun showDeleteItemDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle(R.string.warning)
            .setMessage(R.string.delete_scans_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                val item = adapter[position]
                removeItemsFromAdapter(arrayListOf(item))

            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showDeleteAllDialog() {
        AlertDialog.Builder(adapter.context)
            .setTitle(R.string.warning)
            .setMessage(R.string.delete_all_scans_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                presenterScope.launch {
                    removeItemsFromAdapter(adapter.values.requireValue())
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onItemLongClick(position: Int) {
        showDeleteAllDialog()
    }
}