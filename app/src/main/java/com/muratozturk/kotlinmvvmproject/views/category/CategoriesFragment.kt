package com.muratozturk.kotlinmvvmproject.views.category

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.muratozturk.kotlinmvvmproject.R
import com.muratozturk.kotlinmvvmproject.databinding.FragmentCategoriesBinding
import com.muratozturk.kotlinmvvmproject.models.Categories
import com.muratozturk.kotlinmvvmproject.repo.Repository
import com.muratozturk.kotlinmvvmproject.utils.CategoriesAdapter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class CategoriesFragment : Fragment(R.layout.fragment_categories) {


    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    private val viewModel by lazy { CategoriesViewModel() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            resources.getString(R.string.categories)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)


        viewModel.getCategories()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            when (it!!) {
                Repository.LOADING.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                }
                Repository.LOADING.DONE -> {
                    binding.shimmerLayout.apply {
                        stopShimmer()
                        visibility = View.GONE
                    }
                    binding.categoriesRecyclerView.visibility = View.VISIBLE
                }

                Repository.LOADING.ERROR -> {
                    binding.shimmerLayout.apply {
                        stopShimmer()
                        visibility = View.GONE
                    }
                    binding.categoriesRecyclerView.visibility = View.VISIBLE
                }
            }

        }



        viewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            val productAdapter = CategoriesAdapter(categories as ArrayList<Categories>)
            binding.categoriesRecyclerView.adapter = productAdapter
            productAdapter.onClick = ::clickCategory

        }
        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.categoriesRecyclerView.setHasFixedSize(true)

    }

    private fun clickCategory(category: Categories) {
        val categoryNavigation =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(
                category
            )

        findNavController().navigate(categoryNavigation)

    }

}