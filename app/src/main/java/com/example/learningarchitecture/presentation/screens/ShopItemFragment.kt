package com.example.learningarchitecture.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.learningarchitecture.R
import com.example.learningarchitecture.databinding.FragmentShopItemBinding
import com.example.learningarchitecture.domain.ShopItem
import com.example.learningarchitecture.presentation.vm.ShopItemViewModel
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {
    private lateinit var binding: FragmentShopItemBinding
    private lateinit var viewModel: ShopItemViewModel

    private lateinit var titName: TextInputLayout
    private lateinit var titCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    //метод создает view из макета, тут работать еще нельзя, т.к
    // нельзя знать наверняка что view уже создана
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    //метод вызывается когда view уже точно была создана, здесь и начинаем работать

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShopItemBinding.bind(view)
        parseParams() //получаем параментры при создании фрагмента
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java] //инициализация VM
        initViews() // инициализация view элементов
        addTextChange() //слушатели ввода текста
        launchRightMode() //запуск верного режима экрана (add\edit)
        observeViewModel() // подписываемся на обьекты VM
    }

    private fun observeViewModel() {
        //в качестве observe во фрагментах передается viewLifecycleOwner вместо this, т.к
        // жизненный цикл фрагмента и view которую он содержит, отличается. Фрагмент может
        // жить дольше чем живет view
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            titCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            titName.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }


    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChange() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    //если выполнение прошло успешно, и нигде не упали, проинициализируются переменные
// screenMode и shopItemId.
// В зависимости от этих значений мы можем правильно настроить экран
    private fun parseParams() {
        val args = requireArguments() //получаем переданные агрументы
        if (!args.containsKey(SCREEN_MODE)) { //если не содержится ключ, выпадет исключение
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) { //проверка, если содержится какое то
            //непотяное значение, выпадает исключение
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {//если в режиме редактирования не
                // передали SHOP_ITEM_ID, выпадает исключение
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


    private fun initViews() {
        titName = binding.tilCount
        titCount = binding.tilCount
        etName = binding.etName
        etCount = binding.etCount
        buttonSave = binding.saveButton
    }

    //константы для ключ\значение
    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        //запуск экрана в режиме добавления нового элемента
        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        //запуск экрана в режиме редактирования
        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

}