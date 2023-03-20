package com.example.posapp

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentAddMenuBinding
import com.example.posapp.viewModel.MenuViewModel
import com.example.posapp.viewModel.MenuViewModelFactory
import java.io.ByteArrayOutputStream

class FragmentAddMenu : Fragment() {

    private lateinit var imageView: ImageView
    private var productKinds: String = ""
    private var productType: String = ""
    private var tempQuantityInCart: Int = 0
    private var selectedCheckbox: CheckBox? = null
    private var _binding: FragmentAddMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuViewModel by activityViewModels {
        MenuViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        val checkboxMainFood = binding.cbxMainFood
        val checkboxDessert = binding.cbxDessert
        val checkboxDrink = binding.cbxDrink
        imageView = binding.imgImage

        checkboxMainFood.setOnClickListener {
            selectedCheckbox = checkboxMainFood
            checkboxDessert.isChecked = false
            checkboxDrink.isChecked = false
            productKinds = "主食"
            productType = "MAIN_FOOD"
        }
        checkboxDessert.setOnClickListener {
            selectedCheckbox = checkboxDessert
            checkboxMainFood.isChecked = false
            checkboxDrink.isChecked = false
            productKinds = "デザート"
            productType = "DESSERT"
        }
        checkboxDrink.setOnClickListener {
            selectedCheckbox = checkboxDrink
            checkboxDessert.isChecked = false
            checkboxMainFood.isChecked = false
            productKinds = "ドリンク"
            productType = "DRINK"
        }
        return view
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnExit.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddMenu_to_fragmentMenu)
        }
        binding.btnAddMenu.setOnClickListener {
            Toast.makeText(getActivity(), "追加しました！", Toast.LENGTH_SHORT).show()
            addNewItem()
        }
        viewModel.restoreInstanceState(savedInstanceState)
        binding.btnAddImage.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data

            imageView.setImageURI(imageUri)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceState(outState)
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addNewItem() {
        val price = binding.edtPrice.text.toString().toIntOrNull() ?: return
        val quantity = binding.edtQuantity.text.toString().toIntOrNull() ?: return
        viewModel.addNewItem(
            productKinds,
            binding.edtProductName.text.toString(),
            price,
            quantity,
            convertBitmapToByteArray(binding.imgImage.drawToBitmap()),
            productType,
            tempQuantityInCart
        )
        // Clear input fields
        binding.edtPrice.setText("")
        binding.edtQuantity.setText("")
        binding.edtProductName.setText("")
        binding.cbxDrink.isChecked = false
        binding.cbxDessert.isChecked = false
        binding.cbxMainFood.isChecked = false
        binding.imgImage.setImageBitmap(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
        _binding = null
    }
}